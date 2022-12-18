package fr.mamiemru.blocrouter.util.patterns;

import java.util.List;

public abstract class RowsCoordinatesPattern extends Pattern {

    private List<PatternRowCoordinates> rows;

    public RowsCoordinatesPattern(List<PatternRowCoordinates> rows, String uuid) {
        super(uuid);
        this.rows = rows;
    }

    public List<PatternRowCoordinates> getRows() {
        return rows;
    }
}
