package fr.mamiemru.blocrouter.events;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.render.BufferEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = BlocRouter.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(EntitiesRegistry.BUFFER_ENTITY.get(),
                    BufferEntityRenderer::new);
        }
    }
}