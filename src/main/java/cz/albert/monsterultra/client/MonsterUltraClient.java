package cz.albert.monsterultra.client;

import cz.albert.monsterultra.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class MonsterUltraClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModBlocks.CHEMISTRY_TABLE_MENU, ChemistryTableScreen::new);
	}
}
