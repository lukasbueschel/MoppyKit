/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moppydesk.kit;

import java.util.ArrayList;
import moppydesk.outputs.MoppyCOMBridge;
import moppydesk.outputs.MoppyPlayerOutput;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Lukas
 */
public class MoppyKitPlayerOutput extends MoppyPlayerOutput {

    private final byte pin;
    private Tone currentTone = null;
    private long toneStart = 0;

    // Aufnahme aufnehmen
    private ArrayList<RecordTone> recordTones = new ArrayList<>();
    private boolean recording = false;
    private long recordStart = 0;

    // Aufnahme abspielen
    private Record record;
    private Timer timer;
    private Timer loopTimer;

    public MoppyKitPlayerOutput(MoppyCOMBridge newMb, int p) {
        super(newMb);
        pin = (byte) p;
    }

    public boolean isPlayingTone() {
        return currentTone != null;
    }

    public Tone getCurrentTone() {
        return currentTone;
    }
    
    public long getToneStart() {
        return toneStart;
    }

    public void playTone(Tone t) {
        if (currentTone == t) {
            return;
        }
        //System.out.println("Play tone " + t.name());
        if (currentTone != null) {
            stopTone();
        }
        currentTone = t;
        toneStart = System.currentTimeMillis();
        int period = currentTone.getPeriod() / (ARDUINO_RESOLUTION * 2);
        mb.sendEvent(pin, period);
    }

    public void stopTone() {
        mb.sendEvent(pin, 0);
        if (recording) {
            long toneEnd = System.currentTimeMillis();
            recordTones.add(createRecordTone(toneStart, toneEnd));
        }
        currentTone = null;
    }

    /* Aufnahme aufnehmen */
    public boolean isRecording() {
        return recording;
    }

    public void startRecord() {
        recordTones.clear();
        recordStart = System.currentTimeMillis();
        if (currentTone != null) {
            toneStart = recordStart;
        }
        recording = true;
    }

    public Record stopRecord() {
        recording = false;
        long recordEnd = System.currentTimeMillis();
        if (currentTone != null) {
            long toneEnd = recordEnd;
            recordTones.add(createRecordTone(toneStart, toneEnd));
        }
        return new Record(recordTones, recordEnd - recordStart);
    }

    private RecordTone createRecordTone(long start, long end) {
        start = start - recordStart;
        end = end - recordStart;
        return new RecordTone(start, end, currentTone);
    }

    /* Aufnahme abspielen */
    public void playRecord(Record record) {
        if (timer != null) {
            timer.cancel();
        }
        this.record = record;
        timer = new Timer();
        //System.out.println("Play record with " + record.tones.size() + " tones");
        for (final RecordTone recordTone : record.tones) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    playTone(recordTone.tone);
                }
            }, recordTone.start);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stopTone();
                }
            }, recordTone.end);
        }
    }

    public void playRecordInLoop(Record record) {
        if (loopTimer != null) {
            loopTimer.cancel();
        }
        this.record = record;
        loopTimer = new Timer();
        loopTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                playRecord(MoppyKitPlayerOutput.this.record);
            }
        }, 0, record.duration);
    }

    public void stopPlayingRecord() {
        if(loopTimer != null) {
            loopTimer.cancel();
        }
        loopTimer = null;
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
        stopTone();
    }

    public static Tone getToneForKey(char k) {
        Tone tone = null;
        switch (k) {
            case 'a':
                tone = Tone.C2;
                break;
            case 'w':
                tone = Tone.CS2;
                break;
            case 's':
                tone = Tone.D2;
                break;
            case 'e':
                tone = Tone.DS2;
                break;
            case 'd':
                tone = Tone.E2;
                break;
            case 'f':
                tone = Tone.F2;
                break;
            case 't':
                tone = Tone.FS2;
                break;
            case 'g':
                tone = Tone.G2;
                break;
            case 'z':
                tone = Tone.GS2;
                break;
            case 'h':
                tone = Tone.A2;
                break;
            case 'u':
                tone = Tone.AS2;
                break;
            case 'j':
                tone = Tone.B2;
                break;
            case 'k':
                tone = Tone.C3;
                break;
            case 'o':
                tone = Tone.CS3;
                break;
            case 'l':
                tone = Tone.D3;
                break;
            case 'p':
                tone = Tone.DS3;
                break;
            case 'ö':
                tone = Tone.E3;
                break;
            case 'ä':
                tone = Tone.F3;
                break;
        }
        return tone;
    }

}
