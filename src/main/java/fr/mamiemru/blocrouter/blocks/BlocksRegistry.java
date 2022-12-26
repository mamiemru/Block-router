package fr.mamiemru.blocrouter.blocks;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.custom.*;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricGeneratorBase;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricHeavyPiston;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricTube;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricWeakPiston;
import fr.mamiemru.blocrouter.blocks.custom.patternEncoder.*;
import fr.mamiemru.blocrouter.blocks.custom.routers.*;
import fr.mamiemru.blocrouter.blocks.custom.scatter.EnderEnergyScatter;
import fr.mamiemru.blocrouter.blocks.custom.scatter.EnderScatter;
import fr.mamiemru.blocrouter.blocks.custom.scatter.Scatter;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlocksRegistry {

    public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, BlocRouter.MOD_ID);
    public static final RegistryObject<Block> BLOCK_ROUTER = registerBlock("block_router", Router::new);
    public static final RegistryObject<Block> BLOCK_ENDER_ROUTER = registerBlock("block_ender_router", EnderRouter::new);
    public static final RegistryObject<Block> BLOCK_TRANSFER_ROUTER = registerBlock("block_transfer_router", TransferRouter::new);
    public static final RegistryObject<Block> BLOCK_DISPATCHER_ROUTER = registerBlock("block_dispatcher_router", DispatcherRouter::new);
    public static final RegistryObject<Block> BLOCK_SLOT_ROUTER = registerBlock("block_slot_router", SlotRouter::new);
    public static final RegistryObject<Block> BLOCK_VACUUM_ROUTER = registerBlock("block_vacuum_router", VacuumRouter::new);
    public static final RegistryObject<Block> BLOCK_PATTERN_ENCODER = registerBlock("block_pattern_encoder", PatternEncoder::new);
    public static final RegistryObject<Block> BLOCK_ENDER_PATTERN_ENCODER = registerBlock("block_ender_pattern_encoder", EnderPatternEncoder::new);
    public static final RegistryObject<Block> BLOCK_TRANSFER_PATTERN_ENCODER = registerBlock("block_transfer_pattern_encoder", TransferPatternEncoder::new);
    public static final RegistryObject<Block> BLOCK_PATTERN_DISPATCHER_ENCODER = registerBlock("block_dispatcher_pattern_encoder", PatternDispatcherEncoder::new);
    public static final RegistryObject<Block> BLOCK_PATTERN_SLOT_ENCODER = registerBlock("block_slot_pattern_encoder", SlotPatternEncoder::new);
    public static final RegistryObject<Block> BLOCK_PATTERN_VACUUM_ENCODER = registerBlock("block_vacuum_pattern_encoder", VacuumPatternEncoder::new);
    public static final RegistryObject<Block> BLOCK_ITEM_BUFFER = registerBlock("block_item_buffer", Buffer::new);
    public static final RegistryObject<Block> BLOCK_SCATTER = registerBlock("block_scatter", Scatter::new);
    public static final RegistryObject<Block> BLOCK_ENDER_SCATTER = registerBlock("block_ender_scatter", EnderScatter::new);
    public static final RegistryObject<Block> BLOCK_ENDER_ENERGY_SCATTER = registerBlock("block_ender_energy_scatter", EnderEnergyScatter::new);
    public static final RegistryObject<Block> BLOCK_RETRIEVER = registerBlock("block_retriever", Retriever::new);
    public static final RegistryObject<Block> BLOCK_ENDER_RETRIEVER = registerBlock("block_ender_retriever", EnderRetriever::new);
    public static final RegistryObject<Block> BLOCK_MOB_LOOT_SORTER = registerBlock("block_mob_loot_sorter", MobLootSorter::new);
    public static final RegistryObject<Block> BLOCK_PIEZOELECTRIC_GENERATOR_BASE = registerBlock("block_piezoelectric_generator_base", PiezoelectricGeneratorBase::new);
    public static final RegistryObject<Block> BLOCK_PIEZOELECTRIC_TUBE = registerBlock("block_piezoelectric_tube", PiezoelectricTube::new);
    public static final RegistryObject<Block> BLOCK_PIEZOELECTRIC_WEAK_PISTON = registerBlock("block_piezoelectric_weak_piston", PiezoelectricWeakPiston::new);
    public static final RegistryObject<Block> BLOCK_PIEZOELECTRIC_HEAVY_PISTON = registerBlock("block_piezoelectric_heavy_piston", PiezoelectricHeavyPiston::new);
    public static final RegistryObject<Block> BLOCK_TRADING_STATION = registerBlock("block_trading_station",
            () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL))
    );

    public static final RegistryObject<Block> BLOCK_MACHINE_BLOCK = registerBlock("block_machine_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL)));
    public static final RegistryObject<Block> BLOCK_ENDER_AMETHYST = registerBlock("block_ender_amethyst",
            () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST)));

    // public static final RegistryObject<Block> BLOCK_TRADING_STATION = registerBlock("block_trading_station", TradingStation::new);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS_REGISTRY.register(name, block);
        registerBlockItem(name, toReturn, BlocRouter.RouterCreativeTab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ItemsRegistry.ITEMS_REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS_REGISTRY.register(eventBus);
    }
}
