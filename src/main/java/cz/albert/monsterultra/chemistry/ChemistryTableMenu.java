package cz.albert.monsterultra.chemistry;

import static cz.albert.monsterultra.chemistry.ChemistryTableBlockEntity.*;

import cz.albert.monsterultra.ModBlocks;
import cz.albert.monsterultra.ModItems;
import cz.albert.monsterultra.MonsterEssence;
import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChemistryTableMenu extends AbstractContainerMenu {
	private static final Identifier EMPTY_SLOT_POTION = Identifier.withDefaultNamespace("container/slot/potion");
	private static final int INV_SLOT_START = SLOT_COUNT;
	private static final int INV_SLOT_END = INV_SLOT_START + 27;
	private static final int USE_ROW_SLOT_END = INV_SLOT_END + 9;

	private final Container table;
	private final ContainerData data;

	public ChemistryTableMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
	}

	public ChemistryTableMenu(int containerId, Inventory inventory, Container table, ContainerData data) {
		super(ModBlocks.CHEMISTRY_TABLE_MENU, containerId);
		checkContainerSize(table, SLOT_COUNT);
		checkContainerDataCount(data, DATA_COUNT);
		this.table = table;
		this.data = data;
		this.addSlot(new Slot(table, SLOT_INGREDIENT, 79, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return ChemistryRecipes.isIngredient(stack);
			}
		});
		this.addSlot(new VialSlot(table, SLOT_VIAL_A, 56, 51));
		this.addSlot(new VialSlot(table, SLOT_VIAL_B, 102, 51));
		this.addSlot(new Slot(table, SLOT_FUEL, 17, 17) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return ChemistryRecipes.fuelValue(stack) > 0;
			}
		});
		this.addDataSlots(data);
		this.addStandardInventorySlots(inventory, 8, 84);
	}

	@Override
	public boolean stillValid(Player player) {
		return table.stillValid(player);
	}

	/** Button 0 / 1 picks the first / second essence for the fruit in the ingredient slot. */
	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		if ((buttonId == 0 || buttonId == 1) && ChemistryRecipes.isFruit(table.getItem(SLOT_INGREDIENT))) {
			data.set(DATA_SELECTED, buttonId);
			return true;
		}
		return false;
	}

	public List<MonsterEssence> essenceOptions() {
		return ChemistryRecipes.essenceOptions(table.getItem(SLOT_INGREDIENT));
	}

	public int getSelected() {
		return data.get(DATA_SELECTED);
	}

	public int getBrewTime() {
		return data.get(DATA_BREW_TIME);
	}

	public int getFuel() {
		return data.get(DATA_FUEL);
	}

	public boolean hasWater() {
		return data.get(DATA_HAS_WATER) != 0;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		Slot slot = this.slots.get(slotIndex);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		if (slotIndex < SLOT_COUNT) {
			if (!this.moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (ChemistryRecipes.fuelValue(stack) > 0) {
			if (!this.moveItemStackTo(stack, SLOT_FUEL, SLOT_FUEL + 1, false)) {
				return ItemStack.EMPTY;
			}
		} else if (ChemistryRecipes.isIngredient(stack)) {
			if (!this.moveItemStackTo(stack, SLOT_INGREDIENT, SLOT_INGREDIENT + 1, false)) {
				return ItemStack.EMPTY;
			}
		} else if (stack.is(ModItems.EMPTY_VIAL)) {
			if (!this.moveItemStackTo(stack, SLOT_VIAL_A, SLOT_VIAL_B + 1, false)) {
				return ItemStack.EMPTY;
			}
		} else if (slotIndex < INV_SLOT_END) {
			if (!this.moveItemStackTo(stack, INV_SLOT_END, USE_ROW_SLOT_END, false)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false)) {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}
		if (stack.getCount() == original.getCount()) {
			return ItemStack.EMPTY;
		}
		slot.onTake(player, stack);
		return original;
	}

	/** Bottom slots: take one empty vial each; products (sucralose, solution, essences) end up here. */
	private static class VialSlot extends Slot {
		VialSlot(Container container, int slot, int x, int y) {
			super(container, slot, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return stack.is(ModItems.EMPTY_VIAL);
		}

		@Override
		public int getMaxStackSize(ItemStack stack) {
			return stack.is(ModItems.EMPTY_VIAL) ? 1 : super.getMaxStackSize(stack);
		}

		@Override
		public Identifier getNoItemIcon() {
			return EMPTY_SLOT_POTION;
		}
	}
}
