package com.novax.common.security.util;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * 签名工具类
 */
@Slf4j
@Component
public class SignatureUtil {

    /**
     * 生成签名
     *
     * @param params 参数
     * @param secret 密钥
     * @return 签名
     */
    public static String sign(Map<String, String> params, String secret) {
        // 参数排序
        TreeMap<String, String> sortedParams = new TreeMap<>(params);

        // 拼接参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        // 添加密钥
        sb.append("key=").append(secret);

        // 计算签名
        String signStr = sb.toString();
        log.debug("Sign string: {}", signStr);

        return SecureUtil.md5(signStr).toUpperCase();
    }

    /**
     * 验证签名
     *
     * @param params 参数
     * @param signature 签名
     * @param secret 密钥
     * @return 是否有效
     */
    public static boolean verify(Map<String, String> params, String signature, String secret) {
        // 生成签名
        String calculatedSignature = sign(params, secret);

        // 比较签名
        return calculatedSignature.equals(signature);
    }

    /**
     * 验证签名（带时间窗口检查）
     *
     * @param params 参数
     * @param signature 签名
     * @param secret 密钥
     * @param windowSeconds 时间窗口（秒）
     * @return 是否有效
     */
    public static boolean verifyWithTimeWindow(Map<String, String> params, String signature,
                                               String secret, long windowSeconds) {
        // 检查时间戳
        String timestamp = params.get("timestamp");
        if (timestamp == null) {
            log.warn("Missing timestamp in signature verification");
            return false;
        }

        try {
            long requestTime = Long.parseLong(timestamp);
            long currentTime = System.currentTimeMillis() / 1000;

            // 检查时间窗口（默认 5 分钟）
            if (Math.abs(currentTime - requestTime) > windowSeconds) {
                log.warn("Request timestamp out of window: {} vs {}", requestTime, currentTime);
                return false;
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid timestamp format: {}", timestamp);
            return false;
        }

        // 验证签名
        return verify(params, signature, secret);
    }
}
