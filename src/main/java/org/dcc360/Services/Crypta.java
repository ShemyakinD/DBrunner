//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360.Services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Crypta {
    private static SecretKey secretKey;
    private static byte[] key;

    public Crypta() {
    }

    private static void setKey() {
        try {
            String myKey = "Ti_4e_ne_smotri2312";
            key = myKey.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = Arrays.copyOf(sha.digest(key), 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Loggator.commonLog(Level.SEVERE,"Ошибка создания ключа шифрования " + ex.getMessage());
        }

    }

    public static String encrypt(String str) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            Loggator.commonLog(Level.SEVERE,"Ошибка кодирования " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String str) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(str)));
        } catch (Exception e) {
            Loggator.commonLog(Level.SEVERE,"Ошибка декодирования " + e.getMessage());
            return null;
        }
    }
}
