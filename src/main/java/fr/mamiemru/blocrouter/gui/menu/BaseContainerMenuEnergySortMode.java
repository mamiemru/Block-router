package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergySortMode;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public abstract class BaseContainerMenuEnergySortMode extends BaseContainerMenuEnergy {

    protected BaseContainerMenuEnergySortMode(int id, Inventory inv, @NotNull MenuType<?> menu, ContainerData data, BaseEntityEnergy entity) {
        super(id, inv, menu, data, entity);
    }

    public BaseEntityEnergySortMode getEntity() {
        return (BaseEntityEnergySortMode) super.getEntity();
    }

    public Comparable switchSortMethodState() {
        this.getEntity().toggleSortMethod();
        return getSortMethodState();
    }

    public void setSortMethodState(SortMode state) {
        this.getEntity().setSortMethod(state);
    }

    public Comparable getSortMethodState() {
        return this.getEntity().getSortMethod();
    }

}

