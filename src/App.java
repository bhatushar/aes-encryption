import gui.Form;
import gui.RedirectedOutput;
import java.io.PrintStream;

/**
 * <h1>File Encryptor/Decryptor</h1>
 * A Swing GUI application to apply password-based encryption and decryption.
 *
 * @author Tushar Bhatt
 * @version 1.0
 * @since 2018-08-30
 */
class App {
    /**
     * Entry point for the program
     * Method creates a {@link gui.Form} instance, which is the main program window.
     * Also redirects the output buffer to {@link gui.Form#log}
     *
     * @param args Commandline arguments (Not required)
     */
    public static void main(String[] args) {
        Form form = new Form();
        PrintStream printStream = new PrintStream(new RedirectedOutput(form.getLog()));
        System.setOut(printStream);
    }
}
