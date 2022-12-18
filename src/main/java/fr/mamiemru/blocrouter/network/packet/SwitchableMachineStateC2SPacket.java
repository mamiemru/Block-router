package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchableMachineStateC2SPacket {

    private boolean state;

    public SwitchableMachineStateC2SPacket(FriendlyByteBuf buf) {
        this.state = buf.readBoolean();
    }

    public SwitchableMachineStateC2SPacket(boolean state) {
        this.state = state;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.state);
    }

    public static void handle(SwitchableMachineStateC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;
            if (containerMenu instanceof BaseContainerMenuEnergy energyAbstractAbstractContainerMenu) {
                energyAbstractAbstractContainerMenu.setRedstoneState(message.state);energyAbstractAbstractContainerMenu.getEntity().setChanged();
            }

        });
        supplier.get().setPacketHandled(true);
    }
}
