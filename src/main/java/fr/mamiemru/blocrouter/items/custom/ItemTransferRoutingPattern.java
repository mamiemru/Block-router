package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.WhiteBlackList;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.TransferPattern;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ItemTransferRoutingPattern extends ItemRoutingPattern {

    public static void encodePatternTag(ItemStack is, int isExtraction, int isWhiteList, ListTag transferInput,
                                        ListTag transferOutput, ListTag trash, ListTag ingredients
    ) {
        CompoundTag nbtData = new CompoundTag();
        nbtData.putInt("isExtraction", isExtraction);
        nbtData.putInt("isWhiteList", isWhiteList);
        nbtData.put("ingredients", ingredients);
        nbtData.put("trash", trash);
        nbtData.put("transferInput", transferInput);
        nbtData.put("transferOutput", transferOutput);
        nbtData.putString(ItemRoutingPattern.getNbtUuid(), UUID.randomUUID().toString());
        is.setTag(nbtData);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            TransferPattern transferPattern = Pattern.decodeTransferPattern(pStack.getTag());
            pTooltipComponents.add(Component.literal("Extract from " + transferPattern.getTransferInput().toShortString()));
            pTooltipComponents.add(Component.literal(WhiteBlackList.fromIndex(transferPattern.isWhitelist()) + " mode"));

            if (transferPattern.isExtraction() == 0) {
                pTooltipComponents.add(Component.literal("Insert into " + transferPattern.getTransferOutput().get(0).toShortString()));
                for (ItemStack is : transferPattern.getIngredients()) {
                    if (!is.isEmpty()) {
                        pTooltipComponents.add(Component.literal(is.getItem().getDescriptionId()));
                    }
                }
            } else {
                for (int i = 0; i < transferPattern.getTransferOutput().size(); ++i) {
                    pTooltipComponents.add(Component.literal("Insert into " + transferPattern.getTransferOutput().get(i).toShortString() + " items:"));
                    for (int slot = 0; slot < 3; ++slot) {
                        ItemStack is = transferPattern.getIngredients().get((3 * i) + slot);
                        if (!is.isEmpty()) {
                            pTooltipComponents.add(Component.literal(is.getItem().getDescriptionId()));
                        }
                    }
                }
            }

            if (!transferPattern.getTrash().isEmpty()) {
                pTooltipComponents.add(Component.literal("Trash items:"));
                for (ItemStack is : transferPattern.getTrash()) {
                    if (!is.isEmpty()) {
                        pTooltipComponents.add(Component.literal(is.getItem().getDescriptionId()));
                    }
                }
            }

            super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
        }
    }
}
