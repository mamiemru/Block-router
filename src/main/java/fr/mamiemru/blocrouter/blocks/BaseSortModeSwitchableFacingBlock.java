package fr.mamiemru.blocrouter.blocks;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

public abstract class BaseSortModeSwitchableFacingBlock extends BaseSwitchableFacingBlock {

    public static final EnumProperty SORT_MODE = EnumProperty.create("sort_mode", SortMode.class);

    public BaseSortModeSwitchableFacingBlock() {
        super(Properties.of(Material.METAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(SORT_MODE, SortMode.ROUND_ROBIN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(SORT_MODE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(SORT_MODE, SortMode.ROUND_ROBIN);
    }
}
