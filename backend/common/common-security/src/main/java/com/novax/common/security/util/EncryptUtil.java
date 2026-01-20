package com.novax.common.security.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 加密解密工具类
 */
@Slf4j
@Component
public class EncryptUtil {

    @Value("${security.encrypt.key:nova-x-encrypt-key-16byte-length}")
    private String encryptKey;

    /**
     * AES 加密
     *
     * @param plainText 明文
     * @return 密文（Base64编码）
     */
    public String encrypt(String plainText) {
        try {
            AES aes = SecureUtil.aes(encryptKey.getBytes(StandardCharsets.UTF_8));
            byte[] encrypted = aes.encrypt(plainText);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Failed to encrypt data", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * AES 解密
     *
     * @param cipherText 密文（Base64编码）
     * @return 明文
     */
    public String decrypt(String cipherText) {
        try {
            AES aes = SecureUtil.aes(encryptKey.getBytes(StandardCharsets.UTF_8));
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = aes.decrypt(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Failed to decrypt data", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }

    /**
     * MD5 哈希
     *
     * @param text 原文
     * @return MD5值
     */
    public static String md5(String text) {
        return SecureUtil.md5(text);
    }

    /**
     * SHA256 哈希
     *
     * @param text 原文
     * @return SHA256值
     */
    public static String sha256(String text) {
        return SecureUtil.sha256(text);
    }

    /**
     * Base64 编码
     *
     * @param text 原文
     * @return Base64编码
     */
    public static String base64Encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 解码
     *
     * @param encodedText Base64编码
     * @return 原文
     */
    public static String base64Decode(String encodedText) {
        byte[] decoded = Base64.getDecoder().decode(encodedText);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}
