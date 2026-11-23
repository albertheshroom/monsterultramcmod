package cz.albert.monsterultra;

import cz.albert.monsterultra.recipe.WaterBottleShapelessRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MonsterUltra.MOD_ID);

	public static final RegistryObject<WaterBottleShapelessRecipe.Serializer> WATER_BOTTLE_SHAPELESS =
		SERIALIZERS.register("crafting_shapeless_bottle_remainder", WaterBottleShapelessRecipe.Serializer::new);

	private ModRecipes() {
	}
}
