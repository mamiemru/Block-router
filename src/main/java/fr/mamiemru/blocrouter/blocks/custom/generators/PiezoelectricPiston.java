package fr.mamiemru.blocrouter.blocks.custom.generators;

import fr.mamiemru.blocrouter.blocks.BaseSwitchableFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public abstract class PiezoelectricPiston extends BaseSwitchableFacingBlock {

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    public PiezoelectricPiston() {
        super(Properties.of(Material.PISTON).sound(SoundType.METAL).noOcclusion()
                .isValidSpawn(PiezoelectricTube::never)
                .isRedstoneConductor(PiezoelectricTube::never)
                .isSuffocating(PiezoelectricTube::never)
                .isViewBlocking(PiezoelectricTube::never)
        );
        this.registerDefaultState(
                this.stateDefinition.any().setValue(EXTENDED, Boolean.valueOf(false))
        );
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                          Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(EXTENDED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(EXTENDED, false);
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        this.onPowerState(pLevel, pPos, pState);
    }

    protected abstract void onPowerState(Level level, BlockPos pos, BlockState blockState);
    public abstract int getEnergyGeneratedPerShard();
    public abstract int getCompressionPower(Level level, BlockPos pos);
}
