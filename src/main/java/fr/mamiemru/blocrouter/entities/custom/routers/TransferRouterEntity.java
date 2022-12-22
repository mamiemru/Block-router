package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.TransferRouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemTransferRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransferRouterEntity extends BaseEntityEnergyRouter {

    public static final int SLOT_INPUT_SLOT = 0;
    public static final int SLOT_UPGRADE = 1;

    public static final int NUMBER_OF_SLOTS = 2;
    public static final int NUMBER_OF_VARS = 2;

    @Override
    protected Pattern getCastedPattern(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemTransferRoutingPattern && is.getCount() == 1 && is.hasTag()) ?
                ItemTransferRoutingPattern.decodePatternTag(is) : null;
    }

    @Override
    protected boolean isPatternRight(Item item) {
        return item instanceof ItemTransferRoutingPattern;
    }

    @Override
    protected int getSlotPatternSlot0() {
        return 0;
    }

    @Override
    protected int getNumberOfPatternSlots() {
        return 0;
    }

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemProcessingUpgrade) {
            return slot == SLOT_UPGRADE;
        }
        return true;
    }

    public TransferRouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.TRANSFER_ROUTER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Transfer Router");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TransferRouterMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected void handleExtraction(@NotNull Pattern pattern) {

    }

    @Override
    protected void handleProcessing(@NotNull Pattern pattern) {

    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_SLOT;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_SLOT;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return -1;
    }

    @Override
    public int getNumberOfSlots() {
        return TransferRouterEntity.NUMBER_OF_SLOTS;
    }
}
