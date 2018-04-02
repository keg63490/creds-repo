package com.example.credsrepo;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class Passwords {
    final private static String secret = "Grant Pinkley";

    private Passwords() {
    }

    //generate salt
    public static String genNextSalt() {
            BytesKeyGenerator generator = KeyGenerators.secureRandom(16);
            final byte[] salt = generator.generateKey();
            final String salt1 = new String(Hex.encode(salt));
        return salt1;
    }

    //encrypt
    public static byte[] encrypt(byte[] textToEncrypt, String passSalt) {
        //Encrypt
        byte[] encryptedText = null;
        try {
            BytesEncryptor encryptor = Encryptors.standard(secret, passSalt);
            encryptedText = encryptor.encrypt(textToEncrypt);
        }catch(Exception e) {
            //do stuff
        }
        return encryptedText;

    }

    //decrypt
    public static String decrypt(byte[] encryptedText, String passSalt) {
        String decryptedText3=null;
        try {
            BytesEncryptor decryptor = Encryptors.standard(secret, passSalt);
            //encryptedText = new String(Hex.decode(encryptedText));
            //System.out.println(encryptedText);
            byte[] decryptedText = decryptor.decrypt(encryptedText);  //.getBytes());
            String decryptedText2 = new String(Hex.encode(decryptedText));
            decryptedText3 = new String(Hex.decode(decryptedText2), "UTF-8");
            //System.out.println("Decrypted text: \"" + decryptedText3 + "\"");

        }catch (Exception e) {
            //do stuff

        }
        return decryptedText3;
    }
}
