package fr.mamiemru.blocrouter;

import com.mojang.logging.LogUtils;
import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.events.ClientEvents;
import fr.mamiemru.blocrouter.gui.MenuTypes;
import fr.mamiemru.blocrouter.gui.screen.*;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.tabs.RouterCreativeTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BlocRouter.MOD_ID)
public class BlocRouter
{
    public static final String MOD_ID = "block_router";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CreativeModeTab RouterCreativeTab = new RouterCreativeTab();

    public BlocRouter() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemsRegistry.register(modEventBus);
        BlocksRegistry.register(modEventBus);
        EntitiesRegistry.register(modEventBus);
        MenuTypes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetworking.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            MenuScreens.register(MenuTypes.ROUTER_MENU.get(), RouterScreen::new);
            MenuScreens.register(MenuTypes.PATTERN_ENCODER_MENU.get(), PatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.BUFFER_MENU.get(), BufferScreen::new);
            MenuScreens.register(MenuTypes.SCATTER_MENU.get(), ScatterScreen::new);
            MenuScreens.register(MenuTypes.RETRIEVER_MENU.get(), RetrieverScreen::new);
            // MenuScreens.register(MenuTypes.ENDER_ROUTER_MENU.get(), EnderRouterScreen::new);
        }
    }
}
