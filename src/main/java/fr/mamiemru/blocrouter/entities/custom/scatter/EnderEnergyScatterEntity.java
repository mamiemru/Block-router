package fr.mamiemru.blocrouter.entities.custom.scatter;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergySortMode;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderEnergyScatterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderEnergyScatterEntity extends BaseEntityEnergySortMode {
    public static final int SLOT_INPUT_TRANSFER_MIN = 0;
    public static final int SLOT_INPUT_TRANSFER_MAX = 17;
    public static final int SLOT_UPGRADE = 18;
    public static final int NUMBER_OF_SLOTS = 19;

    public static final int NUMBER_OF_VARS = 3;

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == SLOT_UPGRADE) {
            return stack.getItem() instanceof ItemProcessingUpgrade;
        } else if (SLOT_INPUT_TRANSFER_MIN <= slot && slot <= SLOT_INPUT_TRANSFER_MAX) {
            return stack.getItem() instanceof ItemTeleportationSlot;
        }
        return false;
    }

    @Override
    protected boolean canExtractSide(int i) {
        return false;
    }

    protected int containerDataGetter(int index) {
        return switch (index) {
            case 2 -> EnderEnergyScatterEntity.this.slotPointer;
            default -> super.containerDataGetter(index);
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 2 -> EnderEnergyScatterEntity.this.slotPointer = value;
            default -> super.containerDataSetter(index, value);
        }
    }
    protected int containerDataSize() {
        return NUMBER_OF_VARS;
    }

    public EnderEnergyScatterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_ENERGY_SCATTER_ENTITY.get(), pos, state, 2048000, 18432, 16);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Energy Scatter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderEnergyScatterMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_TRANSFER_MIN;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_TRANSFER_MAX;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return -1;
    }

    private void processPriority() {
        ItemStack is = itemStackHandler.getStackInSlot(slotPointer);
        useEnergy();
        BlockPos blockPos = ItemTeleportationSlot.getCoordinates(is);
        IEnergyStorage energyStorage = getEnergyHandler(level, blockPos, null);
        if (energyStorage != null) {
            int simulated = energyStorage.receiveEnergy(getEnergyMaxTransfer(), true);
            if (getEnergyStorage().extractEnergy(simulated, true) == simulated) {
                getEnergyStorage().extractEnergy(simulated, false);
                energyStorage.receiveEnergy(simulated, false);
            }

            int percentage = Math.floorDiv(Math.multiplyExact(energyStorage.getEnergyStored(), 100), energyStorage.getMaxEnergyStored());
            if (90 < percentage) {
                ++slotPointer;
            }

        } else {
            ++slotPointer;
        }
    }

    private void processRoundRobin() {
        ItemStack is = itemStackHandler.getStackInSlot(slotPointer);
        useEnergy();
        BlockPos blockPos = ItemTeleportationSlot.getCoordinates(is);
        IEnergyStorage energyStorage = getEnergyHandler(level, blockPos, null);
        if (energyStorage != null) {
            int simulated = energyStorage.receiveEnergy(getEnergyMaxTransfer(), true);
            if (getEnergyStorage().extractEnergy(simulated, true) == simulated) {
                getEnergyStorage().extractEnergy(simulated, false);
                energyStorage.receiveEnergy(simulated, false);
            }
        }
        ++slotPointer;
    }

    @Override
    protected void handleExtraction() {

    }

    @Override
    protected void handleProcessing() {
        ItemStack is = itemStackHandler.getStackInSlot(slotPointer);
        if (is != null && is.getItem() instanceof ItemTeleportationSlot && is.getCount() == 1 && is.hasTag()) {
            Comparable sortMethod = getSortMethod();
            if (sortMethod == SortMode.ROUND_ROBIN) {
                processRoundRobin();
            } else if (sortMethod == SortMode.PRIORITY) {
                processPriority();
            }
        } else {
            ++slotPointer;
        }

        if (slotPointer == SLOT_INPUT_TRANSFER_MAX) {
            slotPointer = SLOT_INPUT_TRANSFER_MIN;
        }
    }

    @Override
    public int getNumberOfSlots() {
        return EnderEnergyScatterEntity.NUMBER_OF_SLOTS;
    }

}
