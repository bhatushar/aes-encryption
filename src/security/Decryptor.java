package security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 * Class used to decrypt an AES/CBC encrypted file with PKCS 5 padding
 */
public class Decryptor {
    /**
     * Path to the encrypted file
     */
    private String input;
    /**
     * Path to the decrypted file
     */
    private String output;
    /**
     * Cipher used for decryption
     */
    private Cipher cipher;

    /**
     * Initializes the {@link Decryptor#output} property and {@link Decryptor#cipher} object
     *
     * @param path Path to encrypted file
     * @param password Password provided by user
     */
    public Decryptor(String path, String password) {
        this.input = path;

        // The name of the decrypted file will be same as source file but with the prefix "de_"
        File f = new File(path);
        this.output = f.getParent() + File.separator + "de_" + f.getName();

        try {
            // Initialize cipher with decryption key and IV
            SecretKeySpec key = Common.buildKey(password);
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.cipher.init(Cipher.DECRYPT_MODE, key, Common.buildIv(cipher.getBlockSize(), this.input, true));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Decrypts the source file
     */
    public void decrypt() {
        try {
            // Create input and output streams
            System.out.println("Loading files");
            FileInputStream in = new FileInputStream(this.input);
            FileOutputStream out = new FileOutputStream(this.output);

            // Skip IV values
            in.getChannel().position(cipher.getBlockSize());

            /*
            Using CipherInputStream object to read from encrypted file
            cipherIn reads data from input stream and and returns the decrypted value
            */
            System.out.println("Creating cipher stream");
            CipherInputStream cipherIn = new CipherInputStream(in, this.cipher);

            System.out.println("Starting file decryption");
            byte[] buffer = new byte[Common.BLOCK_SIZE];
            int len;
            // Reading from the input stream
            while ((len = cipherIn.read(buffer)) != -1)
                // Write the decrypted message to output stream
                out.write(buffer, 0, len);

            // Close all streams
            System.out.println("Closing file streams");
            cipherIn.close();
            out.flush();
            out.close();

            System.out.println("Decryption complete");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
