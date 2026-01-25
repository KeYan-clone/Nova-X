package com.novax.common.log.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novax.common.log.annotation.Log;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面
 * 拦截带有 @Log 注解的方法，记录操作日志
 *
 * @author Nova-X
 * @since 2026-01-25
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    /**
     * 定义切点：所有带有 @Log 注解的方法
     */
    @Pointcut("@annotation(com.novax.common.log.annotation.Log)")
    public void logPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 获取请求信息
        HttpServletRequest request = getRequest();
        String requestUri = request != null ? request.getRequestURI() : "";
        String requestMethod = request != null ? request.getMethod() : "";
        String ip = request != null ? getClientIp(request) : "";

        // 构建日志信息
        Map<String, Object> logData = new HashMap<>();
        logData.put("operationType", logAnnotation.type().name());
        logData.put("operationDesc", logAnnotation.value());
        logData.put("requestUri", requestUri);
        logData.put("requestMethod", requestMethod);
        logData.put("clientIp", ip);
        logData.put("className", joinPoint.getTarget().getClass().getName());
        logData.put("methodName", method.getName());
        logData.put("startTime", LocalDateTime.now());

        // 保存请求参数
        if (logAnnotation.saveParams()) {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                try {
                    logData.put("params", objectMapper.writeValueAsString(args));
                } catch (Exception e) {
                    logData.put("params", "参数序列化失败");
                }
            }
        }

        Object result = null;
        try {
            // 执行方法
            result = joinPoint.proceed();

            // 保存响应结果
            if (logAnnotation.saveResult() && result != null) {
                try {
                    logData.put("result", objectMapper.writeValueAsString(result));
                } catch (Exception e) {
                    logData.put("result", "结果序列化失败");
                }
            }

            logData.put("success", true);
            return result;
        } catch (Throwable e) {
            logData.put("success", false);
            logData.put("errorMsg", e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            logData.put("costTime", endTime - startTime);

            // 记录日志
            try {
                String logJson = objectMapper.writeValueAsString(logData);
                log.info("操作日志: {}", logJson);
            } catch (Exception e) {
                log.error("日志记录失败", e);
            }
        }
    }

    /**
     * 获取请求对象
     */
    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
