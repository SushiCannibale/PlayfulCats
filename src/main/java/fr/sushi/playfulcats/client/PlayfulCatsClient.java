package fr.sushi.playfulcats.client;

import com.mojang.logging.LogUtils;
import fr.sushi.playfulcats.PlayfulCats;
import fr.sushi.playfulcats.client.models.ThrownYarnBallModel;
import fr.sushi.playfulcats.client.renderer.ThrownYarnBallRenderer;
import fr.sushi.playfulcats.common.PlayfulCatRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(value = PlayfulCats.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PlayfulCats.MODID, value = Dist.CLIENT)
public class PlayfulCatsClient {
    public static final Logger LOGGER = LogUtils.getLogger();

    public PlayfulCatsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PlayfulCatRegistries.EntityTypes.THROWN_YARN_BALL_TYPE.get(),
                ThrownYarnBallRenderer::new);
    }

    @SubscribeEvent
    private static void onCreateLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ThrownYarnBallModel.MODEL_LAYER_LOCATION, ThrownYarnBallModel::createBodyLayer);
    }
}
