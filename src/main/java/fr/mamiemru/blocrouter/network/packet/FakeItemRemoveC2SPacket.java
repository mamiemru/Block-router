package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.PatternEncoderMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FakeItemRemoveC2SPacket {

    private int menuId;
    private int index;

    public FakeItemRemoveC2SPacket(FriendlyByteBuf buf) {
        this.menuId = buf.readInt();
        this.index = buf.readInt();
    }

    public FakeItemRemoveC2SPacket(int menuId, int index) {
        this.menuId = menuId;
        this.index = index;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.menuId);
        buf.writeInt(this.index);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            if (serverPlayer != null && serverPlayer.containerMenu != null && serverPlayer.containerMenu.containerId == this.menuId) {
                if (serverPlayer.containerMenu instanceof ItemFilterMenu itemFilterMenu) {
                    itemFilterMenu.removeItemAt(this.index);
                } else if (serverPlayer.containerMenu instanceof BaseContainerMenuPatternEncoder baseContainerMenuPatternEncoder) {
                    baseContainerMenuPatternEncoder.removeItemAt(this.index);
                }
            }
        });
        return true;
    }
}
