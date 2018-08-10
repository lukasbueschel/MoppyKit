package moppydesk.kit;

import java.util.ArrayList;
import java.util.Timer;

/**
 * @author Lukas
 */
public class Record {

    public final ArrayList<RecordTone> tones;
    public final long duration;

    public Record(ArrayList<RecordTone> tones, long duration) {
        this.tones = tones;
        this.duration = duration;
    }
}
