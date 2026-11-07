package cz.albert.monsterultra;

import cz.albert.monsterultra.chemistry.ChemistryRecipes;
import cz.albert.monsterultra.item.MonsterBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MonsterUltra.MOD_ID)
public class MonsterUltra {
	public static final String MOD_ID = "monsterultra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public MonsterUltra(IEventBus modBus) {
		ModEffects.EFFECTS.register(modBus);
		ModItems.ITEMS.register(modBus);
		ModBlocks.register(modBus);
		ModRecipes.SERIALIZERS.register(modBus);
		ModCreativeTabs.TABS.register(modBus);
		NeoForge.EVENT_BUS.addListener(MonsterUltra::registerBrewing);
	}

	private static void registerBrewing(RegisterBrewingRecipesEvent event) {
		event.getBuilder().addRecipe(new MonsterBrewingRecipe());
	}
}
