package fr.mamiemru.blocrouter.network.packet;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PatternEncoderChangeC2SPacket {

    public static final String[] patternEncoderList = {
            "Pattern Encoder", "Ender Pattern Encoder", "Transfer Pattern Encoder",
            "Dispatcher Pattern Encoder", "Slot Pattern Encoder", "Vacuum Pattern Encoder"
    };

    private int patternEncoderIndex;

    public PatternEncoderChangeC2SPacket(FriendlyByteBuf buf) {
        this.patternEncoderIndex = buf.readInt();
    }

    public PatternEncoderChangeC2SPacket(int patternEncoderIndex) {
        this.patternEncoderIndex = patternEncoderIndex;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.patternEncoderIndex);
    }

    public static void handle(PatternEncoderChangeC2SPacket message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = context.getSender();
            AbstractContainerMenu containerMenu = serverPlayer.containerMenu;
            if (containerMenu instanceof BaseContainerMenuPatternEncoder container) {
                BlockState blockState = switch (message.patternEncoderIndex) {
                    case 1 -> BlocksRegistry.BLOCK_ENDER_PATTERN_ENCODER.get().defaultBlockState();
                    case 2 -> BlocksRegistry.BLOCK_TRANSFER_PATTERN_ENCODER.get().defaultBlockState();
                    case 3 -> BlocksRegistry.BLOCK_PATTERN_DISPATCHER_ENCODER.get().defaultBlockState();
                    case 4 -> BlocksRegistry.BLOCK_PATTERN_SLOT_ENCODER.get().defaultBlockState();
                    case 5 -> BlocksRegistry.BLOCK_PATTERN_VACUUM_ENCODER.get().defaultBlockState();
                    default -> BlocksRegistry.BLOCK_PATTERN_ENCODER.get().defaultBlockState();
                };

                serverPlayer.doCloseContainer();
                serverPlayer.closeContainer();
                serverPlayer.getLevel().setBlock(container.getEntity().getBlockPos(), blockState, 1 | 2 | 8 | 64);
            }
        });
    }
}
