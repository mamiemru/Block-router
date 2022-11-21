package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.custom.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.ScatterEntity;
import fr.mamiemru.blocrouter.gui.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.UpgradeSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ScatterMenu extends EnergyAbstractAbstractContainerMenu {

    public ScatterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(ScatterEntity.NUMBER_OF_VARS));
    }

    public ScatterMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.SCATTER_MENU.get(), data, entity);
        checkContainerSize(inv, ScatterEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, ScatterEntity.SLOT_INPUT_SLOT, 81, 20));
            this.addSlot(new UpgradeSlot(handler, ScatterEntity.SLOT_UPGRADE, 180, 5));

        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_SCATTER.get());
    }
}

