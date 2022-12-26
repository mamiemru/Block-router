package fr.mamiemru.blocrouter.blocks;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseBlockEntityRouterBlock extends BaseEntityBlock {

    protected BaseBlockEntityRouterBlock(Properties pProperties) {
        super(pProperties.strength(0.5f).requiresCorrectToolForDrops());
    }

    protected abstract boolean isEntityInstanceOf(Object o);

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

}
