package cz.albert.monsterultra;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * All Monster Ultra flavors. Each flavor gets a can item ({@code <id>_can})
 * and a bucket item ({@code <id>_bucket}).
 */
public enum MonsterFlavor {
	ULTRA_WHITE("ultra_white", "Ultra White", effect(MobEffects.STRENGTH, 0)),
	ULTRA_STRAWBERRY_DREAMS("ultra_strawberry_dreams", "Ultra Strawberry Dreams", effect(MobEffects.REGENERATION, 0)),
	ULTRA_ROSA("ultra_rosa", "Ultra Rosá", effect(MobEffects.HEALTH_BOOST, 0)),
	ULTRA_PARADISE("ultra_paradise", "Ultra Paradise", effect(MobEffects.NIGHT_VISION, 0)),
	ULTRA_VIOLET("ultra_violet", "Ultra Violet"),
	ULTRA_PEACHY_KEEN("ultra_peachy_keen", "Ultra Peachy Keen"),
	ULTRA_WATERMELON("ultra_watermelon", "Ultra Watermelon"),
	ULTRA_RED("ultra_red", "Ultra Red"),
	ULTRA_BLUE("ultra_blue", "Ultra Blue", effect(MobEffects.DOLPHINS_GRACE, 0)),
	ULTRA_FANTASY_RUBY_RED("ultra_fantasy_ruby_red", "Ultra Fantasy Ruby Red", effect(MobEffects.FIRE_RESISTANCE, 0)),
	LANDO_NORRIS("lando_norris", "Lando Norris",
		effect(MobEffects.SPEED, 1), new MobEffectInstance(MobEffects.NAUSEA, 10 * 20, 0)),
	ULTRA_VICE_GUAVA("ultra_vice_guava", "Ultra Vice Guava"),
	ULTRA_FIESTA_MANGO("ultra_fiesta_mango", "Ultra Fiesta Mango"),
	/** Also removes all negative effects, see {@link #clearsHarmfulEffects()}. */
	VR46_ZERO_SUGAR("vr46_zero_sugar", "VR46 Zero Sugar", effect(MobEffects.SPEED, 1)),
	ULTRA_GOLD_PINEAPPLE("ultra_gold_pineapple", "Ultra Gold Pineapple", effect(MobEffects.ABSORPTION, 0)),
	ULTRA_BLACK("ultra_black", "Ultra Black", effect(MobEffects.RESISTANCE, 0));

	/** Effect duration in ticks (60 s). */
	public static final int DURATION = 60 * 20;

	private final String id;
	private final String displayName;
	private final List<MobEffectInstance> ownEffects;

	MonsterFlavor(String id, String displayName, MobEffectInstance... ownEffects) {
		this.id = id;
		this.displayName = displayName;
		this.ownEffects = List.of(ownEffects);
	}

	private static MobEffectInstance effect(Holder<MobEffect> effect, int amplifier) {
		return new MobEffectInstance(effect, DURATION, amplifier);
	}

	public String id() {
		return id;
	}

	public String displayName() {
		return displayName;
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
		effects.add(new MobEffectInstance(ModEffects.CAFFEINATED, DURATION, 0));
		effects.addAll(ownEffects);
		return effects;
	}

	/** VR46 Zero Sugar removes all negative effects when drunk. */
	public boolean clearsHarmfulEffects() {
		return this == VR46_ZERO_SUGAR;
	}
}
