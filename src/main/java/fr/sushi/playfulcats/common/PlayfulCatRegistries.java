package fr.sushi.playfulcats.common;

import fr.sushi.playfulcats.PlayfulCats;
import fr.sushi.playfulcats.common.entity.ThrownYarnBall;
import fr.sushi.playfulcats.common.item.YarnBallItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PlayfulCatRegistries
{
	public static class Items
	{
		public static final DeferredRegister.Items ITEMS =
				DeferredRegister.createItems(PlayfulCats.MODID);
		public static final DeferredItem<YarnBallItem> YARN_BALL_ITEM =
				ITEMS.registerItem("yarn_ball",
								   (properties) -> new YarnBallItem(
										   properties.stacksTo(1)));
	}

	public static class Tabs
	{
		public static final DeferredRegister<CreativeModeTab>
				CREATIVE_MODE_TABS =
				DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
										PlayfulCats.MODID);
		public static final Supplier<CreativeModeTab> TOYS_TAB =
				CREATIVE_MODE_TABS.register("toys_tab",
											() -> CreativeModeTab.builder()
																 .title(Component.translatable(
																		 "itemGroup." +
																		 PlayfulCats.MODID +
																		 ".toys"))
																 .icon(() -> Items.YARN_BALL_ITEM
																		 .value()
																		 .getDefaultInstance())
																 .displayItems(
																		 (parameters, output) ->
																		 {
																			 output.accept(
																					 Items.YARN_BALL_ITEM.get());
																		 })
																 .build());
	}

	public static class EntityTypes
	{
		public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
				DeferredRegister.createEntities(PlayfulCats.MODID);
		public static final Supplier<EntityType<ThrownYarnBall>>
				THROWN_YARN_BALL_TYPE =
				ENTITY_TYPES.register("thrown_yarn_ball",
									  () -> EntityType.Builder
											  .<ThrownYarnBall>of(
													  ThrownYarnBall::new,
													  MobCategory.MISC)
											  .sized(0.5F, 0.5F)
											  .eyeHeight(0.13F)
											  .updateInterval(20)
											  .build(ResourceKey.create(
													  Registries.ENTITY_TYPE,
													  ResourceLocation.fromNamespaceAndPath(
															  PlayfulCats.MODID,
															  "thrown_yarn_ball"))));
	}

	public static void registerRegistries(IEventBus modEventBus)
	{
		Items.ITEMS.register(modEventBus);
		Tabs.CREATIVE_MODE_TABS.register(modEventBus);
		EntityTypes.ENTITY_TYPES.register(modEventBus);
	}
}
