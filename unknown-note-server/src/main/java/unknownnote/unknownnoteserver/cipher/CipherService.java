package unknownnote.unknownnoteserver.cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CipherService {

    @Value("${cipher.algorithm}")
    private String ALGORITHM;

    @Value("${cipher.secret-key}")
    private String SECRET_KEY;

    // 암호화 메서드
    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM); // 알고리즘 인스턴스
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM)); // 암호화 모드 설정 후, 시크릿 키 적용
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)); // 암호화
        return Base64.getEncoder().encodeToString(encrypted); // 암호화된 데이터를 Base64 인코딩하여 문자열로 반환
    }

    // 복호화 메서드
    public String decrypt(String encryptedData) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedData); // 암호화된 데이터를 Base64 디코딩
        Cipher cipher = Cipher.getInstance(ALGORITHM); // 암호화 알고리즘 인스턴스
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM)); // 복호화 모드를 설정하고 비밀 키를 적용
        return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8); // 데이터를 복호화하고, 결과를 문자열로 반환
    }
}
