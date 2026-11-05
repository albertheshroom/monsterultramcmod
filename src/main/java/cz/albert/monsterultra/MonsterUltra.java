package cz.albert.monsterultra;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonsterUltra implements ModInitializer {
	public static final String MOD_ID = "monsterultra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModEffects.register();
		ModItems.register();
		ModBlocks.register();
		ModRecipes.register();
		ModCreativeTabs.register();
		LOGGER.info("Monster Ultra loaded with {} flavors", MonsterFlavor.values().length);
	}
}
