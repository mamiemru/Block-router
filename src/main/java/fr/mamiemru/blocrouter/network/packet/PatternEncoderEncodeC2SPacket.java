package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.entities.custom.PatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.PatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PatternEncoderEncodeC2SPacket {


    public PatternEncoderEncodeC2SPacket(FriendlyByteBuf buf) {
    }

    public PatternEncoderEncodeC2SPacket() {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public static void handle(PatternEncoderEncodeC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;

            if (containerMenu instanceof PatternEncoderMenu) {
                PatternEncoderMenu container = (PatternEncoderMenu) containerMenu;
                ((PatternEncoderEntity) container.getEntity()).encodePattern();
            }
        });
    }
}
