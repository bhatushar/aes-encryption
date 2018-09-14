package security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;


/**
 * Encrypts any standard binary/text file using AES/CBC block cipher technique.
 * Using PKCS 5 padding
 */
public class Encryptor {
    /**
     * Path to source file
     */
    private String input;
    /**
     * Path to encrypted file
     */
    private String output;
    /**
     * Encryption cipher
     */
    private Cipher cipher;

    /**
     * Builds the path to encrypted file.
     * Initializes cipher.
     *
     * @param path Path to the source file
     * @param password Password provided by user
     */
    public Encryptor(String path, String password) {
        this.input = path;

        // The encrypted file has the same name as source file but has an "en_" prefix
        File f = new File(path);
        this.output = f.getParent() + File.separator + "en_" + f.getName();

        try {
            // Initialize cipher with key and IV
            SecretKeySpec key = Common.buildKey(password);
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, Common.buildIv(cipher.getBlockSize(), this.output, false));
        } catch (Exception e) {
            System.out.println("Cannot encrypt password: " + e.getMessage());
        }
    }

    /**
     * Encrypts the source file
     */
    public void encrypt() {
        try {
            // Create input and output streams
            System.out.println("Loading files");
            FileInputStream in = new FileInputStream(this.input);
            FileOutputStream out = new FileOutputStream(this.output, true);

            /*
            Using CipherOutputStream object to write to encrypted file.
            cipherOut encrypts the file data and then writes it to output stream
             */
            System.out.println("Creating cipher stream");
            CipherOutputStream cipherOut = new CipherOutputStream(out, this.cipher);

            System.out.println("Starting encryption");
            byte[] buffer = new byte[Common.BLOCK_SIZE];
            int len;
            // Read from source
            while ((len = in.read(buffer)) != -1)
                // Write encrypted data to output
                cipherOut.write(buffer, 0, len);

            // Close all streams
            System.out.println("Closing file streams");
            in.close();
            cipherOut.flush();
            cipherOut.close();

            System.out.println("Encryption Complete");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
