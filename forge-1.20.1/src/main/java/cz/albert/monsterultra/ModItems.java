package cz.albert.monsterultra;

import cz.albert.monsterultra.item.FlavoredItem;
import cz.albert.monsterultra.item.MonsterCanItem;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MonsterUltra.MOD_ID);

	private static final FoodProperties MONSTER_FOOD = new FoodProperties.Builder()
		.nutrition(2)
		.saturationMod(0.2F)
		.alwaysEat()
		.build();

	public static final RegistryObject<Item> EMPTY_CAN = ITEMS.register("empty_can", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> EMPTY_VIAL = ITEMS.register("empty_vial", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SUCRALOSE = ITEMS.register("sucralose", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SUCRALOSE_SOLUTION = ITEMS.register("sucralose_solution",
		() -> new Item(new Item.Properties().craftRemainder(EMPTY_VIAL.get())));

	public static final Map<MonsterEssence, RegistryObject<Item>> ESSENCES = new EnumMap<>(MonsterEssence.class);
	public static final Map<MonsterFlavor, RegistryObject<Item>> CANS = new EnumMap<>(MonsterFlavor.class);
	public static final Map<MonsterFlavor, RegistryObject<Item>> BUCKETS = new EnumMap<>(MonsterFlavor.class);

	static {
		for (MonsterEssence essence : MonsterEssence.values()) {
			// Essences come in a vial: the Cooking Pot (and crafting) gives the empty vial back.
			ESSENCES.put(essence, ITEMS.register(essence.itemId(),
				() -> new Item(new Item.Properties().craftRemainder(EMPTY_VIAL.get()))));
		}
		for (MonsterFlavor flavor : MonsterFlavor.values()) {
			CANS.put(flavor, ITEMS.register(flavor.canId(),
				() -> new MonsterCanItem(flavor, new Item.Properties().food(MONSTER_FOOD).stacksTo(16))));
			BUCKETS.put(flavor, ITEMS.register(flavor.bucketId(),
				() -> new FlavoredItem(flavor, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))));
		}
	}

	private ModItems() {
	}
}
