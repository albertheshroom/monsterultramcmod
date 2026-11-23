package cz.albert.monsterultra.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** Caffeinated: faster movement, mining and attacks (attribute modifiers added in ModEffects). */
public class CaffeinatedEffect extends MobEffect {
	public CaffeinatedEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x6F4E37);
	}
}
