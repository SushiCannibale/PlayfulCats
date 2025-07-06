package fr.sushi.playfulcats.common;

import fr.sushi.playfulcats.PlayfulCats;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PlayfulCatRegistries
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
			Registries.CREATIVE_MODE_TAB, PlayfulCats.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOYS_TAB = CREATIVE_MODE_TABS.register(
			"toys_tab",
			() -> CreativeModeTab.builder()
								 .title(Component.translatable(
										 "itemGroup.playfulcats_toys")) //The language key for the title of your CreativeModeTab
								 .withTabsBefore(
										 CreativeModeTabs.COMBAT)
								 .icon(Items.STONE::getDefaultInstance)
								 .displayItems((parameters, output) ->
								 {
									 output.accept(Items.STONE);
								 }).build());

	public static void registerCreativeTabs(IEventBus modEventBus)
	{
		CREATIVE_MODE_TABS.register(modEventBus);
	}
}
