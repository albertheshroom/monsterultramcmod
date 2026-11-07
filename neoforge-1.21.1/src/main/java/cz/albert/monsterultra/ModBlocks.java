package cz.albert.monsterultra;

import cz.albert.monsterultra.chemistry.ChemistryTableBlock;
import cz.albert.monsterultra.chemistry.ChemistryTableBlockEntity;
import cz.albert.monsterultra.chemistry.ChemistryTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MonsterUltra.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MonsterUltra.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MonsterUltra.MOD_ID);

	public static final DeferredBlock<ChemistryTableBlock> CHEMISTRY_TABLE = BLOCKS.register("chemistry_table",
		() -> new ChemistryTableBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.METAL)
			.strength(2.5F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.noOcclusion()));

	public static final DeferredItem<BlockItem> CHEMISTRY_TABLE_ITEM = ModItems.ITEMS.registerSimpleBlockItem(CHEMISTRY_TABLE);

	@SuppressWarnings("DataFlowIssue")
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChemistryTableBlockEntity>> CHEMISTRY_TABLE_ENTITY =
		BLOCK_ENTITIES.register("chemistry_table",
			() -> BlockEntityType.Builder.of(ChemistryTableBlockEntity::new, CHEMISTRY_TABLE.get()).build(null));

	public static final DeferredHolder<MenuType<?>, MenuType<ChemistryTableMenu>> CHEMISTRY_TABLE_MENU = MENUS.register("chemistry_table",
		() -> new MenuType<>(ChemistryTableMenu::new, FeatureFlags.VANILLA_SET));

	private ModBlocks() {
	}

	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
		BLOCK_ENTITIES.register(modBus);
		MENUS.register(modBus);
	}
}
