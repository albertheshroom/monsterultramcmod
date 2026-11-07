package cz.albert.monsterultra;

import cz.albert.monsterultra.item.MonsterCanItem;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MonsterUltra.MOD_ID);

	private static final FoodProperties MONSTER_FOOD = new FoodProperties.Builder()
		.nutrition(2)
		.saturationModifier(0.2F)
		.alwaysEdible()
		.build();

	public static final DeferredItem<Item> EMPTY_CAN = ITEMS.registerSimpleItem("empty_can");
	public static final DeferredItem<Item> EMPTY_VIAL = ITEMS.registerSimpleItem("empty_vial");
	public static final DeferredItem<Item> SUCRALOSE = ITEMS.registerSimpleItem("sucralose");
	public static final DeferredItem<Item> SUCRALOSE_SOLUTION = ITEMS.registerItem("sucralose_solution",
		properties -> new Item(properties.craftRemainder(EMPTY_VIAL.get())), new Item.Properties());

	public static final Map<MonsterEssence, DeferredItem<Item>> ESSENCES = new EnumMap<>(MonsterEssence.class);
	public static final Map<MonsterFlavor, DeferredItem<Item>> CANS = new EnumMap<>(MonsterFlavor.class);
	public static final Map<MonsterFlavor, DeferredItem<Item>> BUCKETS = new EnumMap<>(MonsterFlavor.class);

	static {
		for (MonsterEssence essence : MonsterEssence.values()) {
			// Essences come in a vial: the Cooking Pot (and crafting) gives the empty vial back.
			ESSENCES.put(essence, ITEMS.registerItem(essence.itemId(),
				properties -> new Item(properties.craftRemainder(EMPTY_VIAL.get())), new Item.Properties()));
		}
		for (MonsterFlavor flavor : MonsterFlavor.values()) {
			ItemLore flavorTooltip = new ItemLore(List.of(Component.translatable(flavor.translationKey())
				.withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false))));
			CANS.put(flavor, ITEMS.registerItem(flavor.canId(), properties -> new MonsterCanItem(flavor, properties),
				new Item.Properties().food(MONSTER_FOOD).stacksTo(16).component(DataComponents.LORE, flavorTooltip)));
			BUCKETS.put(flavor, ITEMS.registerItem(flavor.bucketId(), Item::new,
				new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).component(DataComponents.LORE, flavorTooltip)));
		}
	}

	private ModItems() {
	}
}
