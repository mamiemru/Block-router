package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.custom.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntitiesRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BlocRouter.MOD_ID);

    public static final RegistryObject<BlockEntityType<RouterEntity>> ROUTER_ENTITY =
            BLOCK_ENTITIES.register("router_entity", () ->
                    BlockEntityType.Builder.of(RouterEntity::new,
                            BlocksRegistry.BLOCK_ROUTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<EnderRouterEntity>> ENDER_ROUTER_ENTITY =
            BLOCK_ENTITIES.register("ender_router_entity", () ->
                    BlockEntityType.Builder.of(EnderRouterEntity::new,
                            BlocksRegistry.BLOCK_ENDER_ROUTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<PatternEncoderEntity>> PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(PatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_PATTERN_ENCODER.get()).build(null));

    public static final RegistryObject<BlockEntityType<TradingStationEntity>> TRADING_STATION_ENTITY =
            BLOCK_ENTITIES.register("trading_station_entity", () ->
                    BlockEntityType.Builder.of(TradingStationEntity::new,
                            BlocksRegistry.BLOCK_TRADING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<BufferEntity>> BUFFER_ENTITY =
            BLOCK_ENTITIES.register("item_buffer_entity", () ->
                    BlockEntityType.Builder.of(BufferEntity::new,
                            BlocksRegistry.BLOCK_ITEM_BUFFER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ScatterEntity>> SCATTER_ENTITY =
            BLOCK_ENTITIES.register("scatter_entity", () ->
                    BlockEntityType.Builder.of(ScatterEntity::new,
                            BlocksRegistry.BLOCK_SCATTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<RetrieverEntity>> RETRIEVER_ENTITY =
            BLOCK_ENTITIES.register("retriever_entity", () ->
                    BlockEntityType.Builder.of(RetrieverEntity::new,
                            BlocksRegistry.BLOCK_RETRIEVER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}