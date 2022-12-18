package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.custom.*;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.*;
import fr.mamiemru.blocrouter.entities.custom.routers.*;
import fr.mamiemru.blocrouter.entities.custom.scatter.EnderEnergyScatterEntity;
import fr.mamiemru.blocrouter.entities.custom.scatter.EnderScatterEntity;
import fr.mamiemru.blocrouter.entities.custom.scatter.ScatterEntity;
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
    public static final RegistryObject<BlockEntityType<TransferRouterEntity>> TRANSFER_ROUTER_ENTITY =
            BLOCK_ENTITIES.register("transfer_router_entity", () ->
                    BlockEntityType.Builder.of(TransferRouterEntity::new,
                            BlocksRegistry.BLOCK_TRANSFER_ROUTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<DispatcherRouterEntity>> DISPATCHER_ROUTER_ENTITY =
            BLOCK_ENTITIES.register("dispatcher_router_entity", () ->
                    BlockEntityType.Builder.of(DispatcherRouterEntity::new,
                            BlocksRegistry.BLOCK_DISPATCHER_ROUTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SlotRouterEntity>> SLOT_ROUTER_ENTITY =
            BLOCK_ENTITIES.register("slot_router_entity", () ->
                    BlockEntityType.Builder.of(SlotRouterEntity::new,
                            BlocksRegistry.BLOCK_SLOT_ROUTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<VacuumRouterEntity>> VACUUM_ROUTER_ENTITY =
            BLOCK_ENTITIES.register("vacuum_router_entity", () ->
                    BlockEntityType.Builder.of(VacuumRouterEntity::new,
                            BlocksRegistry.BLOCK_VACUUM_ROUTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<PatternEncoderEntity>> PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(PatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_PATTERN_ENCODER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EnderPatternEncoderEntity>> ENDER_PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("ender_pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(EnderPatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_ENDER_PATTERN_ENCODER.get()).build(null));
    public static final RegistryObject<BlockEntityType<TransferPatternEncoderEntity>> TRANSFER_PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("transfer_pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(TransferPatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_TRANSFER_PATTERN_ENCODER.get()).build(null));
    public static final RegistryObject<BlockEntityType<DispatcherPatternEncoderEntity>> DISPATCHER_PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("dispatcher_pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(DispatcherPatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_PATTERN_DISPATCHER_ENCODER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SlotPatternEncoderEntity>> SLOT_PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("slot_pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(SlotPatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_PATTERN_SLOT_ENCODER.get()).build(null));
    public static final RegistryObject<BlockEntityType<VacuumPatternEncoderEntity>> VACUUM_PATTERN_ENCODER_ENTITY =
            BLOCK_ENTITIES.register("vacuum_pattern_encoder_entity", () ->
                    BlockEntityType.Builder.of(VacuumPatternEncoderEntity::new,
                            BlocksRegistry.BLOCK_PATTERN_VACUUM_ENCODER.get()).build(null));
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
    public static final RegistryObject<BlockEntityType<EnderScatterEntity>> ENDER_SCATTER_ENTITY =
            BLOCK_ENTITIES.register("ender_scatter_entity", () ->
                    BlockEntityType.Builder.of(EnderScatterEntity::new,
                            BlocksRegistry.BLOCK_ENDER_SCATTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EnderEnergyScatterEntity>> ENDER_ENERGY_SCATTER_ENTITY =
            BLOCK_ENTITIES.register("ender_energy_scatter_entity", () ->
                    BlockEntityType.Builder.of(EnderEnergyScatterEntity::new,
                            BlocksRegistry.BLOCK_ENDER_ENERGY_SCATTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RetrieverEntity>> RETRIEVER_ENTITY =
            BLOCK_ENTITIES.register("retriever_entity", () ->
                    BlockEntityType.Builder.of(RetrieverEntity::new,
                            BlocksRegistry.BLOCK_RETRIEVER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EnderRetrieverEntity>> ENDER_RETRIEVER_ENTITY =
            BLOCK_ENTITIES.register("ender_retriever_entity", () ->
                    BlockEntityType.Builder.of(EnderRetrieverEntity::new,
                            BlocksRegistry.BLOCK_ENDER_RETRIEVER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}