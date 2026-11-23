package cz.albert.monsterultra.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import cz.albert.monsterultra.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
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

	public WaterBottleShapelessRecipe(ResourceLocation id, String group, CraftingBookCategory category, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(id, group, category, result, ingredients);
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
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> remaining = super.getRemainingItems(container);
		for (int slot = 0; slot < remaining.size(); slot++) {
			ItemStack stack = container.getItem(slot);
			if (remaining.get(slot).isEmpty() && (stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION))) {
				remaining.set(slot, new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		return remaining;
	}

	public static class Serializer implements RecipeSerializer<WaterBottleShapelessRecipe> {
		@Override
		public WaterBottleShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");
			CraftingBookCategory category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
			JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (int i = 0; i < array.size(); i++) {
				Ingredient ingredient = Ingredient.fromJson(array.get(i), false);
				if (!ingredient.isEmpty()) {
					ingredients.add(ingredient);
				}
			}
			if (ingredients.isEmpty() || ingredients.size() > 9) {
				throw new JsonParseException("Shapeless recipe needs 1 to 9 ingredients");
			}
			ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			return new WaterBottleShapelessRecipe(id, group, category, result, ingredients);
		}

		@Override
		public WaterBottleShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			CraftingBookCategory category = buffer.readEnum(CraftingBookCategory.class);
			int size = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
			ItemStack result = buffer.readItem();
			return new WaterBottleShapelessRecipe(id, group, category, result, ingredients);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, WaterBottleShapelessRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeEnum(recipe.category);
			buffer.writeVarInt(recipe.ingredients.size());
			for (Ingredient ingredient : recipe.ingredients) {
				ingredient.toNetwork(buffer);
			}
			buffer.writeItem(recipe.result);
		}
	}
}
