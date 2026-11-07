package cz.albert.monsterultra;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MonsterUltra.MOD_ID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MONSTER_ULTRA = TABS.register("monster_ultra", () -> CreativeModeTab.builder()
		.title(Component.translatable("itemGroup.monsterultra.monster_ultra"))
		.icon(() -> new ItemStack(ModItems.CANS.get(MonsterFlavor.ULTRA_WHITE).get()))
		.displayItems((parameters, output) -> {
			for (MonsterFlavor flavor : MonsterFlavor.values()) {
				output.accept(ModItems.CANS.get(flavor).get());
			}
			for (MonsterFlavor flavor : MonsterFlavor.values()) {
				output.accept(ModItems.BUCKETS.get(flavor).get());
			}
			output.accept(ModItems.EMPTY_CAN.get());
			for (MonsterEssence essence : MonsterEssence.values()) {
				output.accept(ModItems.ESSENCES.get(essence).get());
			}
			output.accept(ModItems.SUCRALOSE.get());
			output.accept(ModItems.EMPTY_VIAL.get());
			output.accept(ModItems.SUCRALOSE_SOLUTION.get());
			output.accept(ModBlocks.CHEMISTRY_TABLE_ITEM.get());
		})
		.build());

	private ModCreativeTabs() {
	}
}
