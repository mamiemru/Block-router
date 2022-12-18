package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.SlotPatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PatternEncoderChangerSlotC2SPacket {

    private int slot;
    private int index;

    public PatternEncoderChangerSlotC2SPacket(FriendlyByteBuf buf) {
        this.index = buf.readByte();
        this.slot = buf.readInt();
    }

    public PatternEncoderChangerSlotC2SPacket(int index, int slot) {
        this.index = index;
        this.slot = slot;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(this.index);
        buf.writeInt(this.slot);
    }

    public static void handle(PatternEncoderChangerSlotC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof SlotPatternEncoderMenu container) {
                container.setSlotData(message.index, message.slot);
                container.getEntity().setChanged();
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
