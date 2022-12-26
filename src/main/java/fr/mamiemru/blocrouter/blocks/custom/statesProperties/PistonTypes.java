package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public enum PistonTypes implements StringRepresentable {
    EMPTY("empty"),
    IRON("iron"),
    DIAMOND("diamond"),
    NETHERITE("netherite"),
    DIAMONDIUM("diamondium");

    private final String name;

    private PistonTypes(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }


}