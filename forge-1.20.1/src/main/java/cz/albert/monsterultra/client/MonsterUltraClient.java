package cz.albert.monsterultra.client;

import cz.albert.monsterultra.ModBlocks;
import cz.albert.monsterultra.MonsterUltra;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MonsterUltra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MonsterUltraClient {
	private MonsterUltraClient() {
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(ModBlocks.CHEMISTRY_TABLE_MENU.get(), ChemistryTableScreen::new));
	}
}
