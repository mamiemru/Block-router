package fr.mamiemru.blocrouter.entities.Container;

import net.minecraft.world.inventory.ContainerData;

import java.util.Arrays;

public class ContainerListData implements ContainerData {

    public int[] array;

    public ContainerListData(int size) {
        this(size, 0);
    }

    public ContainerListData(int size, int n) {
        this.array = new int[size];
        Arrays.fill(this.array, n);
    }

    public ContainerListData(int[] array) {
        this.array = array;
    }

    @Override
    public int get(int pIndex) {
        return array[pIndex];
    }

    @Override
    public void set(int pIndex, int pValue) {
        array[pIndex] = pValue;
    }

    @Override
    public int getCount() {
        return array.length;
    }
}
