package service.authentication;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.codec.binary.Hex;

@Named
@RequestScoped
public class PasswordHandler {

    /*
     * String password = "pass"; String salt = "1234";
     */

    /***
     * Simple password hashing using the  PBKDF2WithHmacSHA512 algorithm.
     * Ensure to call this method with the exact same params, when comparission needed.
     * 
     * @param password
     *            the field which need to be stored
     * @param salt
     *            some random value used to make the hashing more safe
     * @return
     * @throws Exception
     */
    public static String createPasswordHash(String password, String salt) throws Exception {
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = salt.getBytes();

        byte[] hashedBytes = hashPassword(passwordChars, saltBytes);
        return Hex.encodeHexString(hashedBytes);
    }


    private static byte[] hashPassword(final char[] password, final byte[] salt) throws Exception {
        int iterations = 10000;
        int keyLength = 512;
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
