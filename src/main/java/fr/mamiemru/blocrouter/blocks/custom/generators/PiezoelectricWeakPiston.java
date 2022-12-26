package fr.mamiemru.blocrouter.blocks.custom.generators;

import fr.mamiemru.blocrouter.blocks.BaseSwitchableFacingBlock;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.ContentLevel;
import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricGeneratorBaseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PiezoelectricWeakPiston extends PiezoelectricPiston {

    protected void onPowerState(Level level, BlockPos pos, BlockState blockState) {
        boolean flag = level.hasNeighborSignal(pos);
        boolean isEnabled = blockState.getValue(BaseSwitchableFacingBlock.ENABLED).booleanValue();

        if (flag && !level.isClientSide() && isEnabled) {
            level.setBlock(pos, blockState.setValue(EXTENDED, true), 4);

            BlockPos below = pos.below();
            BlockPos below2 = below.below();

            BlockState tubeBlock = level.getBlockState(below);
            BlockState generatorBlock = level.getBlockState(below2);

            boolean isBelowTubeBlock = tubeBlock.getBlock() instanceof PiezoelectricTube;
            boolean isBelow2Generator = generatorBlock.getBlock() instanceof PiezoelectricGeneratorBase;

            if (isBelowTubeBlock && isBelow2Generator && level.getBlockEntity(below2) instanceof PiezoelectricGeneratorBaseEntity pegbe) {
                Random rand = new Random();
                PiezoelectricTube piezoelectricTube = ((PiezoelectricTube) tubeBlock.getBlock());
                int b = BlockRouterConfig.PIEZOELECTRIC_GENERATOR_WEAK_PISTON_BREAK_CRYSTAL_RANDOM_BOUND.get();
                int compressionPower = getCompressionPower(level, pos);
                int energyPerShard = getEnergyGeneratedPerShard();
                int numberOfShards = ContentLevel.toIndex(piezoelectricTube.getContentLevel(level, below));
                boolean jobDone = pegbe.generateEnergy(compressionPower, energyPerShard, 0, numberOfShards);
                if (jobDone && rand.nextInt(0, b) == 0) {
                    piezoelectricTube.decrementContentLevel(level, below);
                }
            }
        }

        level.setBlock(pos, blockState.setValue(EXTENDED, false), 4);
    }

    @Override
    public int getEnergyGeneratedPerShard() {
        return BlockRouterConfig.PIEZOELECTRIC_GENERATOR_WEAK_PISTON_ENERGY_GENERATED_PER_SHARD.get();
    }

    @Override
    public int getCompressionPower(Level pLevel, BlockPos pos) {
        return 1;
    }

    @Override
    protected boolean isEntityInstanceOf(Object o) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }
}
