package cz.albert.monsterultra.client;

import cz.albert.monsterultra.ModBlocks;
import cz.albert.monsterultra.MonsterUltra;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = MonsterUltra.MOD_ID, value = Dist.CLIENT)
public final class MonsterUltraClient {
	private MonsterUltraClient() {
	}

	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(ModBlocks.CHEMISTRY_TABLE_MENU.get(), ChemistryTableScreen::new);
	}
}
