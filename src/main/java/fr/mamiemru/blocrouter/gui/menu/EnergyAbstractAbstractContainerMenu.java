package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.entities.custom.BaseEntityWithMenuProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class EnergyAbstractAbstractContainerMenu extends AbstractAbstractContainerMenu {

    protected EnergyAbstractAbstractContainerMenu(int id, Inventory inv, @NotNull MenuType<?> menu, ContainerData data, BaseEntityWithMenuProvider entity) {
        super(id, inv, menu, data, entity);
    }
}
