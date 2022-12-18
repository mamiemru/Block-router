package fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.Container.ContainerListData;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.TransferPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class TransferPatternEncoderMenu extends BaseContainerMenuPatternEncoder {

    public TransferPatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new ContainerListData(TransferPatternEncoderEntity.NUMBER_OF_VARS));
    }

    public TransferPatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.TRANSFER_PATTERN_ENCODER_MENU.get(), data, entity, 1, 1);
        checkContainerSize(inv, entity.getNumberOfSlots());
        initSlots();
    }

    public void initSlots() {
        super.slots.clear();
        super.initPlayerSLots();
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRANSFER_SLOT_0, 7, 18));
            this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_OUTPUT_TRANSFER_SLOT_0, 154, 7));
            this.addSlot(new PatternSlot(handler, TransferPatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 64));

            if (isInsertion()) {
                initSlotInsertion(handler);
            } else {
                initSlotExtraction(handler);
            }
        });
        addDataSlots(data);
    }

    public void initSlotInsertion(@NotNull IItemHandler handler) {
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_0, 100, 7));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_1, 118, 7));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_2, 136, 7));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_3, 100, 25));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_4, 118, 25));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_5, 136, 25));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_6, 100, 43));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_7, 118, 43));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_8, 136, 43));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_0, 28, 18));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_1, 46, 18));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_2, 64, 18));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_3, 28, 36));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_4, 46, 36));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_5, 64, 36));
    }

    public void initSlotExtraction(@NotNull IItemHandler handler) {
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_0, 100, 7));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_1, 118, 7));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_2, 136, 7));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_3, 100, 26));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_4, 118, 26));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_5, 136, 26));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_6, 100, 45));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_7, 118, 45));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_FILTER_SLOT_8, 136, 45));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_0, 28, 18));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_1, 46, 18));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_2, 64, 18));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_3, 28, 36));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_4, 46, 36));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_INPUT_TRASH_SLOT_5, 64, 36));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_OUTPUT_TRANSFER_SLOT_1, 154, 26));
        this.addSlot(new SlotItemHandler(handler, TransferPatternEncoderEntity.SLOT_OUTPUT_TRANSFER_SLOT_2, 154, 45));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_TRANSFER_PATTERN_ENCODER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }
    public int getWhiteBlackList() {
        return data.get(TransferPatternEncoderEntity.VAR_WHITELIST_INDEX);
    }
    public void toggleWhiteBlackList(int wb) {
        data.set(TransferPatternEncoderEntity.VAR_WHITELIST_INDEX, (wb+1) % 2);
    }
    public boolean isInsertion() {
        return getInsertOrExtract() == 0;
    }
    public boolean isExtract() {return getInsertOrExtract() == 1;}
    public int getInsertOrExtract() {
        return data.get(TransferPatternEncoderEntity.VAR_INSERT_OR_EXTRACT);
    }

    public void toggleInsertOrExtract(int ioe) {
        if (isInsertion()) {
            ((TransferPatternEncoderEntity) getEntity()).dropToggleInsertion();
        } else if (isExtract()) {
            ((TransferPatternEncoderEntity) getEntity()).dropToggleExtraction();
        }
        data.set(TransferPatternEncoderEntity.VAR_INSERT_OR_EXTRACT, (ioe+1) % 2);
    }
}

