package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FakeItemInsertionC2SPacket {

    private int menuId;
    private int index;
    private ItemStack itemStack;

    public FakeItemInsertionC2SPacket(FriendlyByteBuf buf) {
        this.menuId = buf.readInt();
        this.index = buf.readInt();
        this.itemStack = buf.readItem();
    }

    public FakeItemInsertionC2SPacket(int menuId, int index, ItemStack itemStack) {
        this.menuId = menuId;
        this.index = index;
        this.itemStack = itemStack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.menuId);
        buf.writeInt(this.index);
        buf.writeItemStack(itemStack, false);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null && serverPlayer.containerMenu != null && serverPlayer.containerMenu.containerId == this.menuId) {
                if (serverPlayer.containerMenu instanceof ItemFilterMenu itemFilterMenu) {
                    itemFilterMenu.setItemAt(this.index, this.itemStack);
                } else if (serverPlayer.containerMenu instanceof BaseContainerMenuPatternEncoder baseContainerMenuPatternEncoder) {
                    baseContainerMenuPatternEncoder.setItemAt(this.index, this.itemStack);
                }
            }
        });
        return true;
    }
}
