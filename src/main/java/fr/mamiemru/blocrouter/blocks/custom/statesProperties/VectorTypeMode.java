package fr.mamiemru.blocrouter.blocks.custom.statesProperties;

public enum VectorTypeMode {
    HORIZONTAL,
    VERTICAL,
    DEPTH;

    public static VectorTypeMode fromIndex(int index) {
        return switch (index) {
            case 1 -> VERTICAL;
            case 2 -> DEPTH;
            default -> HORIZONTAL;
        };
    }

    public static int toIndex(VectorTypeMode vectorType) {
        return switch (vectorType) {
            case HORIZONTAL -> 0;
            case VERTICAL -> 1;
            case DEPTH -> 2;
        };
    }
}