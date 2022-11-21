package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.custom.PatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.custom.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.gui.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternItemSlot;
import fr.mamiemru.blocrouter.util.PatternUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.BooleanSupplier;

public class PatternEncoderMenu extends AbstractAbstractContainerMenu {

    public PatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(PatternEncoderEntity.NUMBER_OF_VARS));
    }

    public PatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.PATTERN_ENCODER_MENU.get(), data, entity);
        checkContainerSize(inv, PatternEncoderEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            for (int i = 0; i < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
                this.addSlot(new PatternItemSlot(handler, i, 9 + 18 * i, 18));
            }
            this.addSlot(new SlotItemHandler(handler, PatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 64));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_PATTERN_ENCODER.get());
    }

    public int getSideData(int index) {
        return data.get(index);
    }

    public void setSideData(int index, int value) {
        data.set(index, value);
    }

    public int getSlotData(int index) {
        return data.get(9+index);
    }

    public void setSlotData(int index, int value) {
        data.set(9+index, value);
    }

    public int incrementSideData(int index) {
        setSideData(index, (getSideData(index)+1) % PatternUtil.LIST_OF_AXES.length);
        return getSideData(index);
    }

    public int incrementSlotData(int index) {
        setSlotData(index, (getSlotData(index)+1) % 9);
        return getSlotData(index);
    }

}

