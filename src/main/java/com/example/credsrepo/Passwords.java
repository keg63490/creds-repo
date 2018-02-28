//package com.example.credsrepo;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Passwords {
    public static void main(String [] args) throws UnsupportedEncodingException {
        final String password = "Grant Pinkley";
        BytesKeyGenerator generator = KeyGenerators.secureRandom(16);
        final byte[] salt = generator.generateKey();
                  //final String salt = KeyGenerators.string().generateKey();
        final String salt1 = new String(Hex.encode(salt));

        System.out.println(salt1);
        String textToEncrypt = "This is a test!!";

        //Encrypt
        BytesEncryptor encryptor = Encryptors.standard(password, salt1);
        System.out.println("Original text: \"" + textToEncrypt + "\"");
        byte[] encryptedText = encryptor.encrypt(textToEncrypt.getBytes(Charset.forName("UTF-8")));
        String encryptedText1 = new String(Hex.encode(encryptedText));
        System.out.println("Encrypted text: \"" + encryptedText1 + "\"");

        //Decrypt
        BytesEncryptor decryptor = Encryptors.standard(password, salt1);
        byte[] decryptedText = decryptor.decrypt(encryptedText);
        String decryptedText2 = new String(Hex.encode(decryptedText));
        System.out.println("Decrypted text: \"" + decryptedText2 + "\"");

        //Check if it worked
        if(encryptedText1.equals(decryptedText2)) {
            System.out.println("Success: decrypted text matches");
        }
        else {
            System.out.println("Failed: decrypted text does not match");
        }
    }
}


//a61414104d4354ad2c7e3c0b70129bbc420f39e0c728eb3d7c6b5da438bade6a
//8f7711ee8560414b7b47f973afb45da668886ae1d8e0886a8e9f7f7103c4f9999d7563a746dcfd7d67c2caecbd83f500 /6f562bf3b7562180
//6a41e3a83c85f215446fa04ad0a68c1bab588af8a1bc0d82f63e63138466cffc4b03f0f2a70e8f0331ede27aeb4811de /9397dddde8fdcbfd002c14be8efaea27
//d5d92267ccf3da3a61d0cf9e459695317f45a33137289e17f0abcac7d170691af3f3ca31b04b56abbb67084472b3ff65
