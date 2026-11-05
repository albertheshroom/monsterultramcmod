package cz.albert.monsterultra.effect;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

/** Removes every harmful (negative) status effect from the consumer. JSON type: {@code monsterultra:clear_harmful_effects}. */
public record ClearHarmfulEffectsConsumeEffect() implements ConsumeEffect {
	public static final ClearHarmfulEffectsConsumeEffect INSTANCE = new ClearHarmfulEffectsConsumeEffect();
	public static final MapCodec<ClearHarmfulEffectsConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, ClearHarmfulEffectsConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);
	public static final ConsumeEffect.Type<ClearHarmfulEffectsConsumeEffect> TYPE = new ConsumeEffect.Type<>(CODEC, STREAM_CODEC);

	@Override
	public ConsumeEffect.Type<ClearHarmfulEffectsConsumeEffect> getType() {
		return TYPE;
	}

	@Override
	public boolean apply(Level level, ItemStack stack, LivingEntity user) {
		List<Holder<MobEffect>> harmful = user.getActiveEffects().stream()
			.map(MobEffectInstance::getEffect)
			.filter(effect -> effect.value().getCategory() == MobEffectCategory.HARMFUL)
			.toList();
		boolean removed = false;
		for (Holder<MobEffect> effect : harmful) {
			removed |= user.removeEffect(effect);
		}
		return removed;
	}
}
