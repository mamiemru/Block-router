package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

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

    public static int toIndex(Comparable i) {
        if (i == NORTH) { return 0;}
        else if (i == SOUTH) { return 1;}
        else if (i == EAST) { return 2;}
        else if (i == WEST) { return 3;}
        else if (i == UP) { return 4;}
        return 5;
    }

    public static Sides fromIndex(int index) {
        return switch (index) {
            case 1 -> SOUTH;
            case 2 -> EAST;
            case 3 -> WEST;
            case 4 -> UP;
            case 5 -> DOWN;
            default -> NORTH;
        };
    }

}