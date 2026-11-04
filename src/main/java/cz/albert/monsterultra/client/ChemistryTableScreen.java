package cz.albert.monsterultra.client;

import cz.albert.monsterultra.MonsterEssence;
import cz.albert.monsterultra.MonsterUltra;
import cz.albert.monsterultra.chemistry.ChemistryRecipes;
import cz.albert.monsterultra.chemistry.ChemistryTableBlockEntity;
import cz.albert.monsterultra.chemistry.ChemistryTableMenu;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ChemistryTableScreen extends AbstractContainerScreen<ChemistryTableMenu> {
	private static final Identifier BACKGROUND = MonsterUltra.id("textures/gui/container/chemistry_table.png");
	private static final Identifier FUEL_LENGTH_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/fuel_length");
	private static final Identifier BREW_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/brew_progress");
	private static final Identifier BUBBLES_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/bubbles");
	private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe");
	private static final Identifier BUTTON_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_selected");
	private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe_highlighted");
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
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int xo = this.leftPos;
		int yo = this.topPos;
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		int fuelLength = Mth.clamp(Mth.positiveCeilDiv(18 * this.menu.getFuel(), ChemistryTableBlockEntity.MAX_FUEL), 0, 18);
		if (fuelLength > 0) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FUEL_LENGTH_SPRITE, 18, 4, 0, 0, xo + 60, yo + 44, fuelLength, 4);
		}

		int brewTime = this.menu.getBrewTime();
		if (brewTime > 0) {
			int length = (int) (28.0F * (1.0F - (float) brewTime / ChemistryTableBlockEntity.BREW_TIME));
			if (length > 0) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREW_PROGRESS_SPRITE, 9, 28, 0, 0, xo + 97, yo + 16, 9, length);
			}
			length = BUBBLE_LENGTHS[brewTime / 2 % 7];
			if (length > 0) {
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BUBBLES_SPRITE, 12, 29, 0, 29 - length, xo + 63, yo + 14 + 29 - length, 12, length);
			}
		}

		List<MonsterEssence> options = this.menu.essenceOptions();
		for (int i = 0; i < options.size(); i++) {
			int x = xo + BUTTON_X + i * BUTTON_SPACING;
			int y = yo + BUTTON_Y;
			Identifier sprite = i == this.menu.getSelected() ? BUTTON_SELECTED_SPRITE
				: isOver(mouseX, mouseY, x, y) ? BUTTON_HIGHLIGHTED_SPRITE : BUTTON_SPRITE;
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y - 1, 16, 18);
			graphics.item(new ItemStack(ChemistryRecipes.essenceItem(options.get(i))), x, y);
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
		super.extractLabels(graphics, xm, ym);
		if (!this.menu.hasWater()) {
			graphics.text(this.font, NEEDS_WATER, this.imageWidth - 8 - this.font.width(NEEDS_WATER), 40, 0xFFD03030, false);
		}
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		super.extractTooltip(graphics, mouseX, mouseY);
		List<MonsterEssence> options = this.menu.essenceOptions();
		for (int i = 0; i < options.size(); i++) {
			if (isOver(mouseX, mouseY, this.leftPos + BUTTON_X + i * BUTTON_SPACING, this.topPos + BUTTON_Y)) {
				graphics.setTooltipForNextFrame(this.font, new ItemStack(ChemistryRecipes.essenceItem(options.get(i))), mouseX, mouseY);
			}
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		List<MonsterEssence> options = this.menu.essenceOptions();
		for (int i = 0; i < options.size(); i++) {
			if (isOver(event.x(), event.y(), this.leftPos + BUTTON_X + i * BUTTON_SPACING, this.topPos + BUTTON_Y)
				&& this.menu.clickMenuButton(this.minecraft.player, i)) {
				this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i);
				return true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	private static boolean isOver(double mouseX, double mouseY, int x, int y) {
		return mouseX >= x && mouseY >= y - 1 && mouseX < x + 16 && mouseY < y + 17;
	}
}
