package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergyMachine;
import fr.mamiemru.blocrouter.gui.menu.menus.EnderRetrieverMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderRetrieverEntity extends BaseEntityEnergyMachine {

    public static final int SLOT_OUTPUT_SLOT = 0;
    public static final int SLOT_INPUT_TRANSFER_MIN = 1;
    public static final int SLOT_INPUT_TRANSFER_MAX = 18;
    public static final int SLOT_UPGRADE = 19;

    public static final int NUMBER_OF_SLOTS = 20;
    private int numberOfItemsToTransferPerOperation = 1;

    public static final int NUMBER_OF_VARS = 3;

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == SLOT_UPGRADE) {
            return stack.getItem() instanceof ItemProcessingUpgrade;
        } else if (SLOT_INPUT_TRANSFER_MIN <= slot && slot <= SLOT_INPUT_TRANSFER_MAX) {
            return stack.getItem() instanceof ItemTeleportationSlot;
        } else if (SLOT_OUTPUT_SLOT == slot) {
            return true;
        }
        return super.checkIsItemValid(slot, stack);
    }

    protected int containerDataGetter(int index) {
        return switch (index) {
            case 2 -> EnderRetrieverEntity.this.numberOfItemsToTransferPerOperation;
            default -> super.containerDataGetter(index);
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 2 -> EnderRetrieverEntity.this.numberOfItemsToTransferPerOperation = value;
            default -> super.containerDataSetter(index, value);
        }
    }
    protected int containerDataSize() {
        return 3;
    }

    public EnderRetrieverEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_RETRIEVER_ENTITY.get(), pos, state, 32000, 1024, 32);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Retriever");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderRetrieverMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected void handleExtraction() {}

    @Override
    protected void handleProcessing() {
        IItemHandler targetItemHandler = getItemHandler(level, getBlockPos(), Direction.UP);
        if (targetItemHandler != null) {
            for (int transferSlotId = SLOT_INPUT_TRANSFER_MIN; transferSlotId <= SLOT_INPUT_TRANSFER_MAX; ++transferSlotId) {
                useEnergy();
                ItemStack teleportationSlot = itemStackHandler.getStackInSlot(transferSlotId);
                if (teleportationSlot != null && !teleportationSlot.isEmpty() && teleportationSlot.getItem() instanceof ItemTeleportationSlot) {
                    BlockPos blockPos = ItemTeleportationSlot.getCoordinates(teleportationSlot);
                    IItemHandler sourceItemHandler = getItemHandler(level, blockPos, Direction.DOWN);
                    if (sourceItemHandler != null) {
                        processExtractionFromDistantToSelf(sourceItemHandler, targetItemHandler, SLOT_OUTPUT_SLOT, SLOT_OUTPUT_SLOT, numberOfItemsToTransferPerOperation);
                    }
                }
            }
        }
    }

    @Override
    protected int getSlotInputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotInputSlotN() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return SLOT_OUTPUT_SLOT;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return SLOT_OUTPUT_SLOT;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("EnderRetrieverEntity.numberOfItemsToTransferPerOperation", numberOfItemsToTransferPerOperation);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        numberOfItemsToTransferPerOperation = nbt.getInt("EnderRetrieverEntity.numberOfItemsToTransferPerOperation");
    }

    @Override
    public int getNumberOfSlots() {
        return EnderRetrieverEntity.NUMBER_OF_SLOTS;
    }
}
