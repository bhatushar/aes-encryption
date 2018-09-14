package security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

/**
 * Contains attributes and methods which are common for encryption and decryption.
 */
class Common {
    /**
     * Maximum number of bytes that can be read at once
     */
    final static int BLOCK_SIZE = 1024;

    /**
     * The password provided by the user is converted in a 16 byte key.
     * I am using 128 bit Cipher, it only works on 16 byte long keys.
     *
     * @param str Password string which is used to generate key
     * @return Generated key which is used for encryption/decryption.
     * @throws NoSuchAlgorithmException MessageDigest cannot find an "SHA-1" instance
     */
    static SecretKeySpec buildKey(String str) throws NoSuchAlgorithmException {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            bytes = sha.digest(bytes);
            bytes = Arrays.copyOf(bytes, 16);
            return new SecretKeySpec(bytes, "AES");
    }

    /**
     * Creates and Initialization Vector which can be used in AES Block Cipher
     *
     * @param size Size of the IV byte array
     * @return Initialization vector as an instance of IvParameterVector
     * @throws Exception NoSuchAlgorithmException, FileNotFountException, IOException
     */
    static IvParameterSpec buildIv(int size, String filePath, boolean read) throws Exception {
        byte[] iv = new byte[size];

        if (!read) {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(iv);
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(iv);
            out.close();
        }
        else {
            FileInputStream in = new FileInputStream(filePath);
            if (in.read(iv) != size)
                throw new IOException("Cannot read the IV values");
            in.close();
        }
        return new IvParameterSpec(iv);
    }
}
