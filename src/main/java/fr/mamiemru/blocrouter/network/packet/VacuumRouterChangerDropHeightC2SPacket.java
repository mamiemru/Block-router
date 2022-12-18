package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.SlotPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.VacuumRouterMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VacuumRouterChangerDropHeightC2SPacket {

    private int index;

    public VacuumRouterChangerDropHeightC2SPacket(FriendlyByteBuf buf) {
        this.index = buf.readByte();
    }

    public VacuumRouterChangerDropHeightC2SPacket(int index) {
        this.index = index;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(this.index);
    }

    public static void handle(VacuumRouterChangerDropHeightC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof VacuumRouterMenu container) {
                container.setDropHeightIndex(message.index);
                container.getEntity().setChanged();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
