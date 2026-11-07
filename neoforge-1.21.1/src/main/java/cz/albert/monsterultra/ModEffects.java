package cz.albert.monsterultra;

import cz.albert.monsterultra.effect.CaffeinatedEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MonsterUltra.MOD_ID);

	/** +20 % movement speed, mining speed and attack speed per level (like Speed I + Haste I). */
	public static final DeferredHolder<MobEffect, MobEffect> CAFFEINATED = EFFECTS.register("caffeinated", () -> new CaffeinatedEffect()
		.addAttributeModifier(Attributes.MOVEMENT_SPEED, MonsterUltra.id("effect.caffeinated.speed"), 0.2,
			AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
		.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, MonsterUltra.id("effect.caffeinated.mining"), 0.2,
			AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
		.addAttributeModifier(Attributes.ATTACK_SPEED, MonsterUltra.id("effect.caffeinated.attack"), 0.2,
			AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	private ModEffects() {
	}
}
