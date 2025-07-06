package fr.sushi.playfulcats;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(PlayfulCats.MODID)
public class PlayfulCats {
    public static final String MODID = "playfulcats";

    public PlayfulCats(IEventBus modEventBus, ModContainer modContainer) {
        PlayfulCatRegistries.registerCreativeTabs(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
