package fr.mamiemru.blocrouter.items;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.RouterEntity;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemQuantityUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegistry {
    public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BlocRouter.MOD_ID);

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
    public static final RegistryObject<Item> ITEM_TELEPORTATION_SLOT = ITEMS_REGISTRY.register("item_teleportation_slot",
            ItemTeleportationSlot::new
    );
    public static final RegistryObject<Item> ITEM_QUANTITY_UPGRADE = ITEMS_REGISTRY.register("item_quantity_upgrade",
            ItemQuantityUpgrade::new
    );

    public static final RegistryObject<Item> ITEM_AMETHYST_GEAR = ITEMS_REGISTRY.register("item_amethyst_gear",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab)));
    public static final RegistryObject<Item> ITEM_CONDUCTIVE_AMETHYST_SHARD = ITEMS_REGISTRY.register("item_conductive_amethyst_shard",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab)));
    public static final RegistryObject<Item> ITEM_ENDER_AMETHYST_SHARD = ITEMS_REGISTRY.register("item_ender_amethyst_shard",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab)));

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}