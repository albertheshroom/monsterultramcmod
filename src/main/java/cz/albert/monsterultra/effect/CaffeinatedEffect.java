package cz.albert.monsterultra.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** Caffeinated: faster movement, mining and attacks. Attribute modifiers are added in {@link cz.albert.monsterultra.ModEffects}. */
public class CaffeinatedEffect extends MobEffect {
	public CaffeinatedEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x6F4E37);
	}
}
