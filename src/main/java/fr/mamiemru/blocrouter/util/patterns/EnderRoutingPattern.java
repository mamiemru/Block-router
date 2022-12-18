package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.core.BlockPos;
import java.util.List;

public class EnderRoutingPattern extends RowsCoordinatesPattern {

    private List<BlockPos> outputs;

    public EnderRoutingPattern(List<PatternRowCoordinates> rows, List<BlockPos> outputs, String uuid) {
        super(rows, uuid);
        this.outputs = outputs;
    }

    public List<BlockPos> getOutputs() {
        return outputs;
    }
}
