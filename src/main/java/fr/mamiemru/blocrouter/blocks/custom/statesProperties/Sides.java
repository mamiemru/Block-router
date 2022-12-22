package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum Sides implements StringRepresentable {
    NORTH("North"),
    SOUTH("South"),
    EAST("East"),
    WEST("West"),
    UP("Up"),
    DOWN("Down");

    private final String name;

    public static final int NUMBER_OF_SIDES = 6;

    private Sides(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }

    public static Sides fromIndex(int index) {
        return switch (index) {
            case 2 -> SOUTH;
            case 3 -> EAST;
            case 4 -> WEST;
            case 0 -> UP;
            case 5 -> DOWN;
            case 1 -> NORTH;
            default -> null;
        };
    }

}