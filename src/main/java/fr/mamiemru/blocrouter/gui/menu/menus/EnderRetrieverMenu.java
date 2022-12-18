package fr.mamiemru.blocrouter.gui.menu.menus;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.EnderRetrieverEntity;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.slot.TeleportationCardSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.UpgradeSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class EnderRetrieverMenu extends BaseContainerMenuEnergy {

    public EnderRetrieverMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(EnderRetrieverEntity.NUMBER_OF_VARS));
    }

    public EnderRetrieverMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.ENDER_RETRIEVER_MENU.get(), data, entity);
        checkContainerSize(inv, EnderRetrieverEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, EnderRetrieverEntity.SLOT_OUTPUT_SLOT, 8, 18));

            int teleportationSlotCoordinateX = 0;
            int teleportationSlotCoordinateY = 0;
            for (int slotId = EnderRetrieverEntity.SLOT_INPUT_TRANSFER_MIN; slotId <= EnderRetrieverEntity.SLOT_INPUT_TRANSFER_MAX; ++slotId) {
                if (teleportationSlotCoordinateX == 6) {
                    ++teleportationSlotCoordinateY;
                    teleportationSlotCoordinateX = 0;
                }
                this.addSlot(new TeleportationCardSlot(handler, slotId, 45+(teleportationSlotCoordinateX*18), 18+(teleportationSlotCoordinateY*18)));
                ++teleportationSlotCoordinateX;
            }

            this.addSlot(new UpgradeSlot(handler, EnderRetrieverEntity.SLOT_UPGRADE, 180, 5));

        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_ENDER_RETRIEVER.get());
    }
}

