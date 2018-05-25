package service.impl;

import service.SecurityService;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Михаил on 06.01.2017.
 */

public class SecurityServiceImpl implements SecurityService {

    private static String salt = "LongStringForExtraSecurity@#$!%^&*(*)1234567890";

    @Override
    public String encrypt(String password){
        String generatedPassword = null;
        final Charset charset = Charset.forName("UTF-8");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest((password + salt).getBytes(charset));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public boolean validate(String password, String hash){
        return hash.equals(encrypt(password));
    }
}
