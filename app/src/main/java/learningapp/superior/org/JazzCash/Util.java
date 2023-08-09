package learningapp.superior.org.JazzCash;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.facebook.appevents.internal.AppEventUtility.bytesToHex;

public class Util {
    public static String getHMACSha1(byte[] value, byte[] key) throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA256";
        SecretKeySpec secret = new SecretKeySpec(key, type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value);
        return bytesToHex(bytes);
    }


}
