package fr.mamiemru.blocrouter.network;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.network.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return ++packetId;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(BlocRouter.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PatternEncoderEncodeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PatternEncoderEncodeC2SPacket::new)
                .encoder(PatternEncoderEncodeC2SPacket::toBytes)
                .consumerMainThread(PatternEncoderEncodeC2SPacket::handle)
                .add();

        net.messageBuilder(PatternEncoderChangerSideC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PatternEncoderChangerSideC2SPacket::new)
                .encoder(PatternEncoderChangerSideC2SPacket::toBytes)
                .consumerMainThread(PatternEncoderChangerSideC2SPacket::handle)
                .add();

        net.messageBuilder(BufferSetItemThresholdC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BufferSetItemThresholdC2SPacket::new)
                .encoder(BufferSetItemThresholdC2SPacket::toBytes)
                .consumerMainThread(BufferSetItemThresholdC2SPacket::handle)
                .add();

        net.messageBuilder(BufferRendererS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BufferRendererS2CPacket::new)
                .encoder(BufferRendererS2CPacket::toBytes)
                .consumerMainThread(BufferRendererS2CPacket::handle)
                .add();

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumerMainThread(EnergySyncS2CPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
