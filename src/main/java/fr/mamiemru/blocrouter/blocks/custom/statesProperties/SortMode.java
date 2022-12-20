package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.util.StringRepresentable;

public enum SortMode implements StringRepresentable {
    ROUND_ROBIN("round_robin"),
    PRIORITY("priority");

    private final String name;

    private SortMode(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public static SortMode nextMode(Comparable sortMode) {
        if (sortMode == ROUND_ROBIN) {
            return PRIORITY;
        }
        return ROUND_ROBIN;
    }

    public static int toIndex(Comparable sortMode) {
        if (sortMode == ROUND_ROBIN) {
            return 0;
        }
        return 1;
    }

    public static SortMode fromIndex(int index) {
        return switch (index) {
            case 1 -> PRIORITY;
            default -> ROUND_ROBIN;
        };
    }

}