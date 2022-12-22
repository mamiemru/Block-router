package fr.mamiemru.blocrouter.gui.screen.screens;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.MobLootSorterMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MobLootSorterScreen extends BaseContainerScreenEnergy<MobLootSorterMenu> {

    public MobLootSorterScreen(MobLootSorterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_mob_loot_sorter_gui.png");
    }
}
