package fr.mamiemru.blocrouter.gui.menu.menus;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.MobLootSorterEntity;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.slot.FilterSlot;
import fr.mamiemru.blocrouter.gui.menu.slot.UpgradeSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class MobLootSorterMenu extends BaseContainerMenuEnergy {

    public MobLootSorterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(MobLootSorterEntity.NUMBER_OF_VARS));
    }

    public MobLootSorterMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.MOB_LOOT_SORTER_MENU.get(), data, entity);
        checkContainerSize(inv, MobLootSorterEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new UpgradeSlot(handler,MobLootSorterEntity.SLOT_UPGRADE, 180, 5));
            this.addSlot(new FilterSlot(handler,MobLootSorterEntity.SLOT_FILTER, 26, 22));
            this.addSlot(new FilterSlot(handler,MobLootSorterEntity.SLOT_TRASH, 26, 48));

            int posX = 0;
            int posY = 0;
            for (int slotIndex = MobLootSorterEntity.SLOT_INPUT_SLOT_MIN; slotIndex <= MobLootSorterEntity.SLOT_INPUT_SLOT_MAX; ++slotIndex) {
                this.addSlot(new SlotItemHandler(handler, slotIndex, 62 + 18 * posX, 18 + 18 * posY));
                if (posX == 4) {
                    posX = 0;
                    ++posY;
                } else {
                    ++posX;
                }
            }

        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_MOB_LOOT_SORTER.get());
    }

}

