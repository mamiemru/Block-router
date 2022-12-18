package fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.EnderPatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.SlotPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternItemSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.TeleportationCardSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import java.util.List;

public class SlotPatternEncoderMenu extends BaseContainerMenuPatternEncoder {

    public SlotPatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(18));
    }

    @Override
    public SlotPatternEncoderEntity getEntity() {
        return (SlotPatternEncoderEntity) super.getEntity();
    }

    public SlotPatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.SLOT_PATTERN_ENCODER_MENU.get(), data, entity, 9, 1);
        checkContainerSize(inv, SlotPatternEncoderEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            for (int i = 0; i < SlotPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
                this.addSlot(new PatternItemSlot(handler, i, 9 + 18 * i, 18));
            }
            this.addSlot(new TeleportationCardSlot(handler, SlotPatternEncoderEntity.SLOT_INPUT_TELEPORTATION_SLOT, 62, 64));
            this.addSlot(new PatternSlot(handler, SlotPatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 64));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_PATTERN_SLOT_ENCODER.get());
    }

    public int getSlotData(int index) {
        return data.get(index);
    }

    public void setSlotData(int index, int slot) {
        data.set(index, slot);
    }

    public int incrementSlotData(int index) {
        int slot = getSlotData(index);
        int slots = getEntity().getTotalSlotsFromTargetedEntity();
        int nextSlot = (slots == 0)? 0 : (slot+1) % slots;
        setSlotData(index, nextSlot);
        return nextSlot;
    }
}

