package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.entities.custom.BufferEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class BufferRendererS2CPacket {

    private final BlockPos pos;
    private final ItemStack itemStack;

    public BufferRendererS2CPacket(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
        this.pos = buf.readBlockPos();
    }

    public BufferRendererS2CPacket(BlockPos pos, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.pos = pos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BufferEntity blockEntity) {
                blockEntity.setRenderStack(itemStack);
            }
        });
        return true;
    }
}
