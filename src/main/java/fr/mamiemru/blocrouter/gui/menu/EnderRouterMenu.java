package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.custom.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.EnderRouterEntity;
import fr.mamiemru.blocrouter.entities.custom.RouterEntity;
import fr.mamiemru.blocrouter.gui.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class EnderRouterMenu extends AbstractAbstractContainerMenu {

    public EnderRouterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(EnderRouterEntity.NUMBER_OF_SLOTS));
    }

    public EnderRouterMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.ENDER_ROUTER_MENU.get(), data, entity);
        checkContainerSize(inv, EnderRouterEntity.NUMBER_OF_SLOTS);

        EnderRouterEntity pEntity = (EnderRouterEntity) entity;

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_0, 10, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_1, 10+18, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_2, 10+18*2, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_3, 10, 18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_4, 10+18, 18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_5, 10+18*2, 18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_6, 10, 18*3));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_7, 10+18, 18*3));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_SLOT_8, 10+18*2, 18*3));

            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_0, 10+18*3, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_1, 10+18*4, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_2, 10+18*5, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_3, 10+18*3, 18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_4, 10+18*4, 18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_5, 10+18*5, 18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_6, 10+18*3, 18*3));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_7, 10+18*4, 18*3));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_INPUT_CARD_8, 10+18*5, 18*3));

            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_OUTPUT_SLOT_0, 10+18*6+2, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_OUTPUT_SLOT_1, 10+18*6+2, 18+18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_OUTPUT_SLOT_2, 10+18*6+2, 18+18*2));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_OUTPUT_CARD_0, 10+18*7+2, 18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_OUTPUT_CARD_1, 10+18*7+2, 18+18));
            this.addSlot(new SlotItemHandler(handler, EnderRouterEntity.SLOT_OUTPUT_CARD_2, 10+18*7+2, 18+18*2));

            this.addSlot(new PatternSlot(handler, EnderRouterEntity.SLOT_PATTERN_SLOT_0, 180, 5));
            this.addSlot(new PatternSlot(handler, EnderRouterEntity.SLOT_PATTERN_SLOT_1, 180, 5+18));
            this.addSlot(new PatternSlot(handler, EnderRouterEntity.SLOT_PATTERN_SLOT_2, 180, 5+18*2));
            this.addSlot(new PatternSlot(handler, EnderRouterEntity.SLOT_PATTERN_SLOT_3, 180, 5+18*3));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_ENDER_ROUTER.get());
    }
}

