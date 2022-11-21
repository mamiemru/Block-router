package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.PatternEncoderEntity;
import fr.mamiemru.blocrouter.util.Pattern;
import fr.mamiemru.blocrouter.util.PatternRow;
import fr.mamiemru.blocrouter.util.PatternUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ItemRoutingPattern extends Item {

    public static final String NBT_UUID = ".uuid";

    public ItemRoutingPattern() {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));

    }

    public static void encodePatternTag(ItemStack is, ListTag ingredients) {
        CompoundTag nbtData = new CompoundTag();
        int ingredientsIndex = 0;
        for (Tag tag : ingredients) {
            nbtData.put(String.valueOf(ingredientsIndex++), tag);
        }
        nbtData.putString(ItemRoutingPattern.getNbtUuid(), UUID.randomUUID().toString());
        is.setTag(nbtData);
    }

    public static Pattern decodePatternTag(@NotNull ItemStack is) {
        if (is.hasTag()) {
            return PatternUtil.decodePatternIngredient(is.getTag());
        }
        return null;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUseHand) {
        ItemStack is = player.getItemInHand(pUseHand);
        if (is.hasTag() && player.isCrouching()) {
            is.setTag(new CompoundTag());
        }
        return super.use(level, player, pUseHand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            pTooltipComponents.add(Component.literal("Ingredients"));
            Pattern pattern = decodePatternTag(pStack);
            for (PatternRow row : pattern.getRows()) {
                MutableComponent str = Component.empty();
                str.append("Side ");
                str.append(PatternUtil.axeIdToString(row.axe));
                str.append(" ");
                str.append(row.is.getItem()+" x"+row.is.getCount());
                pTooltipComponents.add(str);
            }
        }

        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    public static final String getNbtUuid() {
        return BlocRouter.MOD_ID+NBT_UUID;
    }
}
