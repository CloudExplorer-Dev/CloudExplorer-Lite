package com.fit2cloud.common.utils;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Company: FIT2CLOUD 飞致云
 * @Author: 张少虎
 * @Date: Created in 5:57 PM 2022/4/10
 * @Description: Please Write notes scientifically
 */
public class RSAUtil {

    private static String RSA_KEY = null;

    private static String RUB_KEY = null;


    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    @SneakyThrows
    public static KeyValue<String, String> genKeyPair() {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(2048, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // key是表示公钥  value 私钥
        KeyValue<String, String> keyValue = new DefaultKeyValue<>(publicKeyString, privateKeyString);
        return keyValue;
    }


    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    @SneakyThrows
    public static String encrypt(String str, String publicKey) {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    @SneakyThrows
    public static String decrypt(String str, String privateKey) {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static String getLocalRSA() {
        File file = new File("/opt/cloudexplorer/conf/RSA");
        if (file.exists()) {
            String text = FileUtils.txt2String(file);
            if (StringUtils.isNotBlank(text)) {
                return text;
            }
        }
        throw new RuntimeException("RSA文件不存在");
    }

    @SneakyThrows
    public static void createLocalRSA(String text) {
        File file = new File("/opt/cloudexplorer/conf/RSA");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
    }

    public static String getLocalPUB() {
        File file = new File("/opt/cloudexplorer/conf/RSA.PUB");
        if (file.exists()) {
            String text = FileUtils.txt2String(file);
            if (StringUtils.isNotBlank(text)) {
                return text;
            }
        }
        throw new RuntimeException("RSA.PUB文件不存在");
    }

    @SneakyThrows
    public static void createLocalPUB(String text) {
        File file = new File("/opt/cloudexplorer/conf/RSA.PUB");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static KeyValue<String, String> getKeyPair() {
        return getKeyPair(false);
    }

    @SneakyThrows
    public static KeyValue<String, String> getKeyPair(boolean createLocalFile) {
        if (StringUtils.isNotBlank(RSA_KEY) && StringUtils.isNotBlank(RUB_KEY)) {
            return new DefaultKeyValue<>(RUB_KEY, RSA_KEY);
        }

        String rsaStr = null, pubStr = null;
        try {
            rsaStr = getLocalRSA();
            pubStr = getLocalPUB();
        } catch (Exception e) {
            if (!createLocalFile) {
                throw e;
            }
            KeyValue<String, String> keyPair = genKeyPair();
            pubStr = keyPair.getKey();
            rsaStr = keyPair.getValue();

            createLocalRSA(rsaStr);
            createLocalPUB(pubStr);
        }

        RSA_KEY = rsaStr;
        RUB_KEY = pubStr;

        return new DefaultKeyValue<>(pubStr, rsaStr);
    }

}
