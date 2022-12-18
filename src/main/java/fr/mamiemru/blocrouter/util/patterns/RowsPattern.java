package fr.mamiemru.blocrouter.util.patterns;

import java.util.List;

public abstract class RowsPattern extends Pattern {

    private List<PatternRow> rows;

    public RowsPattern(List<PatternRow> rows, String uuid) {
        super(uuid);
        this.rows = rows;
    }

    public List<PatternRow> getRows() {
        return rows;
    }
}
