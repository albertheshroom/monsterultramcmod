package cz.albert.monsterultra.item;

import cz.albert.monsterultra.ModItems;
import cz.albert.monsterultra.MonsterFlavor;
import java.util.List;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/** A can of Monster: drunk like a potion, gives its effects and leaves an empty can. */
public class MonsterCanItem extends FlavoredItem {
	public MonsterCanItem(MonsterFlavor flavor, Properties properties) {
		super(flavor, properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (!level.isClientSide()) {
			if (flavor.clearsHarmfulEffects()) {
				List<MobEffect> harmful = entity.getActiveEffects().stream()
					.map(MobEffectInstance::getEffect)
					.filter(effect -> effect.getCategory() == MobEffectCategory.HARMFUL)
					.toList();
				harmful.forEach(entity::removeEffect);
			}
			for (MobEffectInstance effect : flavor.effects()) {
				entity.addEffect(effect);
			}
		}

		ItemStack result = super.finishUsingItem(stack, level, entity);
		if (entity instanceof Player player && player.getAbilities().instabuild) {
			return result;
		}
		ItemStack emptyCan = new ItemStack(ModItems.EMPTY_CAN.get());
		if (result.isEmpty()) {
			return emptyCan;
		}
		if (entity instanceof Player player && !player.getInventory().add(emptyCan)) {
			player.drop(emptyCan, false);
		}
		return result;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.GENERIC_DRINK;
	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.GENERIC_DRINK;
	}
}
