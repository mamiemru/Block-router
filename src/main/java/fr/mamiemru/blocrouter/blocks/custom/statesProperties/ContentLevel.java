package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public enum ContentLevel implements StringRepresentable {
    EMPTY("empty"),
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    FULL("full");

    private final String name;
    public static final int MAX_SIZE_INDEX = 4;

    private static final Map<Item, Integer> itemsToIndex = new HashMap<>();

    static {
        itemsToIndex.put(Items.AMETHYST_CLUSTER, 4);
        itemsToIndex.put(Items.LARGE_AMETHYST_BUD, 3);
        itemsToIndex.put(Items.MEDIUM_AMETHYST_BUD, 2);
        itemsToIndex.put(Items.SMALL_AMETHYST_BUD, 1);
    }

    private ContentLevel(String pName) {
        this.name = pName;
    }

    public static Comparable nextLevel(Comparable level) {
        return switch ((ContentLevel) level) {
            case EMPTY -> SMALL;
            case SMALL -> MEDIUM;
            case MEDIUM -> LARGE;
            default -> FULL;
        };
    }

    public static Comparable previousLevel(Comparable level) {
        return switch ((ContentLevel) level) {
            case MEDIUM -> SMALL;
            case LARGE -> MEDIUM;
            case FULL -> LARGE;
            default -> EMPTY;
        };
    }

    public static int toIndex(Comparable level) {
        return switch ((ContentLevel) level) {
            case SMALL -> 1;
            case MEDIUM -> 2;
            case LARGE -> 3;
            case FULL -> 4;
            default -> 0;
        };
    }

    public static ContentLevel fromIndex(int index) {
        return switch (index) {
            case 1 -> SMALL;
            case 2 -> MEDIUM;
            case 3 -> LARGE;
            case 4 -> FULL;
            default -> EMPTY;
        };
    }

    public static int itemToIndex(Item item) {
        return itemsToIndex.getOrDefault(item, 0);
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }


}