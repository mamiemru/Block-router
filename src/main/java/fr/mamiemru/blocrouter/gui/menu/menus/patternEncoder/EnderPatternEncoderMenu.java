package fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.EnderPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
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

public class EnderPatternEncoderMenu extends BaseContainerMenuPatternEncoder {

    public EnderPatternEncoderMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(18));
    }

    public EnderPatternEncoderMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.ENDER_PATTERN_ENCODER_MENU.get(), data, entity, 9, 1);
        checkContainerSize(inv, EnderPatternEncoderEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            for (int i = 0; i < EnderPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
                this.addSlot(new PatternItemSlot(handler, i, 9 + 18 * i, 18));
                this.addSlot(new TeleportationCardSlot(handler, EnderPatternEncoderEntity.SLOT_INPUT_TELEPORTATION_CARD_SLOT_0+i, 9 + 18 * i, 36));
            }

            this.addSlot(new TeleportationCardSlot(handler, EnderPatternEncoderEntity.SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_0, 9, 55));
            this.addSlot(new TeleportationCardSlot(handler, EnderPatternEncoderEntity.SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_1, 27, 55));
            this.addSlot(new TeleportationCardSlot(handler, EnderPatternEncoderEntity.SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_2, 45, 55));

            this.addSlot(new PatternSlot(handler, EnderPatternEncoderEntity.SLOT_INPUT_SLOT_PATTERN, 144, 64));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_ENDER_PATTERN_ENCODER.get());
    }
}

