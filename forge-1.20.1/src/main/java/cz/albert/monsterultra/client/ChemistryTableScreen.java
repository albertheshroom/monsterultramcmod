package cz.albert.monsterultra.client;

import cz.albert.monsterultra.MonsterEssence;
import cz.albert.monsterultra.MonsterUltra;
import cz.albert.monsterultra.chemistry.ChemistryRecipes;
import cz.albert.monsterultra.chemistry.ChemistryTableBlockEntity;
import cz.albert.monsterultra.chemistry.ChemistryTableMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * 1.20.1 has no GUI sprite atlas: the progress arrow, bubbles and fuel bar are drawn from the
 * chemistry table texture (same layout as the vanilla 1.20.1 brewing stand texture) and the
 * essence buttons from the vanilla stonecutter texture.
 */
public class ChemistryTableScreen extends AbstractContainerScreen<ChemistryTableMenu> {
	private static final ResourceLocation BACKGROUND = MonsterUltra.id("textures/gui/container/chemistry_table.png");
	private static final ResourceLocation STONECUTTER = new ResourceLocation("textures/gui/container/stonecutter.png");
	private static final int[] BUBBLE_LENGTHS = {29, 24, 20, 16, 11, 6, 0};
	private static final int BUTTON_X = 126;
	private static final int BUTTON_Y = 16;
	private static final int BUTTON_SPACING = 20;
	private static final Component NEEDS_WATER = Component.translatable("container.monsterultra.chemistry_table.needs_water");

	public ChemistryTableScreen(ChemistryTableMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		this.renderTooltip(graphics, mouseX, mouseY);
		List<MonsterEssence> options = this.menu.essenceOptions();
		for (int i = 0; i < options.size(); i++) {
			if (isOver(mouseX, mouseY, this.leftPos + BUTTON_X + i * BUTTON_SPACING, this.topPos + BUTTON_Y)) {
				graphics.renderTooltip(this.font, new ItemStack(ChemistryRecipes.essenceItem(options.get(i))), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		int xo = this.leftPos;
		int yo = this.topPos;
		graphics.blit(BACKGROUND, xo, yo, 0, 0, this.imageWidth, this.imageHeight);

		int fuelLength = Mth.clamp((18 * this.menu.getFuel() + ChemistryTableBlockEntity.MAX_FUEL - 1) / ChemistryTableBlockEntity.MAX_FUEL, 0, 18);
		if (fuelLength > 0) {
			graphics.blit(BACKGROUND, xo + 60, yo + 44, 176, 29, fuelLength, 4);
		}

		int brewTime = this.menu.getBrewTime();
		if (brewTime > 0) {
			int length = (int) (28.0F * (1.0F - (float) brewTime / ChemistryTableBlockEntity.BREW_TIME));
			if (length > 0) {
				graphics.blit(BACKGROUND, xo + 97, yo + 16, 176, 0, 9, length);
			}
			length = BUBBLE_LENGTHS[brewTime / 2 % 7];
			if (length > 0) {
				graphics.blit(BACKGROUND, xo + 63, yo + 14 + 29 - length, 185, 29 - length, 12, length);
			}
		}

		List<MonsterEssence> options = this.menu.essenceOptions();
		for (int i = 0; i < options.size(); i++) {
			int x = xo + BUTTON_X + i * BUTTON_SPACING;
			int y = yo + BUTTON_Y;
			int v = i == this.menu.getSelected() ? 184 : isOver(mouseX, mouseY, x, y) ? 202 : 166;
			graphics.blit(STONECUTTER, x, y - 1, 0, v, 16, 18);
			graphics.renderItem(new ItemStack(ChemistryRecipes.essenceItem(options.get(i))), x, y);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderLabels(graphics, mouseX, mouseY);
		if (!this.menu.hasWater()) {
			graphics.drawString(this.font, NEEDS_WATER, this.imageWidth - 8 - this.font.width(NEEDS_WATER), 40, 0xD03030, false);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		List<MonsterEssence> options = this.menu.essenceOptions();
		for (int i = 0; i < options.size(); i++) {
			if (isOver(mouseX, mouseY, this.leftPos + BUTTON_X + i * BUTTON_SPACING, this.topPos + BUTTON_Y)
				&& this.menu.clickMenuButton(this.minecraft.player, i)) {
				this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	private static boolean isOver(double mouseX, double mouseY, int x, int y) {
		return mouseX >= x && mouseY >= y - 1 && mouseX < x + 16 && mouseY < y + 17;
	}
}
