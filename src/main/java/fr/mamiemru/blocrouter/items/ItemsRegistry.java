package fr.mamiemru.blocrouter.items;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import fr.mamiemru.blocrouter.items.custom.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BlocRouter.MOD_ID);

    public static final RegistryObject<Item> ITEM_AMETHYST_GEAR = ITEMS_REGISTRY.register("item_amethyst_gear",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab)));
    public static final RegistryObject<Item> ITEM_CONDUCTIVE_AMETHYST_SHARD = ITEMS_REGISTRY.register("item_conductive_amethyst_shard",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab)));
    public static final RegistryObject<Item> ITEM_ENDER_AMETHYST_SHARD = ITEMS_REGISTRY.register("item_ender_amethyst_shard",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab)));

    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_LOW = ITEMS_REGISTRY.register("item_processing_upgrade_low",
            () ->  new ItemProcessingUpgrade(2)
    );
    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_MEDIUM = ITEMS_REGISTRY.register("item_processing_upgrade_medium",
            () ->  new ItemProcessingUpgrade(3)
    );
    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_HIGH = ITEMS_REGISTRY.register("item_processing_upgrade_high",
            () ->  new ItemProcessingUpgrade(8)
    );
    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_EXTRA = ITEMS_REGISTRY.register("item_processing_upgrade_extra",
            () ->  new ItemProcessingUpgrade(96)
    );
    public static final RegistryObject<Item> ITEM_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_routing_pattern",
            ItemRoutingPattern::new
    );
    public static final RegistryObject<Item> ITEM_NORMAL_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_normal_routing_pattern",
            ItemNormalRoutingPattern::new
    );
    public static final RegistryObject<Item> ITEM_ENDER_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_ender_routing_pattern",
            ItemEnderRoutingPattern::new
    );
    public static final RegistryObject<Item> ITEM_DISPATCHER_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_dispatcher_routing_pattern",
            ItemDispatcherRoutingPattern::new
    );
    public static final RegistryObject<Item> ITEM_SLOT_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_slot_routing_pattern",
            ItemSlotRoutingPattern::new
    );
    public static final RegistryObject<Item> ITEM_VACUUM_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_vacuum_routing_pattern",
            ItemVacuumRoutingPattern::new
    );
    public static final RegistryObject<Item> ITEM_TRANSFER_ROUTING_PATTERN = ITEMS_REGISTRY.register("item_transfer_routing_pattern",
            ItemTransferRoutingPattern::new
    );

    public static final RegistryObject<Item> ITEM_TELEPORTATION_SLOT = ITEMS_REGISTRY.register("item_teleportation_slot",
            ItemTeleportationSlot::new
    );

    // public static final RegistryObject<Item> ITEM_QUANTITY_UPGRADE = ITEMS_REGISTRY.register("item_quantity_upgrade",ItemQuantityUpgrade::new);

    public static final RegistryObject<Item> ITEM_FILTER = ITEMS_REGISTRY.register("item_filter", ItemFilter::new);

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}