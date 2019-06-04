package com.jsharpe.plantlive.consume;

import com.jsharpe.plantlive.exceptions.IllegalPasswordException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class PasswordHasher {

    public static String hash(String password) throws IllegalPasswordException {
        if (StringUtils.isBlank(password)) {
            throw new IllegalPasswordException();
        }
        return DigestUtils.sha512Hex(password);
    }

    public static boolean verify(String hashed, String candidate) {
        try {
            return PasswordHasher.hash(candidate).equals(hashed);
        } catch (IllegalPasswordException e) {
            // A blank password is not allowed to match anything
            // TODO Maybe think this through a bit more.
            return false;
        }
    }

}
