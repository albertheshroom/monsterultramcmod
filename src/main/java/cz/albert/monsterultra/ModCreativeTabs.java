package cz.albert.monsterultra;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {
	public static final CreativeModeTab MONSTER_ULTRA = Registry.register(
		BuiltInRegistries.CREATIVE_MODE_TAB,
		MonsterUltra.id("monster_ultra"),
		FabricCreativeModeTab.builder()
			.title(Component.translatable("itemGroup.monsterultra.monster_ultra"))
			.icon(() -> new ItemStack(ModItems.CANS.get(MonsterFlavor.ULTRA_WHITE)))
			.displayItems((parameters, output) -> {
				for (MonsterFlavor flavor : MonsterFlavor.values()) {
					output.accept(ModItems.CANS.get(flavor));
				}
				for (MonsterFlavor flavor : MonsterFlavor.values()) {
					output.accept(ModItems.BUCKETS.get(flavor));
				}
				output.accept(ModItems.EMPTY_CAN);
			})
			.build()
	);

	private ModCreativeTabs() {
	}

	public static void register() {
	}
}
