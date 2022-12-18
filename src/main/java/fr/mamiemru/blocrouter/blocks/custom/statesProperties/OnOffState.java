package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.util.StringRepresentable;

public enum OnOffState implements StringRepresentable {
    RUNNING("Machine is running"),
    OFF("Machine is off");

    private final String name;

    private OnOffState(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public static OnOffState nextMode(Comparable sortMode) {
        if (sortMode == RUNNING) {
            return OFF;
        }
        return RUNNING;
    }

    public static boolean toIndex(Comparable sortMode) {
        return sortMode == RUNNING;
    }

    public static OnOffState fromIndex(boolean b) {
        return (b)? RUNNING : OFF;
    }

}