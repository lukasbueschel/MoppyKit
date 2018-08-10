package moppydesk.kit;

/**
 * @author Lukas
 */
public class RecordTone {

    public final long start;
    public final long end;
    public final Tone tone;

    public RecordTone(long start, long end, Tone tone) {
        this.start = start;
        this.end = end;
        this.tone = tone;
    }
}
