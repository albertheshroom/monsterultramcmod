package cz.albert.monsterultra;

import cz.albert.monsterultra.effect.CaffeinatedEffect;
import cz.albert.monsterultra.effect.ClearHarmfulEffectsConsumeEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class ModEffects {
	/** +20 % movement speed, mining speed and attack speed per level (like Speed I + Haste I). */
	public static final Holder<MobEffect> CAFFEINATED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
		MonsterUltra.id("caffeinated"),
		new CaffeinatedEffect()
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, MonsterUltra.id("effect.caffeinated.speed"), 0.2,
				AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, MonsterUltra.id("effect.caffeinated.mining"), 0.2,
				AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.ATTACK_SPEED, MonsterUltra.id("effect.caffeinated.attack"), 0.2,
				AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	private ModEffects() {
	}

	public static void register() {
		Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, MonsterUltra.id("clear_harmful_effects"),
			ClearHarmfulEffectsConsumeEffect.TYPE);
	}
}
