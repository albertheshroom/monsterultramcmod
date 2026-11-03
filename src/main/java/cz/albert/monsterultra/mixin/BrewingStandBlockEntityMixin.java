package cz.albert.monsterultra.mixin;

import cz.albert.monsterultra.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Lets the brewing stand turn a Monster bucket + iron nuggets into cans without
 * any blaze powder / fuel. The actual conversion is done by the data-driven
 * brewing recipes in {@code data/monsterultra/recipe/brewing/}.
 */
@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
	private static final int MONSTER_BREW_TIME = 400;

	@Shadow
	private int brewTime;
	@Shadow
	private int totalBrewTime;
	@Shadow
	private Item ingredient;

	@Shadow
	private static boolean isBrewable(ServerLevel serverLevel, BrewingStandBlockEntity entity) {
		throw new AssertionError();
	}

	@Inject(method = "serverTick", at = @At("HEAD"))
	private static void monsterultra$brewWithoutFuel(ServerLevel level, BlockPos pos, BlockState state, BrewingStandBlockEntity entity, CallbackInfo ci) {
		BrewingStandBlockEntityMixin self = (BrewingStandBlockEntityMixin) (Object) entity;
		if (self.brewTime > 0) {
			return;
		}

		ItemStack reagent = entity.getItem(3);
		if (ModItems.BUCKETS.containsValue(reagent.getItem()) && isBrewable(level, entity)) {
			// Vanilla decrements brewTime later in this same tick, so start one higher.
			self.brewTime = MONSTER_BREW_TIME + 1;
			self.totalBrewTime = MONSTER_BREW_TIME;
			self.ingredient = reagent.getItem();
			entity.setChanged();
		}
	}
}
