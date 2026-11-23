package cz.albert.monsterultra.mixin;

import cz.albert.monsterultra.item.MonsterBrewingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Lets the brewing stand turn a Monster bucket + iron nuggets into cans without blaze powder. */
@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
	private static final int MONSTER_BREW_TIME = 400;

	@Shadow
	int brewTime;
	@Shadow
	private Item ingredient;
	@Shadow
	private NonNullList<ItemStack> items;

	@Shadow
	private static boolean isBrewable(NonNullList<ItemStack> items) {
		throw new AssertionError();
	}

	@Inject(method = "serverTick", at = @At("HEAD"))
	private static void monsterultra$brewWithoutFuel(Level level, BlockPos pos, BlockState state, BrewingStandBlockEntity entity, CallbackInfo ci) {
		BrewingStandBlockEntityMixin self = (BrewingStandBlockEntityMixin) (Object) entity;
		if (self.brewTime > 0) {
			return;
		}
		ItemStack reagent = self.items.get(3);
		if (MonsterBrewingRecipe.flavorOf(reagent) != null && isBrewable(self.items)) {
			// Vanilla decrements brewTime later in this same tick, so start one higher.
			self.brewTime = MONSTER_BREW_TIME + 1;
			self.ingredient = reagent.getItem();
			entity.setChanged();
		}
	}
}
