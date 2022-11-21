package fr.mamiemru.blocrouter.blocks;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.custom.*;
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
    public static final RegistryObject<Block> BLOCK_PATTERN_ENCODER = registerBlock("block_pattern_encoder", PatternEncoder::new);
    public static final RegistryObject<Block> BLOCK_TRADING_STATION = registerBlock("block_trading_station", TradingStation::new);
    public static final RegistryObject<Block> BLOCK_ITEM_BUFFER = registerBlock("block_item_buffer", Buffer::new);
    public static final RegistryObject<Block> BLOCK_SCATTER = registerBlock("block_scatter", Scatter::new);
    public static final RegistryObject<Block> BLOCK_RETRIEVER = registerBlock("block_retriever", Retriever::new);

    public static final RegistryObject<Block> BLOCK_MACHINE_BLOCK = registerBlock("block_machine_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.HEAVY_METAL)));
    public static final RegistryObject<Block> BLOCK_ENDER_AMETHYST = registerBlock("block_ender_amethyst",
            () -> new Block(BlockBehaviour.Properties.of(Material.AMETHYST)));

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
