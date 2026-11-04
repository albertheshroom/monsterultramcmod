package cz.albert.monsterultra;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * All Monster Ultra flavors. Each flavor gets a can item ({@code <id>_can})
 * and a bucket item ({@code <id>_bucket}).
 */
public enum MonsterFlavor {
	ULTRA_WHITE("ultra_white", "Ultra White"),
	ULTRA_STRAWBERRY_DREAMS("ultra_strawberry_dreams", "Ultra Strawberry Dreams"),
	ULTRA_ROSA("ultra_rosa", "Ultra Rosá"),
	ULTRA_PARADISE("ultra_paradise", "Ultra Paradise"),
	ULTRA_VIOLET("ultra_violet", "Ultra Violet"),
	ULTRA_PEACHY_KEEN("ultra_peachy_keen", "Ultra Peachy Keen"),
	ULTRA_WATERMELON("ultra_watermelon", "Ultra Watermelon"),
	ULTRA_RED("ultra_red", "Ultra Red"),
	ULTRA_BLUE("ultra_blue", "Ultra Blue"),
	ULTRA_FANTASY_RUBY_RED("ultra_fantasy_ruby_red", "Ultra Fantasy Ruby Red"),
	LANDO_NORRIS("lando_norris", "Lando Norris"),
	ULTRA_VICE_GUAVA("ultra_vice_guava", "Ultra Vice Guava"),
	ULTRA_FIESTA_MANGO("ultra_fiesta_mango", "Ultra Fiesta Mango"),
	VR46_ZERO_SUGAR("vr46_zero_sugar", "VR46 Zero Sugar"),
	ULTRA_GOLD_PINEAPPLE("ultra_gold_pineapple", "Ultra Gold Pineapple"),
	ULTRA_BLACK("ultra_black", "Ultra Black");

	/** Default effect duration in ticks (30 s). */
	private static final int DEFAULT_DURATION = 30 * 20;

	private final String id;
	private final String displayName;

	MonsterFlavor(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
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

	/**
	 * Effects applied when a can of this flavor is drunk.
	 * Placeholder for alpha: every flavor gives Speed I + Haste I for 30 s.
	 */
	public List<MobEffectInstance> effects() {
		return List.of(
			new MobEffectInstance(MobEffects.SPEED, DEFAULT_DURATION, 0),
			new MobEffectInstance(MobEffects.HASTE, DEFAULT_DURATION, 0)
		);
	}
}
