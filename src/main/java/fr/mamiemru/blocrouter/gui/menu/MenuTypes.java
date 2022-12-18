package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.Container.ContainerListData;
import fr.mamiemru.blocrouter.gui.menu.menus.*;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.*;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.*;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderEnergyScatterMenu;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderScatterMenu;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.ScatterMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypes {

    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BlocRouter.MOD_ID);

    public static final RegistryObject<MenuType<RouterMenu>> ROUTER_MENU =
            registerMenuType(RouterMenu::new, "router_menu");
    public static final RegistryObject<MenuType<EnderRouterMenu>> ENDER_ROUTER_MENU =
            registerMenuType(EnderRouterMenu::new, "ender_router_menu");
    public static final RegistryObject<MenuType<TransferRouterMenu>> TRANSFER_ROUTER_MENU =
            registerMenuType(TransferRouterMenu::new, "transfer_router_menu");
    public static final RegistryObject<MenuType<DispatcherRouterMenu>> DISPATCHER_ROUTER_MENU =
            registerMenuType(DispatcherRouterMenu::new, "dispatcher_router_menu");
    public static final RegistryObject<MenuType<SlotRouterMenu>> SLOT_ROUTER_MENU =
            registerMenuType(SlotRouterMenu::new, "slot_router_menu");
    public static final RegistryObject<MenuType<VacuumRouterMenu>> VACUUM_ROUTER_MENU =
            registerMenuType(VacuumRouterMenu::new, "vacuum_router_menu");
    public static final RegistryObject<MenuType<PatternEncoderMenu>> PATTERN_ENCODER_MENU =
            registerMenuType(PatternEncoderMenu::new, "pattern_encoder_menu");
    public static final RegistryObject<MenuType<EnderPatternEncoderMenu>> ENDER_PATTERN_ENCODER_MENU =
            registerMenuType(EnderPatternEncoderMenu::new, "ender_pattern_encoder_menu");
    public static final RegistryObject<MenuType<TransferPatternEncoderMenu>> TRANSFER_PATTERN_ENCODER_MENU =
            registerMenuType(TransferPatternEncoderMenu::new, "transfer_pattern_encoder_menu");
    public static final RegistryObject<MenuType<DispatcherPatternEncoderMenu>> DISPATCHER_PATTERN_ENCODER_MENU =
            registerMenuType(DispatcherPatternEncoderMenu::new, "dispatcher_pattern_encoder_menu");
    public static final RegistryObject<MenuType<SlotPatternEncoderMenu>> SLOT_PATTERN_ENCODER_MENU =
            registerMenuType(SlotPatternEncoderMenu::new, "slot_pattern_encoder_menu");
    public static final RegistryObject<MenuType<VacuumPatternEncoderMenu>> VACUUM_PATTERN_ENCODER_MENU =
            registerMenuType(VacuumPatternEncoderMenu::new, "vacuum_pattern_encoder_menu");
    public static final RegistryObject<MenuType<BufferMenu>> BUFFER_MENU =
            registerMenuType(BufferMenu::new, "buffer_menu");
    public static final RegistryObject<MenuType<ScatterMenu>> SCATTER_MENU =
            registerMenuType(ScatterMenu::new, "scatter_menu");
    public static final RegistryObject<MenuType<RetrieverMenu>> RETRIEVER_MENU =
            registerMenuType(RetrieverMenu::new, "retriever_menu");
    public static final RegistryObject<MenuType<EnderScatterMenu>> ENDER_SCATTER_MENU =
            registerMenuType(EnderScatterMenu::new, "ender_scatter_menu");
    public static final RegistryObject<MenuType<EnderRetrieverMenu>> ENDER_RETRIEVER_MENU =
            registerMenuType(EnderRetrieverMenu::new, "ender_retriever_menu");
    public static final RegistryObject<MenuType<EnderEnergyScatterMenu>> ENDER_ENERGY_SCATTER_MENU =
            registerMenuType(EnderEnergyScatterMenu::new, "ender_energy_scatter_menu");
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
