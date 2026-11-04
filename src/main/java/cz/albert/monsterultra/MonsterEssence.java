package cz.albert.monsterultra;

/** Flavor essences (ingredients for Monster). Item id: {@code <id>_essence}. */
public enum MonsterEssence {
	BLUE_RASPBERRY("blue_raspberry"),
	CHERRY("cherry"),
	CITRUS("citrus"),
	COLDREX("coldrex"),
	DREAMBERRY("dreamberry"),
	GRAPE("grape"),
	GRAPEFRUIT("grapefruit"),
	GUAVA("guava"),
	KIWI("kiwi"),
	MANGO("mango"),
	PEACH("peach"),
	PINEAPPLE("pineapple"),
	REDBERRY("redberry"),
	ROSA("rosa"),
	WATERMELON("watermelon"),
	YUZU("yuzu");

	private final String id;

	MonsterEssence(String id) {
		this.id = id;
	}

	public String itemId() {
		return id + "_essence";
	}
}
