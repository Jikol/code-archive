// Simple modified implementation of https://github.com/skdev/PBKDF2WithHmacSHA512-Java
package com.domainlayer.module;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class PasswordEncryption {
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_LENGTH = 64;

    private static byte[] salt = null;

    private PasswordEncryption() {
        throw new AssertionError();
    }

    public static String GetHashedPassword(final String password) {
        SetSalt(password);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        String hashedPassword = null;
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hashedBytePassword = secretKeyFactory.generateSecret(keySpec).getEncoded();
            hashedPassword = Base64.getEncoder().encodeToString(hashedBytePassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        return hashedPassword;
    }

    private static void SetSalt(final String input) {
        StringBuilder modifiedInput = new StringBuilder();
        char[] charArray = input.toCharArray();
        for (int i = 1; i < charArray.length - 1; i++) {
            if (charArray[i - 1] >= 'a' && charArray[i - 1] <= 'z') {
                modifiedInput.append((char) ((double) charArray[i - 1] - 32));
            } else {
                modifiedInput.append((char) ((double) charArray[i - 1] + 32));
            }
            if (charArray[i + 1] >= 'a' && charArray[i + 1] <= 'z') {
                modifiedInput.append((char) ((double) charArray[i + 1] - 32));
            } else {
                modifiedInput.append((char) ((double) charArray[i + 1] + 32));
            }
        }
        salt = StringToByte(modifiedInput.toString());
    }

    public static boolean AuthenticatePassword(final String originalPassword, final String hashedPassword) {
        return Arrays.equals(StringToByte(GetHashedPassword(originalPassword)), StringToByte(hashedPassword));
    }

    private static byte[] StringToByte(final String string) {
        return Base64.getDecoder().decode(string);
    }
}
