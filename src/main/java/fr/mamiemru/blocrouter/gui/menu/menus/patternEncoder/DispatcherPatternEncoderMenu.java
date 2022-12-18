package fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.DispatcherPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternItemSlotWithBg;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.TeleportationCardSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class DispatcherPatternEncoderMenu extends BaseContainerMenuPatternEncoder {

    public DispatcherPatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(1));
    }

    public DispatcherPatternEncoderEntity getEntity() {
        return (DispatcherPatternEncoderEntity) this.blockEntity;
    }

    public DispatcherPatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.DISPATCHER_PATTERN_ENCODER_MENU.get(), data, entity, 9, 9);
        //checkContainerSize(inv, DispatcherPatternEncoderEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new PatternSlot(handler, DispatcherPatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 118));
            this.addSlot(new TeleportationCardSlot(handler, DispatcherPatternEncoderEntity.SLOT_INPUT_TELEPORTATION_CARD, 8, 116));

            for (int slotIdX = 0; slotIdX < 9; ++slotIdX) {
                for (int slotIdY = 0; slotIdY < 9; ++slotIdY) {
                    int index = DispatcherPatternEncoderEntity.SLOT_INPUT_MIN + slotIdY + slotIdX * 9;
                    int xPosition = 8 + 18 * slotIdX;
                    int yPosition = -46 + 18 * slotIdY;
                    this.addSlot(new PatternItemSlotWithBg(handler, index, xPosition, yPosition));
                }
            }
        });

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_PATTERN_DISPATCHER_ENCODER.get());
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 140 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
        }
    }

    public int getVectorType() {
        return data.get(0);
    }
    public void setVectorType(int index) {
        data.set(0, index);
    }
    public int incrementVectorType() {
        int vectorType = getVectorType();
        vectorType = (vectorType + 1) % 3;
        setVectorType(vectorType);
        return vectorType;
    }
}

