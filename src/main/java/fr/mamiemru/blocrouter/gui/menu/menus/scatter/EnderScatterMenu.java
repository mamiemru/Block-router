package fr.mamiemru.blocrouter.gui.menu.menus.scatter;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.scatter.EnderScatterEntity;
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

public class EnderScatterMenu extends BaseContainerMenuEnergy {

    public EnderScatterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(EnderScatterEntity.NUMBER_OF_VARS));
    }

    public EnderScatterMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.ENDER_SCATTER_MENU.get(), data, entity);
        checkContainerSize(inv, EnderScatterEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, EnderScatterEntity.SLOT_INPUT_SLOT, 8, 18));

            int teleportationSlotCoordinateX = 0;
            int teleportationSlotCoordinateY = 0;
            for (int slotId = EnderScatterEntity.SLOT_INPUT_TRANSFER_MIN; slotId <= EnderScatterEntity.SLOT_INPUT_TRANSFER_MAX; ++slotId) {
                if (teleportationSlotCoordinateX == 6) {
                    ++teleportationSlotCoordinateY;
                    teleportationSlotCoordinateX = 0;
                }
                this.addSlot(new TeleportationCardSlot(handler, slotId, 45+(teleportationSlotCoordinateX*18), 18+(teleportationSlotCoordinateY*18)));
                ++teleportationSlotCoordinateX;
            }

            this.addSlot(new UpgradeSlot(handler, EnderScatterEntity.SLOT_UPGRADE, 180, 5));

        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_ENDER_SCATTER.get());
    }
}

