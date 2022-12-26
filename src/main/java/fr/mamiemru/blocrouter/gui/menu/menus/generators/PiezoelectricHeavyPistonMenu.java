package fr.mamiemru.blocrouter.gui.menu.menus.generators;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricGeneratorBaseEntity;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricHeavyPistonEntity;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class PiezoelectricHeavyPistonMenu extends BaseContainerMenuEnergy {

    public PiezoelectricHeavyPistonMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(PiezoelectricHeavyPistonEntity.NUMBER_OF_VARS));
    }

    public PiezoelectricHeavyPistonMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.PIEZOELECTRIC_HEAVY_PISTON_MENU.get(), data, entity);
        checkContainerSize(inv, PiezoelectricHeavyPistonEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, PiezoelectricHeavyPistonEntity.SLOT_PISTON, 15, 18));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_PIEZOELECTRIC_HEAVY_PISTON.get());
    }
}

