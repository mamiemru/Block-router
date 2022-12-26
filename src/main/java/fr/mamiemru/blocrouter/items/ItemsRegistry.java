package fr.mamiemru.blocrouter.items;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricPiston;
import fr.mamiemru.blocrouter.config.BlockRouterConfig;
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
            () ->  new ItemProcessingUpgrade(BlockRouterConfig.ALL_MACHINES_PROCESSING_UPGRADE_LOW.getDefault())
    );
    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_MEDIUM = ITEMS_REGISTRY.register("item_processing_upgrade_medium",
            () ->  new ItemProcessingUpgrade(BlockRouterConfig.ALL_MACHINES_PROCESSING_UPGRADE_MEDIUM.getDefault())
    );
    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_HIGH = ITEMS_REGISTRY.register("item_processing_upgrade_high",
            () ->  new ItemProcessingUpgrade(BlockRouterConfig.ALL_MACHINES_PROCESSING_UPGRADE_HIGH.getDefault())
    );
    public static final RegistryObject<Item> ITEM_PROCESSING_UPGRADE_EXTRA = ITEMS_REGISTRY.register("item_processing_upgrade_extra",
            () ->  new ItemProcessingUpgrade(BlockRouterConfig.ALL_MACHINES_PROCESSING_UPGRADE_EXTRA.getDefault())
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
    public static final RegistryObject<Item> ITEM_PIEZOELECTRIC_FUEL = ITEMS_REGISTRY.register("item_piezoelectric_fuel",
            () -> new Item(new Item.Properties().tab(BlocRouter.RouterCreativeTab))
    );
    public static final RegistryObject<Item> ITEM_PIEZOELECTRIC_IRON_PISTON = ITEMS_REGISTRY.register("item_piezoelectric_iron_piston",
            () -> new ItemPiezoelectricPiston(BlockRouterConfig.PIEZOELECTRIC_GENERATOR_IRON_PISTON_STRENGTH.getDefault())
    );
    public static final RegistryObject<Item> ITEM_PIEZOELECTRIC_DIAMOND_PISTON = ITEMS_REGISTRY.register("item_piezoelectric_diamond_piston",
            () -> new ItemPiezoelectricPiston(BlockRouterConfig.PIEZOELECTRIC_GENERATOR_DIAMOND_PISTON_STRENGTH.getDefault())
    );
    public static final RegistryObject<Item> ITEM_PIEZOELECTRIC_NETHERITE_PISTON = ITEMS_REGISTRY.register("item_piezoelectric_netherite_piston",
            () -> new ItemPiezoelectricPiston(BlockRouterConfig.PIEZOELECTRIC_GENERATOR_NETHERITE_PISTON_STRENGTH.getDefault())
    );
    public static final RegistryObject<Item> ITEM_PIEZOELECTRIC_DIAMONDIUM_PISTON = ITEMS_REGISTRY.register("item_piezoelectric_diamondium_piston",
            () -> new ItemPiezoelectricPiston(BlockRouterConfig.PIEZOELECTRIC_GENERATOR_DIAMONDIUM_PISTON_STRENGTH.getDefault())
    );

    // public static final RegistryObject<Item> ITEM_QUANTITY_UPGRADE = ITEMS_REGISTRY.register("item_quantity_upgrade",ItemQuantityUpgrade::new);

    public static final RegistryObject<Item> ITEM_FILTER = ITEMS_REGISTRY.register("item_filter", ItemFilter::new);

    public static void register(IEventBus eventBus) {
        ITEMS_REGISTRY.register(eventBus);
    }
}