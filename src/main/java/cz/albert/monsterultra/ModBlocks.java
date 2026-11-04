package cz.albert.monsterultra;

import cz.albert.monsterultra.chemistry.ChemistryTableBlock;
import cz.albert.monsterultra.chemistry.ChemistryTableBlockEntity;
import cz.albert.monsterultra.chemistry.ChemistryTableMenu;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {
	public static final ResourceKey<Block> CHEMISTRY_TABLE_KEY = ResourceKey.create(Registries.BLOCK, MonsterUltra.id("chemistry_table"));

	public static final Block CHEMISTRY_TABLE = Registry.register(BuiltInRegistries.BLOCK, CHEMISTRY_TABLE_KEY,
		new ChemistryTableBlock(BlockBehaviour.Properties.of()
			.setId(CHEMISTRY_TABLE_KEY)
			.mapColor(MapColor.METAL)
			.strength(2.5F)
			.sound(SoundType.METAL)
			.requiresCorrectToolForDrops()
			.noOcclusion()));

	public static final Item CHEMISTRY_TABLE_ITEM = registerBlockItem(CHEMISTRY_TABLE);

	public static final BlockEntityType<ChemistryTableBlockEntity> CHEMISTRY_TABLE_ENTITY = Registry.register(
		BuiltInRegistries.BLOCK_ENTITY_TYPE, MonsterUltra.id("chemistry_table"),
		new BlockEntityType<>(ChemistryTableBlockEntity::new, Set.of(CHEMISTRY_TABLE)));

	public static final MenuType<ChemistryTableMenu> CHEMISTRY_TABLE_MENU = Registry.register(
		BuiltInRegistries.MENU, MonsterUltra.id("chemistry_table"),
		new MenuType<>(ChemistryTableMenu::new, FeatureFlags.VANILLA_SET));

	private ModBlocks() {
	}

	private static Item registerBlockItem(Block block) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, BuiltInRegistries.BLOCK.getKey(block));
		return Registry.register(BuiltInRegistries.ITEM, key,
			new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(key)));
	}

	/** Forces class loading so the static fields get registered. */
	public static void register() {
	}
}
