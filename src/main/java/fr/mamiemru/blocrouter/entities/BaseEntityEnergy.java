package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.blocks.BaseSwitchableFacingBlock;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.EnergySyncS2CPacket;
import fr.mamiemru.blocrouter.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class BaseEntityEnergy extends BaseEntityWithMenuProvider {

    protected ModEnergyStorage ENERGY_STORAGE;
    protected int energyProcess;
    protected int energyMaxTransfer;
    protected LazyOptional<EnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public BaseEntityEnergy(BlockEntityType<?> entityType, BlockPos pos, BlockState state, int energyCapacity, int energyMaxTransfer, int energyProcess) {
        super(entityType, pos, state);
        this.energyProcess = energyProcess;
        this.energyMaxTransfer = energyMaxTransfer;

        ENERGY_STORAGE = new ModEnergyStorage(energyCapacity, energyMaxTransfer) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                ModNetworking.sendToClients(new EnergySyncS2CPacket(ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
            }
        };
    }

    protected boolean hasEnoughEnergy() {
        return ENERGY_STORAGE.getEnergyStored() >= energyProcess;
    }

    protected void useEnergy() {
        ENERGY_STORAGE.extractEnergy(energyProcess, false);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        ENERGY_STORAGE.onEnergyChanged();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("EnergyEntity.energy", ENERGY_STORAGE.getEnergyStored());
        nbt.putBoolean("EnergyEntity.enabled", getBlockState().getValue(BaseSwitchableFacingBlock.ENABLED));
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ENERGY_STORAGE.setEnergy(nbt.getInt("EnergyEntity.energy"));
        getBlockState().setValue(BaseSwitchableFacingBlock.ENABLED, nbt.getBoolean("EnergyEntity.enabled"));
    }

    public int getEnergyOperationCost() { return this.energyProcess; }

    public int getEnergyMaxTransfer() { return this.energyMaxTransfer; }

    public boolean isEnabled() {
        return getBlockState().getValue(BaseSwitchableFacingBlock.ENABLED).booleanValue();
    }

    public void setEnable(boolean enabled) {
        level.setBlock(getBlockPos(), getBlockState().setValue(BaseSwitchableFacingBlock.ENABLED, enabled), 4);
    }

    public void toggleEnable() {
        setEnable(!isEnabled());
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }
}
