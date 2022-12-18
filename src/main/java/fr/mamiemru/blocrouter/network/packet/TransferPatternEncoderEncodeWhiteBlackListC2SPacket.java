package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.TransferPatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TransferPatternEncoderEncodeWhiteBlackListC2SPacket {

    private int wb;
    public TransferPatternEncoderEncodeWhiteBlackListC2SPacket(FriendlyByteBuf buf) {
        this.wb = buf.readInt();
    }

    public TransferPatternEncoderEncodeWhiteBlackListC2SPacket(int wb) {
        this.wb = wb;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.wb);
    }

    public static void handle(TransferPatternEncoderEncodeWhiteBlackListC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof TransferPatternEncoderMenu container) {
                container.toggleWhiteBlackList(message.wb);
                container.getEntity().setChanged();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
