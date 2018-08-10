package moppydesk.kit;

import static moppydesk.outputs.MoppyPlayerOutput.microPeriods;

/**
 * @author Lukas
 */
public enum Tone {

    C1  (microPeriods[24]),
    CS1 (microPeriods[25]),
    D1  (microPeriods[26]),
    DS1 (microPeriods[27]),
    E1  (microPeriods[28]),
    F1  (microPeriods[29]),
    FS1 (microPeriods[30]),
    G1  (microPeriods[31]),
    GS1 (microPeriods[32]),
    A1  (microPeriods[33]),
    AS1 (microPeriods[34]),
    B1  (microPeriods[35]),
    C2  (microPeriods[36]),
    CS2 (microPeriods[37]),
    D2  (microPeriods[38]),
    DS2 (microPeriods[39]),
    E2  (microPeriods[40]),
    F2  (microPeriods[41]),
    FS2 (microPeriods[42]),
    G2  (microPeriods[43]),
    GS2 (microPeriods[44]),
    A2  (microPeriods[45]),
    AS2 (microPeriods[46]),
    B2  (microPeriods[47]),
    C3  (microPeriods[48]),
    CS3 (microPeriods[49]),
    D3  (microPeriods[50]),
    DS3 (microPeriods[51]),
    E3  (microPeriods[52]),
    F3  (microPeriods[53]),
    FS3 (microPeriods[54]),
    G3  (microPeriods[55]),
    GS3 (microPeriods[56]),
    A3  (microPeriods[57]),
    AS3 (microPeriods[58]),
    B3  (microPeriods[59]),
    C4  (microPeriods[60]),
    CS4 (microPeriods[61]),
    D4  (microPeriods[62]),
    DS4 (microPeriods[63]),
    E4  (microPeriods[64]),
    F4  (microPeriods[65]),
    FS4 (microPeriods[66]),
    G4  (microPeriods[67]),
    GS4 (microPeriods[68]),
    A4  (microPeriods[69]),
    AS4 (microPeriods[70]),
    B4  (microPeriods[71]);

    private final int period;

    Tone(int period) {
        this.period = period;
    }
    
    public int getPeriod() {
        return period;
    }
}
