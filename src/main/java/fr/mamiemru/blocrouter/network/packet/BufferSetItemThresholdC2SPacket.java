package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.entities.custom.BufferEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BufferSetItemThresholdC2SPacket {

    public BlockPos pos;
    public int threshold;

    public BufferSetItemThresholdC2SPacket(FriendlyByteBuf buf) {
        this.threshold = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public BufferSetItemThresholdC2SPacket(BlockPos pos, int threshold) {
        this.threshold = threshold;
        this.pos = pos;
        System.out.println(threshold+" "+pos.toShortString());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.threshold);
        buf.writeBlockPos(this.pos);
    }

    public static void handle(BufferSetItemThresholdC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            ServerLevel serverLevel = serverPlayer.getLevel();
            BlockEntity serverEntity = serverLevel.getBlockEntity(message.pos);

            if (serverEntity != null && serverEntity instanceof BufferEntity) {
                ((BufferEntity) serverEntity).setThreshold(message.threshold);
            }
        });
    }
}
