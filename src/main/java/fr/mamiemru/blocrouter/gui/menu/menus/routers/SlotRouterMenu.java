package fr.mamiemru.blocrouter.gui.menu.menus.routers;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.routers.RouterEntity;
import fr.mamiemru.blocrouter.entities.custom.routers.SlotRouterEntity;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.UpgradeSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class SlotRouterMenu extends BaseContainerMenuEnergy {

    public SlotRouterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(SlotRouterEntity.NUMBER_OF_SLOTS));
    }

    public SlotRouterMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.SLOT_ROUTER_MENU.get(), data, entity);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_0, 12, 18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_1, 12+18, 18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_2, 12+18*2, 18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_3, 12, 18*2));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_4, 12+18, 18*2));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_5, 12+18*2, 18*2));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_6, 12, 18*3));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_7, 12+18, 18*3));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_INPUT_SLOT_8, 12+18*2, 18*3));

            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_OUTPUT_SLOT_0, 116, 18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_OUTPUT_SLOT_1, 116, 18+18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_OUTPUT_SLOT_2, 116, 18+18*2));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_OUTPUT_SLOT_3, 116+18, 18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_OUTPUT_SLOT_4, 116+18, 18+18));
            this.addSlot(new SlotItemHandler(handler, SlotRouterEntity.SLOT_OUTPUT_SLOT_5, 116+18, 18+18*2));

            this.addSlot(new UpgradeSlot(handler, SlotRouterEntity.SLOT_UPGRADE, 180, 5));

            this.addSlot(new PatternSlot(handler, SlotRouterEntity.SLOT_PATTERN_SLOT_0, 180, 23));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_SLOT_ROUTER.get());
    }
}

