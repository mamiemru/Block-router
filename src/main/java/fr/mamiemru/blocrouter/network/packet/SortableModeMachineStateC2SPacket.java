package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderEnergyScatterMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SortableModeMachineStateC2SPacket {

    private int state;

    public SortableModeMachineStateC2SPacket(FriendlyByteBuf buf) {
        this.state = buf.readInt();
    }

    public SortableModeMachineStateC2SPacket(Comparable state) {
        this.state = SortMode.toIndex(state);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.state);
    }

    public static void handle(SortableModeMachineStateC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;
            if (containerMenu instanceof EnderEnergyScatterMenu conEnderEnergyScatterMenu) {
                conEnderEnergyScatterMenu.setSortMethodState(SortMode.fromIndex(message.state));
                conEnderEnergyScatterMenu.getEntity().setChanged();
            }

        });
        supplier.get().setPacketHandled(true);
    }
}
