package fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.Sides;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternItemSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class PatternEncoderMenu extends BaseContainerMenuPatternEncoder {

    public PatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(PatternEncoderEntity.NUMBER_OF_VARS));
    }

    public PatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.PATTERN_ENCODER_MENU.get(), data, entity, 6, 1);
        checkContainerSize(inv, PatternEncoderEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            for (int slotIndex = 0; slotIndex < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++slotIndex) {
                this.addSlot(new PatternItemSlot(handler, slotIndex, 9 + 18 * slotIndex, 18));
            }
            this.addSlot(new PatternSlot(handler, PatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 64));

            super.fillFakeSlots(craftMatrix, handler);
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
        setSideData(index, (getSideData(index)+1) % Sides.NUMBER_OF_SIDES);
        return getSideData(index);
    }

    public int incrementSlotData(int index) {
        setSlotData(index, (getSlotData(index)+1) % 9);
        return getSlotData(index);
    }

}

