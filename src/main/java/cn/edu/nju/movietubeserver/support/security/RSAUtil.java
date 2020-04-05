package cn.edu.nju.movietubeserver.support.security;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author dc
 * @date 2019/12/21 21:43
 *
 * RSA算法工具类
 */
@Component
@Slf4j
public class RSAUtil
{

    private static final String SECRETE_ALGORITHM = "RSA";

    private static final String PUBLIC_KEY_HEAD = "-----BEGIN PUBLIC KEY-----";

    private static final String PUBLIC_KEY_TAIL = "-----END PUBLIC KEY-----";

    private static final String PRIVATE_KEY_HEAD = "-----BEGIN PRIVATE KEY-----";

    private static final String PRIVATE_KEY_TAIL = "-----END PRIVATE KEY-----";

    /**
     * 生成密钥对
     *
     * @param keyLength 密钥长度(最少512位)
     * @return 密钥对 公钥 keyPair.getPublic() 私钥 keyPair.getPrivate()
     * @throws Exception e
     */
    public KeyPair genKeyPair(final int keyLength)
        throws Exception
    {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SECRETE_ALGORITHM);
        keyPairGenerator.initialize(keyLength);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 公钥加密
     *
     * @param content 待加密数据
     * @param publicKey 公钥
     * @return 加密内容
     * @throws Exception e
     */
    public byte[] encrypt(final byte[] content, final PublicKey publicKey)
        throws Exception
    {
        final Cipher cipher = Cipher.getInstance(SECRETE_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 私钥解密
     *
     * @param content 加密数据
     * @param privateKey 私钥
     * @return 解密内容
     * @throws Exception e
     */
    public byte[] decrypt(final byte[] content, final PrivateKey privateKey)
        throws Exception
    {
        final Cipher cipher = Cipher.getInstance(SECRETE_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    private byte[] replaceAndBase64Decode(final String filePath, final String headReplace, final String tailReplace)
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource(filePath);
        try (final InputStream inputStream = resource.getInputStream())
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer)))
            {
                outputStream.write(buffer, 0, n);
            }
            final byte[] keyBytes = outputStream.toByteArray();
            final String keyPEM = new String(keyBytes).replace(headReplace, "").trim().replace(tailReplace, "").trim();
            return Base64.decodeBase64(keyPEM);
        }
    }

    /**
     * 加载pem格式的公钥
     *
     * @param pem 公钥文件名
     * @return 公钥
     */
    public PublicKey loadPemPublicKey(final String pem)
    {
        try
        {
            final byte[] decoded = this.replaceAndBase64Decode(pem, PUBLIC_KEY_HEAD, PUBLIC_KEY_TAIL);
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            final KeyFactory keyFactory = KeyFactory.getInstance(SECRETE_ALGORITHM);
            return keyFactory.generatePublic(spec);
        }
        catch (final Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 加载pem格式PKCS8编码的私钥
     *
     * @param pem 私钥文件名
     * @return 私钥
     */
    public PrivateKey loadPemPrivateKey(final String pem)
    {
        try
        {
            final byte[] decoded = this.replaceAndBase64Decode(pem, PRIVATE_KEY_HEAD, PRIVATE_KEY_TAIL);
            final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            final KeyFactory keyFactory = KeyFactory.getInstance(SECRETE_ALGORITHM);
            return keyFactory.generatePrivate(spec);
        }
        catch (final Exception e)
        {
            log.error(e.getMessage());
        }
        return null;
    }
}
