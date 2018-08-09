/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moppydesk.kit;

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
    private char currentKey = 0;

    public MoppyKitPlayerOutput(MoppyCOMBridge newMb, int p) {
        super(newMb);
        pin = (byte) p;
    }
    
    public boolean isPlayingTone() {
        return currentKey != 0;
    }
    
    public char getCurrentKey() {
        return currentKey;
    }

    public void playToneForKey(char key) {
        //System.out.println("Play tone for key " + key);
        currentKey = key;
        int i = getIndexForKey(key);
        int period = microPeriods[i] / (ARDUINO_RESOLUTION * 2);
        mb.sendEvent(pin, period);
    }

    public void stopToneForKey(char key) {        
        //System.out.println("Stop tone for key " + key);
        mb.sendEvent(pin, 0);
        currentKey = 0;
    }
    
    private int getIndexForKey(char key) {
        int i = 0;
        switch (key) {
            case 'a':
                i = 36;
                break;
            case 'w':
                i = 37;
                break;
            case 's':
                i = 38;
                break;
            case 'e':
                i = 39;
                break;
            case 'd':
                i = 40;
                break;
            case 'f':
                i = 41;
                break;
            case 't':
                i = 42;
                break;
            case 'g':
                i = 43;
                break;
            case 'z':
                i = 44;
                break;
            case 'h':
                i = 45;
                break;
            case 'u':
                i = 46;
                break;
            case 'j':
                i = 47;
                break;
            case 'k':
                i = 48;
                break;
            case 'o':
                i = 49;
                break;
            case 'l':
                i = 50;
                break;
            case 'p':
                i = 51;
                break;
            case 'ö':
                i = 52;
                break;
            case 'ä':
                i = 53;
                break;
        }
        return i;
    }
    
}
