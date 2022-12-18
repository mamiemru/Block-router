package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.util.StringRepresentable;

public enum WhiteBlackList implements StringRepresentable {
    WHITELIST("Whitelist"),
    BLACKLIST("Blacklist");

    private final String name;

    private WhiteBlackList(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public static WhiteBlackList nextMode(Comparable sortMode) {
        if (sortMode == WHITELIST) {
            return BLACKLIST;
        }
        return WHITELIST;
    }

    public static int toIndex(Comparable sortMode) {
        if (sortMode == WHITELIST) {
            return 0;
        }
        return 1;
    }

    public static WhiteBlackList fromIndex(int index) {
        return switch (index) {
            case 1 -> BLACKLIST;
            default -> WHITELIST;
        };
    }

}