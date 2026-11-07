package cz.albert.monsterultra.item;

import cz.albert.monsterultra.ModItems;
import cz.albert.monsterultra.MonsterFlavor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

/**
 * Brewing stand: Monster bucket on top, iron nuggets in the bottom slots -> cans of that flavor.
 * The bucket's crafting remainder (empty bucket) stays in the top slot. No fuel is needed,
 * see {@code BrewingStandBlockEntityMixin}.
 */
public class MonsterBrewingRecipe implements IBrewingRecipe {
	@Override
	public boolean isInput(ItemStack input) {
		return input.is(Items.IRON_NUGGET);
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return flavorOf(ingredient) != null;
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		MonsterFlavor flavor = flavorOf(ingredient);
		return isInput(input) && flavor != null ? new ItemStack(ModItems.CANS.get(flavor).get()) : ItemStack.EMPTY;
	}

	public static MonsterFlavor flavorOf(ItemStack stack) {
		for (MonsterFlavor flavor : MonsterFlavor.values()) {
			if (stack.is(ModItems.BUCKETS.get(flavor).get())) {
				return flavor;
			}
		}
		return null;
	}
}
