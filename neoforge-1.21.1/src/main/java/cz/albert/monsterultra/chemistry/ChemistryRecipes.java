package cz.albert.monsterultra.chemistry;

import cz.albert.monsterultra.ModItems;
import cz.albert.monsterultra.MonsterEssence;
import cz.albert.monsterultra.MonsterUltra;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** What the chemistry table can make. */
public final class ChemistryRecipes {
	/** Mushrooms and fungi: fuel for 1 brew. */
	public static final TagKey<Item> FUEL_SMALL = TagKey.create(Registries.ITEM, MonsterUltra.id("chemistry_fuel_small"));
	/** Mushroom blocks and stems: fuel for 4 brews. */
	public static final TagKey<Item> FUEL_LARGE = TagKey.create(Registries.ITEM, MonsterUltra.id("chemistry_fuel_large"));

	/** Fruit -> the two essences the player can choose from. */
	private static final Map<Item, List<MonsterEssence>> FRUIT_ESSENCES = Map.of(
		Items.GOLDEN_APPLE, List.of(MonsterEssence.PINEAPPLE, MonsterEssence.ROSA),
		Items.GLOW_BERRIES, List.of(MonsterEssence.CITRUS, MonsterEssence.DREAMBERRY),
		Items.GLISTERING_MELON_SLICE, List.of(MonsterEssence.COLDREX, MonsterEssence.YUZU),
		Items.MELON_SLICE, List.of(MonsterEssence.WATERMELON, MonsterEssence.MANGO),
		Items.APPLE, List.of(MonsterEssence.GRAPEFRUIT, MonsterEssence.PEACH),
		Items.BEETROOT, List.of(MonsterEssence.GUAVA, MonsterEssence.CHERRY),
		Items.SWEET_BERRIES, List.of(MonsterEssence.BLUE_RASPBERRY, MonsterEssence.REDBERRY),
		Items.CHORUS_FRUIT, List.of(MonsterEssence.GRAPE, MonsterEssence.KIWI)
	);

	private ChemistryRecipes() {
	}

	public static boolean isSugar(ItemStack stack) {
		return stack.is(Items.SUGAR);
	}

	public static boolean isFruit(ItemStack stack) {
		return FRUIT_ESSENCES.containsKey(stack.getItem());
	}

	public static boolean isIngredient(ItemStack stack) {
		return isSugar(stack) || isFruit(stack);
	}

	/** The two essence choices for a fruit, or an empty list. */
	public static List<MonsterEssence> essenceOptions(ItemStack fruit) {
		return FRUIT_ESSENCES.getOrDefault(fruit.getItem(), List.of());
	}

	public static Item essenceItem(MonsterEssence essence) {
		return ModItems.ESSENCES.get(essence).get();
	}

	/** Number of brews one item of this fuel gives, 0 if it is not a fuel. */
	public static int fuelValue(ItemStack stack) {
		if (stack.is(FUEL_LARGE)) {
			return 4;
		}
		return stack.is(FUEL_SMALL) ? 1 : 0;
	}
}
