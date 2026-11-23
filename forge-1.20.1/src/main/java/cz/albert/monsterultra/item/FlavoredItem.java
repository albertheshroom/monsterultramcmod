package cz.albert.monsterultra.item;

import cz.albert.monsterultra.MonsterFlavor;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** Item named "Monster Energy (Bucket)" whose tooltip shows the flavor, e.g. "VR46 Zero Sugar". */
public class FlavoredItem extends Item {
	protected final MonsterFlavor flavor;

	public FlavoredItem(MonsterFlavor flavor, Properties properties) {
		super(properties);
		this.flavor = flavor;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(flavor.translationKey()).withStyle(ChatFormatting.GRAY));
	}
}
