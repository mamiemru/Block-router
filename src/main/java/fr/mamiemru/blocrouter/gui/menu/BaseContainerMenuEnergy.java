package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.EnergySyncS2CPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public abstract class BaseContainerMenuEnergy extends BaseContainerMenu {

    protected BaseContainerMenuEnergy(int id, Inventory inv, @NotNull MenuType<?> menu, ContainerData data, BaseEntityEnergy entity) {
        super(id, inv, menu, data, entity);
        ModNetworking.sendToClients(new EnergySyncS2CPacket(getEntity().getEnergyStorage().getEnergyStored(), getEntity().getBlockPos()));
    }

    public static BaseEntityEnergy getCasterEntity(Inventory inv, FriendlyByteBuf extraData) {
        return (BaseEntityEnergy) inv.player.level.getBlockEntity(extraData.readBlockPos());
    }

    public BaseEntityEnergy getEntity() {
        return (BaseEntityEnergy) super.getEntity();
    }

    public boolean switchRedstoneState() {
        this.getEntity().toggleEnable();
        return getRedstoneState();
    }

    public void setRedstoneState(boolean state) {
        this.getEntity().setEnable(state);
    }

    public boolean getRedstoneState() {
        return this.getEntity().isEnabled();
    }
}
