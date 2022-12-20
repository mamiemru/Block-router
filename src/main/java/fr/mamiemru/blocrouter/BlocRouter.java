package fr.mamiemru.blocrouter;

import com.mojang.logging.LogUtils;
import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.screen.screens.*;
import fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder.*;
import fr.mamiemru.blocrouter.gui.screen.screens.routers.*;
import fr.mamiemru.blocrouter.gui.screen.screens.scatter.EnderEnergyScatterScreen;
import fr.mamiemru.blocrouter.gui.screen.screens.scatter.EnderScatterScreen;
import fr.mamiemru.blocrouter.gui.screen.screens.scatter.ScatterScreen;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.tabs.RouterCreativeTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.awt.*;

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
            MenuScreens.register(MenuTypes.ENDER_ROUTER_MENU.get(), EnderRouterScreen::new);
            MenuScreens.register(MenuTypes.TRANSFER_ROUTER_MENU.get(), TransferRouterScreen::new);
            MenuScreens.register(MenuTypes.DISPATCHER_ROUTER_MENU.get(), DispatcherRouterScreen::new);
            MenuScreens.register(MenuTypes.SLOT_ROUTER_MENU.get(), SlotRouterScreen::new);
            MenuScreens.register(MenuTypes.VACUUM_ROUTER_MENU.get(), VacuumRouterScreen::new);
            MenuScreens.register(MenuTypes.PATTERN_ENCODER_MENU.get(), PatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.ENDER_PATTERN_ENCODER_MENU.get(), EnderPatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.TRANSFER_PATTERN_ENCODER_MENU.get(), TransferPatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.DISPATCHER_PATTERN_ENCODER_MENU.get(), DispatcherPatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.SLOT_PATTERN_ENCODER_MENU.get(), SlotPatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.VACUUM_PATTERN_ENCODER_MENU.get(), VacuumPatternEncoderScreen::new);
            MenuScreens.register(MenuTypes.BUFFER_MENU.get(), BufferScreen::new);
            MenuScreens.register(MenuTypes.SCATTER_MENU.get(), ScatterScreen::new);
            MenuScreens.register(MenuTypes.RETRIEVER_MENU.get(), RetrieverScreen::new);
            MenuScreens.register(MenuTypes.ENDER_SCATTER_MENU.get(), EnderScatterScreen::new);
            MenuScreens.register(MenuTypes.ENDER_RETRIEVER_MENU.get(), EnderRetrieverScreen::new);
            MenuScreens.register(MenuTypes.ENDER_ENERGY_SCATTER_MENU.get(), EnderEnergyScatterScreen::new);
            MenuScreens.register(MenuTypes.ITEM_FILTER_MENU.get(), ItemFilterScreen::new);
        }
    }
}
