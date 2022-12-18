package fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.SlotPatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.VacuumPatternEncoderEntity;
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

import java.awt.*;

public class VacuumPatternEncoderMenu extends BaseContainerMenuPatternEncoder {

    public VacuumPatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(0));
    }

    @Override
    public VacuumPatternEncoderEntity getEntity() {
        return (VacuumPatternEncoderEntity) super.getEntity();
    }

    public VacuumPatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.VACUUM_PATTERN_ENCODER_MENU.get(), data, entity, 13, 1);
        checkContainerSize(inv, VacuumPatternEncoderEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_0, 27, 18));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_1, 45, 18));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_2, 63, 18));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_3, 27, 36));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_4, 45, 36));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_5, 63, 36));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_6, 27, 54));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_7, 45, 54));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_8, 63, 54));

            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_OUTPUT_SLOT_0, 126, 18));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_OUTPUT_SLOT_1, 144, 18));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_OUTPUT_SLOT_2, 126, 36));
            this.addSlot(new PatternItemSlot(handler, VacuumPatternEncoderEntity.SLOT_OUTPUT_SLOT_3, 144, 36));

            this.addSlot(new PatternSlot(handler, VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 64));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_PATTERN_VACUUM_ENCODER.get());
    }
}

