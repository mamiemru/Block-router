package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.DispatcherPatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DispatcherPatternEncoderChangerVectorTypeC2SPacket {

    private int vectorType;

    public DispatcherPatternEncoderChangerVectorTypeC2SPacket(FriendlyByteBuf buf) {
        this.vectorType = buf.readInt();
    }

    public DispatcherPatternEncoderChangerVectorTypeC2SPacket(int vectorType) {
        this.vectorType = vectorType;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.vectorType);
    }

    public static void handle(DispatcherPatternEncoderChangerVectorTypeC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof DispatcherPatternEncoderMenu container) {
                container.setVectorType(message.vectorType);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
