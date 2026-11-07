package cz.albert.monsterultra.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cz.albert.monsterultra.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Shapeless crafting recipe that gives back an empty glass bottle for every potion
 * (e.g. water bottle) used as an ingredient. JSON type:
 * {@code monsterultra:crafting_shapeless_bottle_remainder}, same fields as {@code minecraft:crafting_shapeless}.
 */
public class WaterBottleShapelessRecipe extends ShapelessRecipe {
	private final String group;
	private final CraftingBookCategory category;
	private final ItemStack result;
	private final NonNullList<Ingredient> ingredients;

	public WaterBottleShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(group, category, result, ingredients);
		this.group = group;
		this.category = category;
		this.result = result;
		this.ingredients = ingredients;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.WATER_BOTTLE_SHAPELESS.get();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> remaining = super.getRemainingItems(input);
		for (int slot = 0; slot < remaining.size(); slot++) {
			if (remaining.get(slot).isEmpty() && input.getItem(slot).has(DataComponents.POTION_CONTENTS)) {
				remaining.set(slot, new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return remaining;
	}

	public static class Serializer implements RecipeSerializer<WaterBottleShapelessRecipe> {
		private static final MapCodec<WaterBottleShapelessRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(r -> r.group),
				CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(r -> r.category),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
				Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(
					list -> list.isEmpty() || list.size() > 9
						? DataResult.<NonNullList<Ingredient>>error(() -> "Shapeless recipe needs 1 to 9 ingredients")
						: DataResult.success(NonNullList.of(Ingredient.EMPTY, list.toArray(Ingredient[]::new))),
					DataResult::success).forGetter(r -> r.ingredients))
			.apply(i, WaterBottleShapelessRecipe::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, WaterBottleShapelessRecipe> STREAM_CODEC = StreamCodec.of(
			Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<WaterBottleShapelessRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, WaterBottleShapelessRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static WaterBottleShapelessRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
			int size = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
			ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
			ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
			return new WaterBottleShapelessRecipe(group, category, result, ingredients);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, WaterBottleShapelessRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeEnum(recipe.category);
			buffer.writeVarInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
			}
			ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
		}
	}
}
