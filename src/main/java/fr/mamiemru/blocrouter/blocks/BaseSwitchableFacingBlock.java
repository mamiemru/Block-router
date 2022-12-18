package fr.mamiemru.blocrouter.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BaseSwitchableFacingBlock extends BaseFacingBlock {

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    protected BaseSwitchableFacingBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, Boolean.valueOf(true)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ENABLED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(ENABLED, true);
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        this.checkPoweredState(pLevel, pPos, pState);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState blockState) {
        boolean flag = !level.hasNeighborSignal(pos);
        if (flag != blockState.getValue(ENABLED)) {
            level.setBlock(pos, blockState.setValue(ENABLED, Boolean.valueOf(flag)), 4);
        }
    }
}
