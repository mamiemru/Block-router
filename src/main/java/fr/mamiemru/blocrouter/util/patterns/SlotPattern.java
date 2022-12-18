package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.core.BlockPos;

import java.util.List;

public class SlotPattern extends NormalRoutingPattern {

    private BlockPos target;

    public SlotPattern(List<PatternRow> rows, BlockPos target, String uuid) {
        super(rows, uuid);
        this.target = target;
    }

    public BlockPos getTarget() {
        return target;
    }
}
