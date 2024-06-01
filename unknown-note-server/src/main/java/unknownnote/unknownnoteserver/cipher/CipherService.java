package unknownnote.unknownnoteserver.cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.codec.binary.Hex;

@Service
public class CipherService {

    private static String SECRET_KEY = CipherConstants.PRIVATE_KEY;

    // 암호화 메서드
    public String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(SECRET_KEY.substring(0,16).getBytes());
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);
        byte[] encrpytionByte = c.doFinal(data.getBytes("UTF-8"));
        return Hex.encodeHexString(encrpytionByte);

    }

    // 복호화 메서드
    public String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(SECRET_KEY.substring(0,16).getBytes());
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secretKey, IV);
        byte[] decodeByte = Hex.decodeHex(encryptedData.toCharArray());
        return new String(c.doFinal(decodeByte), "UTF-8");

    }
}
