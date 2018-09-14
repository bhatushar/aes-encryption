package gui;

import javax.swing.JTextArea;
import java.io.OutputStream;

/**
 * Redirects all buffer through OutputStream to a custom component.
 */
public class RedirectedOutput extends OutputStream {
    private JTextArea log;

    /**
     * RedirectOutput constructor
     * @param textArea JTextArea type object which will be used as new output stream.
     */
    public RedirectedOutput(JTextArea textArea) {
        this.log = textArea;
    }

    /**
     * Appends data in logging window
     * @param b byte to be written
     */
    @Override
    public void write(int b) {
        log.append(String.valueOf((char) b));
        log.setCaretPosition(log.getDocument().getLength());
    }
}