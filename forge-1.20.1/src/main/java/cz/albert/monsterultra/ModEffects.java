package cz.albert.monsterultra;

import cz.albert.monsterultra.effect.CaffeinatedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEffects {
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MonsterUltra.MOD_ID);

	/** Mining speed bonus per level; 1.20.1 has no block break speed attribute, so it is applied in {@link #onBreakSpeed}. */
	private static final float MINING_BONUS = 0.2F;

	/** +20 % movement speed, attack speed and (see {@link #onBreakSpeed}) mining speed per level, like Speed I + Haste I. */
	public static final RegistryObject<MobEffect> CAFFEINATED = EFFECTS.register("caffeinated", () -> new CaffeinatedEffect()
		.addAttributeModifier(Attributes.MOVEMENT_SPEED, "5f2b7a8e-4c1d-4b6a-9e3f-1a2b3c4d5e60", 0.2,
			AttributeModifier.Operation.MULTIPLY_TOTAL)
		.addAttributeModifier(Attributes.ATTACK_SPEED, "5f2b7a8e-4c1d-4b6a-9e3f-1a2b3c4d5e61", 0.2,
			AttributeModifier.Operation.MULTIPLY_TOTAL));

	private ModEffects() {
	}

	static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
		var effect = event.getEntity().getEffect(CAFFEINATED.get());
		if (effect != null) {
			event.setNewSpeed(event.getNewSpeed() * (1.0F + MINING_BONUS * (effect.getAmplifier() + 1)));
		}
	}
}
