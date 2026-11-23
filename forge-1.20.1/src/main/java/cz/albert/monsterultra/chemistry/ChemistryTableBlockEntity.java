package cz.albert.monsterultra.chemistry;

import cz.albert.monsterultra.ModBlocks;
import cz.albert.monsterultra.ModItems;
import cz.albert.monsterultra.MonsterEssence;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

/**
 * Chemistry table: turns sugar into sucralose / sucralose solution and fruit into essences.
 * Needs mushroom fuel and water touching one of its six faces.
 */
public class ChemistryTableBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
	public static final int SLOT_INGREDIENT = 0;
	public static final int SLOT_VIAL_A = 1;
	public static final int SLOT_VIAL_B = 2;
	public static final int SLOT_FUEL = 3;
	public static final int SLOT_COUNT = 4;

	public static final int DATA_BREW_TIME = 0;
	public static final int DATA_FUEL = 1;
	public static final int DATA_SELECTED = 2;
	public static final int DATA_HAS_WATER = 3;
	public static final int DATA_COUNT = 4;

	public static final int BREW_TIME = 400;
	public static final int MAX_FUEL = 4;

	private static final int[] SLOTS_FOR_UP = {SLOT_INGREDIENT};
	private static final int[] SLOTS_FOR_DOWN = {SLOT_VIAL_A, SLOT_VIAL_B};
	private static final int[] SLOTS_FOR_SIDES = {SLOT_VIAL_A, SLOT_VIAL_B, SLOT_FUEL};

	private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
	private int brewTime;
	private int fuel;
	/** Chosen essence (0 or 1) for the fruit in the ingredient slot, -1 = none chosen. */
	private int selected = -1;
	/** Fruit the selection was made for; changing the fruit clears the selection. */
	private @Nullable Item selectedFor;
	private @Nullable Item brewingIngredient;
	private boolean hasWater;

	protected final ContainerData dataAccess = new ContainerData() {
		@Override
		public int get(int id) {
			return switch (id) {
				case DATA_BREW_TIME -> brewTime;
				case DATA_FUEL -> fuel;
				case DATA_SELECTED -> selected;
				case DATA_HAS_WATER -> hasWater ? 1 : 0;
				default -> 0;
			};
		}

		@Override
		public void set(int id, int value) {
			switch (id) {
				case DATA_BREW_TIME -> brewTime = value;
				case DATA_FUEL -> fuel = value;
				case DATA_SELECTED -> select(value);
				case DATA_HAS_WATER -> hasWater = value != 0;
				default -> {
				}
			}
		}

		@Override
		public int getCount() {
			return DATA_COUNT;
		}
	};

	public ChemistryTableBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlocks.CHEMISTRY_TABLE_ENTITY.get(), pos, state);
	}

	private void select(int choice) {
		ItemStack ingredient = items.get(SLOT_INGREDIENT);
		if (ChemistryRecipes.isFruit(ingredient) && (choice == 0 || choice == 1)) {
			selected = choice;
			selectedFor = ingredient.getItem();
		} else {
			selected = -1;
			selectedFor = null;
		}
		setChanged();
	}

	public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ChemistryTableBlockEntity table) {
		boolean changed = false;

		ItemStack ingredient = table.items.get(SLOT_INGREDIENT);
		if (table.selected != -1 && !ingredient.is(table.selectedFor)) {
			table.selected = -1;
			table.selectedFor = null;
			changed = true;
		}

		ItemStack fuelStack = table.items.get(SLOT_FUEL);
		int fuelValue = ChemistryRecipes.fuelValue(fuelStack);
		if (table.fuel <= 0 && fuelValue > 0) {
			table.fuel = fuelValue;
			fuelStack.shrink(1);
			changed = true;
		}

		table.hasWater = touchesWater(level, pos);
		boolean canBrew = table.hasWater && table.outputFor(ingredient) != null;

		if (table.brewTime > 0) {
			table.brewTime--;
			if (!canBrew || !ingredient.is(table.brewingIngredient)) {
				table.brewTime = 0;
			} else if (table.brewTime == 0) {
				table.brew(level, pos);
			}
			changed = true;
		} else if (canBrew && table.fuel > 0) {
			table.fuel--;
			table.brewTime = BREW_TIME;
			table.brewingIngredient = ingredient.getItem();
			changed = true;
		}

		if (changed) {
			setChanged(level, pos, state);
		}
	}

	/** True if water (source, flowing, waterlogged block or water cauldron) touches one of the six faces. */
	public static boolean touchesWater(Level level, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			BlockPos neighbor = pos.relative(direction);
			if (level.getFluidState(neighbor).is(FluidTags.WATER) || level.getBlockState(neighbor).is(Blocks.WATER_CAULDRON)) {
				return true;
			}
		}
		return false;
	}

	/** What a brew would put into the two vial slots right now, or null if nothing can be brewed. */
	private @Nullable List<ItemStack> outputFor(ItemStack ingredient) {
		ItemStack a = items.get(SLOT_VIAL_A);
		ItemStack b = items.get(SLOT_VIAL_B);
		boolean twoVials = a.is(ModItems.EMPTY_VIAL.get()) && b.is(ModItems.EMPTY_VIAL.get());

		if (ChemistryRecipes.isSugar(ingredient)) {
			if (twoVials) {
				return List.of(new ItemStack(ModItems.SUCRALOSE_SOLUTION.get()), new ItemStack(ModItems.SUCRALOSE_SOLUTION.get()));
			}
			if (a.isEmpty()) {
				return List.of(new ItemStack(ModItems.SUCRALOSE.get()), b);
			}
			if (a.is(ModItems.SUCRALOSE.get()) && a.getCount() < a.getMaxStackSize()) {
				return List.of(a.copyWithCount(a.getCount() + 1), b);
			}
			return null;
		}

		if (ChemistryRecipes.isFruit(ingredient) && twoVials && selected >= 0) {
			MonsterEssence essence = ChemistryRecipes.essenceOptions(ingredient).get(selected);
			Item essenceItem = ChemistryRecipes.essenceItem(essence);
			return List.of(new ItemStack(essenceItem), new ItemStack(essenceItem));
		}
		return null;
	}

	private void brew(Level level, BlockPos pos) {
		ItemStack ingredient = items.get(SLOT_INGREDIENT);
		List<ItemStack> output = outputFor(ingredient);
		if (output == null) {
			return;
		}
		items.set(SLOT_VIAL_A, output.get(0));
		items.set(SLOT_VIAL_B, output.get(1));
		ingredient.shrink(1);
		level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.monsterultra.chemistry_table");
	}

	@Override
	public int getContainerSize() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot) {
		return items.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return ContainerHelper.removeItem(items, slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		items.set(slot, stack);
		if (stack.getCount() > getMaxStackSize()) {
			stack.setCount(getMaxStackSize());
		}
		setChanged();
	}

	@Override
	public boolean stillValid(net.minecraft.world.entity.player.Player player) {
		return net.minecraft.world.Container.stillValidBlockEntity(this, player);
	}

	@Override
	public void clearContent() {
		items.clear();
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, items);
		brewTime = tag.getInt("BrewTime");
		fuel = tag.getInt("Fuel");
		selected = tag.contains("Selected") ? tag.getInt("Selected") : -1;
		ItemStack ingredient = items.get(SLOT_INGREDIENT);
		selectedFor = selected >= 0 && !ingredient.isEmpty() ? ingredient.getItem() : null;
		brewingIngredient = brewTime > 0 ? ingredient.getItem() : null;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, items);
		tag.putInt("BrewTime", brewTime);
		tag.putInt("Fuel", fuel);
		tag.putInt("Selected", selected);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return switch (slot) {
			case SLOT_INGREDIENT -> ChemistryRecipes.isIngredient(stack);
			case SLOT_VIAL_A, SLOT_VIAL_B -> stack.is(ModItems.EMPTY_VIAL.get()) && getItem(slot).isEmpty();
			case SLOT_FUEL -> ChemistryRecipes.fuelValue(stack) > 0;
			default -> false;
		};
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.UP) {
			return SLOTS_FOR_UP;
		}
		return direction == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
		return canPlaceItem(slot, stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
		// Hoppers only pull finished products, never empty vials waiting to be filled.
		return (slot == SLOT_VIAL_A || slot == SLOT_VIAL_B) && !stack.is(ModItems.EMPTY_VIAL.get());
	}

	@Override
	protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
		return new ChemistryTableMenu(containerId, inventory, this, dataAccess);
	}
}
