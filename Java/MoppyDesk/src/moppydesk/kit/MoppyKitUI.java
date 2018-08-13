package moppydesk.kit;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.PrintException;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Lukas
 */
public class MoppyKitUI extends javax.swing.JFrame {

    private final MoppyKitPlayerOutput[] outputs;

    private Record record;
    
    String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
    PrintService service = PrintServiceLookup.lookupDefaultPrintService();

    public MoppyKitUI(final MoppyKitPlayerOutput... outputs) {
        this.outputs = outputs;
        initComponents();

        // Wenn eine Taste gedrückt wird, wird der nächste MoppyKitPlayerOutput 
        // gesucht, der aktuell keinen Ton spielt. Dieser spielt dann den Ton.
        // Wird die Taste losgelassen, wird der entsprechende 
        // MoppyKitPlayerOutput benachrichtigt.
        getToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                char key = ((KeyEvent) event).getKeyChar();
                Tone tone = MoppyKitPlayerOutput.getToneForKey(key);
                if (tone == null) {
                    // Keine Taste für einen Ton
                    if (event.getID() == KeyEvent.KEY_PRESSED) {
                        if (key == '1') {
                            outputs[0].startRecord();
                        } else if (key == '2') {
                            record = outputs[0].stopRecord();
                        } else if (key == '3') {
                            outputs[0].playRecord(record);
                        } else if (key == '4') {
                            outputs[0].playRecordInLoop(record);
                        } else if (key == '5') {
                            outputs[0].stopPlayingRecord();
                        } else if (key == 'y') {
                            print( ( "XXXXXXXXXX          XXXXX     XXXXX     XXXXXXXXXX          XXXXX     XXXXX     "
                                   + "     XXXXX     XXXXX          XXXXXXXXXX     XXXXX     XXXXX          XXXXXXXXXX\n" ));
                        } else if (key == 'x') {
                            print(( "XXXXX               X         X         XXXXX               X         X         "
                                  + "         X         X               XXXXX         X         X               XXXXX\n"));
                        } else if (key == 'c') {
                            print(( "XXXXXXXXXX          XXXXX               XXXXX               XXXXX               "
                                  + "               XXXXX               XXXXX               XXXXX          XXXXXXXXXX\n"));
                        } else if (key == 'v') {
                            print(( "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX\n"));
                        } else if (key == 'b') {
                            print(( "XXXXXXXXXX          XXXXX     XXXXX     XXXXX     XXXXX     XXXXX     XXXXX     "
                                  + "     XXXXX     XXXXX     XXXXX     XXXXX     XXXXX     XXXXX          XXXXXXXXXX\n"));
                        } else if (key == 'n') {
                            print(( "XXXXX               X         X         X         X         X         X         "
                                  + "         X         X         X         X         X         X               XXXXX\n"));
                        } else if (key == 'm') {
                            print(( "XXXXXXXXXX                              XXXXXXXXXX                              "
                                  + "                              XXXXXXXXXX                              XXXXXXXXXX\n"));
                        } else if (key == ',') {
                            print(( "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX"
                                  + "XXXXX               X                   X                   X                   "
                                  + "                   X                   X                   X               XXXXX\n"));
                        }
                    }
                } else {
                    int i;
                    switch (event.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            //System.out.println("Key pressed " + key);
                            i = 0;
                            do {
                                if (!outputs[i].isPlayingTone()) {
                                    outputs[i].playTone(tone);
                                    break;
                                }
                                i++;
                            } while (i < outputs.length);
                            // Wenn kein freier Output verfügbar ist, wird der
                            // Ton auf dem MoppyKitPlayerOutput abgespielt, der 
                            // bereits am längsten seinen Ton spielt (FIFO).
                            if (i == outputs.length) {
                                i = 0;
                                for(int j = 0; j < outputs.length; j++) {
                                    if(outputs[j].getToneStart() < outputs[i].getToneStart()) {
                                        i = j;
                                    }
                                }
                                outputs[i].playTone(tone);
                            }
                            break;
                        case KeyEvent.KEY_TYPED:
                            //keyTyped((KeyEvent) event);
                            break;
                        case KeyEvent.KEY_RELEASED:
                            //System.out.println("Key released " + key);
                            i = 0;
                            do {
                                if (outputs[i].getCurrentTone() == tone) {
                                    outputs[i].stopTone();
                                    break;
                                }
                                i++;
                            } while (i < outputs.length);
                            break;
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    private void print(String string){
        System.out.println("DefaultPrinter: " + defaultPrinter);
        
        try {
            InputStream is = new ByteArrayInputStream(string.getBytes("UTF8"));


            PrintRequestAttributeSet  pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(1));

            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(is, flavor, null);
            DocPrintJob job = service.createPrintJob();


            PrintJobWatcher pjw = new PrintJobWatcher(job);

            try {
                job.print(doc, pras);
            } catch (PrintException e) {
                System.out.println("PrintException: " + e);
            }

            pjw.waitForDone();
            try {
                is.close();
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("UnsupportedEncodingException: " + e);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));

        panel.setAlignmentX(0.0F);
        panel.setAlignmentY(0.0F);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
