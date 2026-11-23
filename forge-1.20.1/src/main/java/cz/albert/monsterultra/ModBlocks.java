package cz.albert.monsterultra;

import cz.albert.monsterultra.chemistry.ChemistryTableBlock;
import cz.albert.monsterultra.chemistry.ChemistryTableBlockEntity;
import cz.albert.monsterultra.chemistry.ChemistryTableMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
	public static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MonsterUltra.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MonsterUltra.MOD_ID);
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MonsterUltra.MOD_ID);

	public static final RegistryObject<ChemistryTableBlock> CHEMISTRY_TABLE = BLOCKS.register("chemistry_table",
		() -> new ChemistryTableBlock(BlockBehaviour.Properties.of()
			.mapColor(MapColor.METAL)
			.strength(2.5F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.noOcclusion()));

	public static final RegistryObject<BlockItem> CHEMISTRY_TABLE_ITEM = ModItems.ITEMS.register("chemistry_table",
		() -> new BlockItem(CHEMISTRY_TABLE.get(), new Item.Properties()));

	@SuppressWarnings("DataFlowIssue")
	public static final RegistryObject<BlockEntityType<ChemistryTableBlockEntity>> CHEMISTRY_TABLE_ENTITY =
		BLOCK_ENTITIES.register("chemistry_table",
			() -> BlockEntityType.Builder.of(ChemistryTableBlockEntity::new, CHEMISTRY_TABLE.get()).build(null));

	public static final RegistryObject<MenuType<ChemistryTableMenu>> CHEMISTRY_TABLE_MENU = MENUS.register("chemistry_table",
		() -> new MenuType<>(ChemistryTableMenu::new, FeatureFlags.VANILLA_SET));

	private ModBlocks() {
	}

	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
		BLOCK_ENTITIES.register(modBus);
		MENUS.register(modBus);
	}
}
