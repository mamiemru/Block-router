package fr.mamiemru.blocrouter.util;

import fr.mamiemru.blocrouter.entities.custom.PatternEncoderEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class Pattern {

    private List<PatternRow> rows;
    private String uuid;

    public Pattern(List<PatternRow> rows, String uuid) {
        this.rows = rows;
        this.uuid = uuid;
    }

    public void printToClientChat() {

        Minecraft.getInstance().player.displayClientMessage(Component.literal("uuid: " + uuid), false);
        for (PatternRow pat : rows) {
            StringBuilder s = new StringBuilder();
            s.append("Slot:" + pat.slot);
            s.append(" Side:" + PatternUtil.LIST_OF_AXES[pat.axe]);
            s.append(" "+pat.is.getItem().getDescriptionId() + "x" + pat.is.getCount());
            Minecraft.getInstance().player.displayClientMessage(Component.literal(s.toString()), false);
        }
    }

    public String getUuid(){
        return this.uuid;
    }

    public List<PatternRow> getRows() { return this.rows; }
}
