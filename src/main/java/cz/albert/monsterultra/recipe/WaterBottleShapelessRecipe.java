package cz.albert.monsterultra.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Shapeless crafting recipe that gives back an empty glass bottle for every
 * potion (e.g. water bottle) used as an ingredient. Vanilla potions have no
 * crafting remainder, so a plain shapeless recipe would eat the bottle.
 *
 * <p>JSON type: {@code monsterultra:crafting_shapeless_bottle_remainder}, same fields as
 * {@code minecraft:crafting_shapeless}.
 */
public class WaterBottleShapelessRecipe extends ShapelessRecipe {
	public static final MapCodec<WaterBottleShapelessRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
		i -> i.group(
				Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
				CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
				ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
				Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients)
			)
			.apply(i, WaterBottleShapelessRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, WaterBottleShapelessRecipe> STREAM_CODEC = StreamCodec.composite(
		Recipe.CommonInfo.STREAM_CODEC,
		o -> o.commonInfo,
		CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
		o -> o.bookInfo,
		ItemStackTemplate.STREAM_CODEC,
		o -> o.result,
		Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
		o -> o.ingredients,
		WaterBottleShapelessRecipe::new
	);
	public static final RecipeSerializer<WaterBottleShapelessRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final ItemStackTemplate result;
	private final List<Ingredient> ingredients;

	public WaterBottleShapelessRecipe(
		Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ItemStackTemplate result, List<Ingredient> ingredients
	) {
		super(commonInfo, bookInfo, result, ingredients);
		this.result = result;
		this.ingredients = ingredients;
	}

	@Override
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		@SuppressWarnings("unchecked")
		RecipeSerializer<ShapelessRecipe> serializer = (RecipeSerializer<ShapelessRecipe>) (RecipeSerializer<?>) SERIALIZER;
		return serializer;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> remaining = CraftingRecipe.defaultCraftingReminder(input);
		for (int slot = 0; slot < remaining.size(); slot++) {
			if (remaining.get(slot).isEmpty() && input.getItem(slot).has(DataComponents.POTION_CONTENTS)) {
				remaining.set(slot, new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return remaining;
	}
}
