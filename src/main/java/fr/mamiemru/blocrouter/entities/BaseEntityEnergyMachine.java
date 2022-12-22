package fr.mamiemru.blocrouter.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseEntityEnergyMachine extends BaseEntityEnergy {

    public BaseEntityEnergyMachine(BlockEntityType<?> entityType, BlockPos pos, BlockState state, int energyCapacity, int energyMaxTransfer, int energyProcess) {
        super(entityType, pos, state, energyCapacity, energyMaxTransfer, energyProcess);
    }

    @Override
    protected void onSlotContentChanged(int slot) {

    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseEntityEnergyMachine pEntity) {

        if (level.isClientSide() || !pEntity.isEnabled() || !pEntity.hasEnoughEnergy()) {
            return;
        }

        if (pEntity.processTick < pEntity.processMaxTickWithUpgrade()) {
            ++pEntity.processTick;
            return;
        }

        pEntity.useEnergy();
        pEntity.handleProcessing();
        pEntity.handleExtraction();

        pEntity.processTick = 0;
    }
}
