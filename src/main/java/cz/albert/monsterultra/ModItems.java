package cz.albert.monsterultra;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import cz.albert.monsterultra.effect.ClearHarmfulEffectsConsumeEffect;

public final class ModItems {
	private static final FoodProperties MONSTER_FOOD = new FoodProperties.Builder()
		.nutrition(2)
		.saturationModifier(0.2F)
		.alwaysEdible()
		.build();

	public static final Item EMPTY_CAN = register("empty_can", Item::new, new Item.Properties());

	public static final Item EMPTY_VIAL = register("empty_vial", Item::new, new Item.Properties());
	public static final Item SUCRALOSE = register("sucralose", Item::new, new Item.Properties());
	public static final Item SUCRALOSE_SOLUTION = register("sucralose_solution", Item::new, new Item.Properties()
		.craftRemainder(EMPTY_VIAL));

	public static final Map<MonsterEssence, Item> ESSENCES = new EnumMap<>(MonsterEssence.class);
	public static final Map<MonsterFlavor, Item> CANS = new EnumMap<>(MonsterFlavor.class);
	public static final Map<MonsterFlavor, Item> BUCKETS = new EnumMap<>(MonsterFlavor.class);

	static {
		for (MonsterEssence essence : MonsterEssence.values()) {
			// Essences come in a vial: the Cooking Pot (and crafting) gives the empty vial back.
			ESSENCES.put(essence, register(essence.itemId(), Item::new, new Item.Properties().craftRemainder(EMPTY_VIAL)));
		}

		for (MonsterFlavor flavor : MonsterFlavor.values()) {
			Consumable.Builder drink = Consumables.defaultDrink();
			if (flavor.clearsHarmfulEffects()) {
				drink.onConsume(ClearHarmfulEffectsConsumeEffect.INSTANCE);
			}
			Consumable consumable = drink
				.onConsume(new ApplyStatusEffectsConsumeEffect(flavor.effects()))
				.build();
			ItemLore flavorTooltip = flavorTooltip(flavor);
			CANS.put(flavor, register(flavor.canId(), Item::new, new Item.Properties()
				.component(DataComponents.LORE, flavorTooltip)
				.food(MONSTER_FOOD, consumable)
				.usingConvertsTo(EMPTY_CAN)
				.stacksTo(16)));
			BUCKETS.put(flavor, register(flavor.bucketId(), Item::new, new Item.Properties()
				.component(DataComponents.LORE, flavorTooltip)
				.craftRemainder(Items.BUCKET)
				.stacksTo(1)));
		}
	}

	private ModItems() {
	}

	/** Gray, non-italic tooltip line with the flavor name, e.g. "VR46 Zero Sugar". */
	private static ItemLore flavorTooltip(MonsterFlavor flavor) {
		return new ItemLore(List.of(Component.translatable(flavor.translationKey())
			.withStyle(style -> style.withColor(ChatFormatting.GRAY).withItalic(false))));
	}

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, MonsterUltra.id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	/** Forces class loading so the static item fields get registered. */
	public static void register() {
	}
}
