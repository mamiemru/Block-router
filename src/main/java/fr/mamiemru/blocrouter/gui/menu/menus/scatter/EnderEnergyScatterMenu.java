package fr.mamiemru.blocrouter.gui.menu.menus.scatter;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.scatter.EnderEnergyScatterEntity;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergySortMode;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.TeleportationCardSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.UpgradeSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnderEnergyScatterMenu extends BaseContainerMenuEnergySortMode {

    public EnderEnergyScatterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(EnderEnergyScatterEntity.NUMBER_OF_VARS));
    }

    public EnderEnergyScatterMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.ENDER_ENERGY_SCATTER_MENU.get(), data, entity);
        checkContainerSize(inv, EnderEnergyScatterEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            int teleportationSlotCoordinateX = 0;
            int teleportationSlotCoordinateY = 0;
            for (int slotId = EnderEnergyScatterEntity.SLOT_INPUT_TRANSFER_MIN; slotId <= EnderEnergyScatterEntity.SLOT_INPUT_TRANSFER_MAX; ++slotId) {
                if (teleportationSlotCoordinateX == 6) {
                    ++teleportationSlotCoordinateY;
                    teleportationSlotCoordinateX = 0;
                }
                this.addSlot(new TeleportationCardSlot(handler, slotId, 45+(teleportationSlotCoordinateX*18), 18+(teleportationSlotCoordinateY*18)));
                ++teleportationSlotCoordinateX;
            }

            this.addSlot(new UpgradeSlot(handler, EnderEnergyScatterEntity.SLOT_UPGRADE, 180, 5));
        });

        addDataSlots(data);
    }

    public EnderEnergyScatterEntity getEntity() {
        return (EnderEnergyScatterEntity) super.getEntity();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_ENDER_ENERGY_SCATTER.get());
    }
}

