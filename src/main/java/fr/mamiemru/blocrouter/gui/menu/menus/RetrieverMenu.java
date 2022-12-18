package fr.mamiemru.blocrouter.gui.menu.menus;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.RetrieverEntity;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.slot.UpgradeSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class RetrieverMenu extends BaseContainerMenuEnergy {

    public RetrieverMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(RetrieverEntity.NUMBER_OF_VARS));
    }

    public RetrieverMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.RETRIEVER_MENU.get(), data, entity);
        checkContainerSize(inv, RetrieverEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, RetrieverEntity.SLOT_OUTPUT_SLOT, 81, 20));
            this.addSlot(new UpgradeSlot(handler, RetrieverEntity.SLOT_UPGRADE, 180, 5));

        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_RETRIEVER.get());
    }
}

