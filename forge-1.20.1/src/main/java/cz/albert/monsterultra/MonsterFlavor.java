package cz.albert.monsterultra;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * All Monster Ultra flavors. Each flavor gets a can item ({@code <id>_can})
 * and a bucket item ({@code <id>_bucket}).
 */
public enum MonsterFlavor {
	ULTRA_WHITE("ultra_white", effect(MobEffects.DAMAGE_BOOST, 0)),
	ULTRA_STRAWBERRY_DREAMS("ultra_strawberry_dreams", effect(MobEffects.REGENERATION, 0)),
	ULTRA_ROSA("ultra_rosa", effect(MobEffects.HEALTH_BOOST, 0)),
	ULTRA_PARADISE("ultra_paradise", effect(MobEffects.NIGHT_VISION, 0)),
	ULTRA_VIOLET("ultra_violet"),
	ULTRA_PEACHY_KEEN("ultra_peachy_keen"),
	ULTRA_WATERMELON("ultra_watermelon"),
	ULTRA_RED("ultra_red"),
	ULTRA_BLUE("ultra_blue", effect(MobEffects.DOLPHINS_GRACE, 0)),
	ULTRA_FANTASY_RUBY_RED("ultra_fantasy_ruby_red", effect(MobEffects.FIRE_RESISTANCE, 0)),
	LANDO_NORRIS("lando_norris", effect(MobEffects.MOVEMENT_SPEED, 1), () -> new MobEffectInstance(MobEffects.CONFUSION, 10 * 20, 0)),
	ULTRA_VICE_GUAVA("ultra_vice_guava"),
	ULTRA_FIESTA_MANGO("ultra_fiesta_mango"),
	/** Also removes all negative effects, see {@link #clearsHarmfulEffects()}. */
	VR46_ZERO_SUGAR("vr46_zero_sugar", effect(MobEffects.MOVEMENT_SPEED, 1)),
	ULTRA_GOLD_PINEAPPLE("ultra_gold_pineapple", effect(MobEffects.ABSORPTION, 0)),
	ULTRA_BLACK("ultra_black", effect(MobEffects.DAMAGE_RESISTANCE, 0));

	/** Effect duration in ticks (60 s). */
	public static final int DURATION = 60 * 20;

	private final String id;
	private final List<Supplier<MobEffectInstance>> ownEffects;

	@SafeVarargs
	MonsterFlavor(String id, Supplier<MobEffectInstance>... ownEffects) {
		this.id = id;
		this.ownEffects = List.of(ownEffects);
	}

	private static Supplier<MobEffectInstance> effect(MobEffect effect, int amplifier) {
		return () -> new MobEffectInstance(effect, DURATION, amplifier);
	}

	public String id() {
		return id;
	}

	/** Translation key of the flavor name shown in the tooltip. */
	public String translationKey() {
		return "flavor.monsterultra." + id;
	}

	public String canId() {
		return id + "_can";
	}

	public String bucketId() {
		return id + "_bucket";
	}

	/** Effects applied when a can is drunk: Caffeinated (every flavor) plus the flavor's own effects. */
	public List<MobEffectInstance> effects() {
		List<MobEffectInstance> effects = new ArrayList<>();
		effects.add(new MobEffectInstance(ModEffects.CAFFEINATED.get(), DURATION, 0));
		for (Supplier<MobEffectInstance> effect : ownEffects) {
			effects.add(effect.get());
		}
		return effects;
	}

	/** VR46 Zero Sugar removes all negative effects when drunk. */
	public boolean clearsHarmfulEffects() {
		return this == VR46_ZERO_SUGAR;
	}
}
