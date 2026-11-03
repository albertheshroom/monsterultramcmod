package cz.albert.monsterultra;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public final class ModItems {
	private static final FoodProperties MONSTER_FOOD = new FoodProperties.Builder()
		.nutrition(2)
		.saturationModifier(0.2F)
		.alwaysEdible()
		.build();

	public static final Item EMPTY_CAN = register("empty_can", Item::new, new Item.Properties());

	public static final Map<MonsterFlavor, Item> CANS = new EnumMap<>(MonsterFlavor.class);
	public static final Map<MonsterFlavor, Item> BUCKETS = new EnumMap<>(MonsterFlavor.class);

	static {
		for (MonsterFlavor flavor : MonsterFlavor.values()) {
			Consumable consumable = Consumables.defaultDrink()
				.onConsume(new ApplyStatusEffectsConsumeEffect(flavor.effects()))
				.build();
			CANS.put(flavor, register(flavor.canId(), Item::new, new Item.Properties()
				.food(MONSTER_FOOD, consumable)
				.usingConvertsTo(EMPTY_CAN)
				.stacksTo(16)));
			BUCKETS.put(flavor, register(flavor.bucketId(), Item::new, new Item.Properties()
				.craftRemainder(Items.BUCKET)
				.stacksTo(1)));
		}
	}

	private ModItems() {
	}

	private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, MonsterUltra.id(name));
		return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
	}

	/** Forces class loading so the static item fields get registered. */
	public static void register() {
	}
}
