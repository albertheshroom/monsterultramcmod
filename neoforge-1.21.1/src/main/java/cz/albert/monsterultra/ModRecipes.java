package cz.albert.monsterultra;

import cz.albert.monsterultra.recipe.WaterBottleShapelessRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MonsterUltra.MOD_ID);

	public static final DeferredHolder<RecipeSerializer<?>, WaterBottleShapelessRecipe.Serializer> WATER_BOTTLE_SHAPELESS =
		SERIALIZERS.register("crafting_shapeless_bottle_remainder", WaterBottleShapelessRecipe.Serializer::new);

	private ModRecipes() {
	}
}
