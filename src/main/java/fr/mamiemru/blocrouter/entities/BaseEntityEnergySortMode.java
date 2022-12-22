package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.blocks.BaseSortModeSwitchableFacingBlock;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseEntityEnergySortMode extends BaseEntityEnergyMachine {

    protected int slotPointer = 0;

    public BaseEntityEnergySortMode(BlockEntityType<?> entityType, BlockPos pos, BlockState state, int energyCapacity, int energyMaxTransfer, int energyProcess) {
        super(entityType, pos, state, energyCapacity, energyMaxTransfer, energyProcess);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("BaseEntityEnergySortMode.slotPointer", slotPointer);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        slotPointer = nbt.getInt("BaseEntityEnergySortMode.slotPointer");
    }
    public Comparable getSortMethod() {
        return getBlockState().getValue(BaseSortModeSwitchableFacingBlock.SORT_MODE);
    }
    public void toggleSortMethod() {
        setSortMethod(SortMode.nextMode(getSortMethod()));
    }
    public void setSortMethod(SortMode state) {
        level.setBlock(getBlockPos(), getBlockState().setValue(BaseSortModeSwitchableFacingBlock.SORT_MODE, state), 4);
    }
}
