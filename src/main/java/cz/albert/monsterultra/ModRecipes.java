package cz.albert.monsterultra;

import cz.albert.monsterultra.recipe.WaterBottleShapelessRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModRecipes {
	private ModRecipes() {
	}

	public static void register() {
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, MonsterUltra.id("crafting_shapeless_bottle_remainder"),
			WaterBottleShapelessRecipe.SERIALIZER);
	}
}
