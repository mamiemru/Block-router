package fr.mamiemru.blocrouter.blocks.custom.generators;

import fr.mamiemru.blocrouter.blocks.BaseSwitchableFacingBlock;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.ContentLevel;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.PistonTypes;
import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricGeneratorBaseEntity;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricHeavyPistonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PiezoelectricHeavyPiston extends PiezoelectricPiston {

    public static final int MAX_TUBE_DEPTH = BlockRouterConfig.PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_MAX_TUBE_DEPTH.getDefault();
    public static final EnumProperty PISTON_TYPE = EnumProperty.create("piston_type", PistonTypes.class);

    public PiezoelectricHeavyPiston() {
        super();
        this.registerDefaultState(this.stateDefinition.any().setValue(PISTON_TYPE, PistonTypes.EMPTY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(PISTON_TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(PISTON_TYPE, PistonTypes.EMPTY);
    }

    protected void onPowerState(Level level, BlockPos pos, BlockState blockState) {
        boolean flag = level.hasNeighborSignal(pos);
        boolean isEnabled = blockState.getValue(BaseSwitchableFacingBlock.ENABLED).booleanValue();

        if (flag && isEnabled) {
            level.setBlock(pos, blockState.setValue(EXTENDED, true), 4);

            int tubesDepth = 1;
            BlockPos below = pos.below();
            BlockPos below2 = below.below();

            while (tubesDepth < MAX_TUBE_DEPTH && level.getBlockState(below2).getBlock() instanceof PiezoelectricTube) {
                below2 = below2.below();
                ++tubesDepth;
            }

            BlockState tubeBlock = level.getBlockState(below);
            BlockState generatorBlock = level.getBlockState(below2);

            boolean isBelowTubeBlock = tubeBlock.getBlock() instanceof PiezoelectricTube;
            boolean isBelow2Generator = generatorBlock.getBlock() instanceof PiezoelectricGeneratorBase;

            if (tubesDepth < MAX_TUBE_DEPTH && isBelowTubeBlock && isBelow2Generator && level.getBlockEntity(below2) instanceof PiezoelectricGeneratorBaseEntity pegbe) {
                Random rand = new Random();
                PiezoelectricTube piezoelectricTube = ((PiezoelectricTube) tubeBlock.getBlock());
                int b = BlockRouterConfig.PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_BREAK_CRYSTAL_RANDOM_BOUND.get();
                int compressionPower = getCompressionPower(level, pos);
                int energyPerShard = getEnergyGeneratedPerShard();
                int numberOfShards = getNumberOfShards(level, pos, tubesDepth);
                boolean jobDone = pegbe.generateEnergy(compressionPower, energyPerShard, tubesDepth, numberOfShards);
                if (jobDone && rand.nextInt(0, b) == 0) {
                    piezoelectricTube.decrementContentLevel(level, below);
                }
            }
        }

        level.setBlock(pos, blockState.setValue(EXTENDED, false), 4);
    }

    public int getNumberOfShards(Level level, BlockPos pistonPos, int tubeDepth) {
        int numberOfShards = 0;
        while (tubeDepth > 0) {
            pistonPos = pistonPos.below();
            if (level.getBlockState(pistonPos).getBlock() instanceof PiezoelectricTube) {
                numberOfShards += ContentLevel.toIndex(PiezoelectricTube.getContentLevel(level, pistonPos));
            } else break;
            --tubeDepth;
        }
        return numberOfShards;
    }

    @Override
    public int getEnergyGeneratedPerShard() {
        return BlockRouterConfig.PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_GENERATED_PER_SHARD.get();
    }

    @Override
    public int getCompressionPower(Level pLevel, BlockPos pos) {
        if (pLevel.getBlockEntity(pos) instanceof PiezoelectricHeavyPistonEntity entity) {
            return entity.getPistonStrength();
        }
        return 1;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof PiezoelectricHeavyPistonEntity) {
                ((PiezoelectricHeavyPistonEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                          Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof PiezoelectricHeavyPistonEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (PiezoelectricHeavyPistonEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PiezoelectricHeavyPistonEntity(pPos, pState);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, EntitiesRegistry.PIEZOELECTRIC_HEAVY_PISTON_ENTITY.get(), PiezoelectricHeavyPistonEntity::tick);
    }

    @Override
    protected boolean isEntityInstanceOf(Object o) {
        return o instanceof PiezoelectricHeavyPistonEntity;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.literal("Max tubes Depth is " + (MAX_TUBE_DEPTH-1)));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
