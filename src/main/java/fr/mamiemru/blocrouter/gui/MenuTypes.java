package fr.mamiemru.blocrouter.gui;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.*;
import io.netty.util.Attribute;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BlocRouter.MOD_ID);

    public static final RegistryObject<MenuType<RouterMenu>> ROUTER_MENU =
            registerMenuType(RouterMenu::new, "router_menu");
    public static final RegistryObject<MenuType<EnderRouterMenu>> ENDER_ROUTER_MENU =
            registerMenuType(EnderRouterMenu::new, "ender_router_menu");
    public static final RegistryObject<MenuType<PatternEncoderMenu>> PATTERN_ENCODER_MENU =
            registerMenuType(PatternEncoderMenu::new, "pattern_encoder_menu");
    public static final RegistryObject<MenuType<BufferMenu>> BUFFER_MENU =
            registerMenuType(BufferMenu::new, "buffer_menu");
    public static final RegistryObject<MenuType<ScatterMenu>> SCATTER_MENU =
            registerMenuType(ScatterMenu::new, "scatter_menu");
    public static final RegistryObject<MenuType<RetrieverMenu>> RETRIEVER_MENU =
            registerMenuType(RetrieverMenu::new, "retriever_menu");
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
