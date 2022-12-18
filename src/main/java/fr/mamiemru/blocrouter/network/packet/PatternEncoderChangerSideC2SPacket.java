package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.PatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PatternEncoderChangerSideC2SPacket {

    private int slot;
    private boolean isSide;

    public PatternEncoderChangerSideC2SPacket(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.isSide = buf.readBoolean();
    }

    public PatternEncoderChangerSideC2SPacket(int slot, boolean isSide) {
        this.slot = slot;
        this.isSide = isSide;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.slot);
        buf.writeBoolean(this.isSide);
    }

    public static void handle(PatternEncoderChangerSideC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof PatternEncoderMenu container) {
                if (container.getEntity() instanceof PatternEncoderEntity patternEncoderEntity) {
                    if (message.isSide) {
                        container.incrementSideData(message.slot);
                    } else {
                        container.incrementSlotData(message.slot);
                    }
                    patternEncoderEntity.setChanged();
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
