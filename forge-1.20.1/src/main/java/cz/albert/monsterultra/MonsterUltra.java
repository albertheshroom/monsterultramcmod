package cz.albert.monsterultra;

import cz.albert.monsterultra.item.MonsterBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MonsterUltra.MOD_ID)
public class MonsterUltra {
	public static final String MOD_ID = "monsterultra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@SuppressWarnings("removal")
	public MonsterUltra() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModEffects.EFFECTS.register(modBus);
		ModItems.ITEMS.register(modBus);
		ModBlocks.register(modBus);
		ModRecipes.SERIALIZERS.register(modBus);
		ModCreativeTabs.TABS.register(modBus);
		modBus.addListener(MonsterUltra::commonSetup);
		MinecraftForge.EVENT_BUS.addListener(ModEffects::onBreakSpeed);
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new MonsterBrewingRecipe()));
	}
}
