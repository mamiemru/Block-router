package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPiezoelectricPiston extends Item {

    private int efficiency;
    public ItemPiezoelectricPiston(int efficiency) {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));
        this.efficiency = efficiency;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        String coordinates = "efficiency x"+getEfficiency();
        pTooltipComponents.add(Component.literal(coordinates));
        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    public int getEfficiency() {
        return efficiency;
    }
}
