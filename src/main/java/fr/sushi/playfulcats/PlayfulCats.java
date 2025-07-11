package fr.sushi.playfulcats;

import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import fr.sushi.playfulcats.common.network.ServerboundYarnballDebugPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(PlayfulCats.MODID)
public class PlayfulCats
{
	public static final String MODID = "playfulcats";

	public PlayfulCats(IEventBus modEventBus, ModContainer modContainer)
	{
		PlayfulCatRegistries.registerRegistries(modEventBus);
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	@SubscribeEvent
	private static void onRegisterPackets(RegisterPayloadHandlersEvent event)
	{
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.commonToClient(ServerboundYarnballDebugPacket.TYPE,
								 ServerboundYarnballDebugPacket.STREAM_CODEC,
								 new ServerboundCustomPayloadPacket());
	}
}
