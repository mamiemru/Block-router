package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.util.StringRepresentable;

public enum IOMode implements StringRepresentable {
    EXTRACT("Extraction"),
    INSERT("Insertion");

    private final String name;

    private IOMode(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public static IOMode nextMode(Comparable sortMode) {
        if (sortMode == EXTRACT) {
            return INSERT;
        }
        return EXTRACT;
    }

    public static int toIndex(Comparable sortMode) {
        if (sortMode == EXTRACT) {
            return 0;
        }
        return 1;
    }

    public static IOMode fromIndex(int index) {
        return switch (index) {
            case 1 -> INSERT;
            default -> EXTRACT;
        };
    }

}