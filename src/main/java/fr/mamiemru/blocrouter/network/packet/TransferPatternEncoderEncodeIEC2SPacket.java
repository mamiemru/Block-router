package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.TransferPatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TransferPatternEncoderEncodeIEC2SPacket {

    private int ioe;

    public TransferPatternEncoderEncodeIEC2SPacket(FriendlyByteBuf buf) {
        this.ioe = buf.readInt();
    }

    public TransferPatternEncoderEncodeIEC2SPacket(int ioe) {
        this.ioe = ioe;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.ioe);
    }

    public static void handle(TransferPatternEncoderEncodeIEC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof TransferPatternEncoderMenu container) {
                container.toggleInsertOrExtract(message.ioe);
                container.getEntity().setChanged();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
