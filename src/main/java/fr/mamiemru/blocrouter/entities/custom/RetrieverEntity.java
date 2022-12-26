package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergyMachine;
import fr.mamiemru.blocrouter.gui.menu.menus.RetrieverMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RetrieverEntity extends BaseEntityEnergyMachine {

    public static final int SLOT_OUTPUT_SLOT = 0;
    public static final int SLOT_UPGRADE = 1;

    public static final int NUMBER_OF_SLOTS = 2;
    private int numberOfItemsToTransferPerOperation = 1;

    public static final int NUMBER_OF_VARS = 3;

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemProcessingUpgrade) {
            return slot == SLOT_UPGRADE;
        } else if (slot == SLOT_OUTPUT_SLOT) {
            return true;
        }
        return false;
    }

    protected int containerDataGetter(int index) {
        return switch (index) {
            case 2 -> RetrieverEntity.this.numberOfItemsToTransferPerOperation;
            default -> super.containerDataGetter(index);
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 2 -> RetrieverEntity.this.numberOfItemsToTransferPerOperation = value;
            default -> super.containerDataSetter(index, value);
        }
    }
    protected int containerDataSize() {
        return 3;
    }

    public RetrieverEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.RETRIEVER_ENTITY.get(), pos, state,
                BlockRouterConfig.SCATTER_RETRIEVER_ENERGY_CAPACITY.get(),
                BlockRouterConfig.SCATTER_RETRIEVER_ENERGY_MAX_TRANSFER.get(),
                BlockRouterConfig.SCATTER_RETRIEVER_ENERGY_COST_PER_OPERATION.get()
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Retriever");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RetrieverMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected void handleExtraction() {

    }

    @Override
    protected void handleProcessing() {
        BlockEntity block_entity_reference = level.getBlockEntity(getBlockPos().above());
        forEveryMachines(this, block_entity_reference);
        useEnergy();
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
        nbt.putInt("RetrieverEntity.numberOfItemsToTransferPerOperation", numberOfItemsToTransferPerOperation);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        numberOfItemsToTransferPerOperation = nbt.getInt("RetrieverEntity.numberOfItemsToTransferPerOperation");
    }

    private static void forEveryMachines(@NotNull RetrieverEntity pEntity, BlockEntity block_entity_reference) {

        Level level = pEntity.getLevel();
        BlockEntity block_entity_selected  = block_entity_reference;
        IItemHandler targetItemHandler = pEntity.getItemHandler(level, pEntity.getBlockPos(), Direction.UP);
        while (block_entity_selected != null && block_entity_reference.getClass().equals(block_entity_selected.getClass())) {

            BlockPos entity_pos = block_entity_selected.getBlockPos();
            IItemHandler sourceItemHandler = pEntity.getItemHandler(level, entity_pos, Direction.DOWN);

            if (sourceItemHandler != null) {
                processExtractionFromDistantToSelf(sourceItemHandler, targetItemHandler, SLOT_OUTPUT_SLOT, SLOT_OUTPUT_SLOT, pEntity.numberOfItemsToTransferPerOperation);
            }

            block_entity_selected = level.getBlockEntity(block_entity_selected.getBlockPos().above());
        }
    }

    @Override
    public int getNumberOfSlots() {
        return RetrieverEntity.NUMBER_OF_SLOTS;
    }
}
