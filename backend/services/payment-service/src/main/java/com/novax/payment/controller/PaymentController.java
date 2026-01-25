package com.novax.payment.controller;

import com.novax.common.core.result.Result;
import com.novax.common.log.annotation.Log;
import com.novax.payment.dto.PaymentCreateDTO;
import com.novax.payment.dto.RefundApplyDTO;
import com.novax.payment.service.PaymentService;
import com.novax.payment.vo.PaymentResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 支付控制器
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Tag(name = "支付管理", description = "支付相关接口")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "创建支付")
    @PostMapping("/create")
    @Log(value = "创建支付", type = Log.OperationType.INSERT)
    public Result<PaymentResultVO> createPayment(@Valid @RequestBody PaymentCreateDTO createDTO) {
        PaymentResultVO result = paymentService.createPayment(createDTO);
        return Result.success(result);
    }

    @Operation(summary = "查询支付结果")
    @GetMapping("/{paymentNo}")
    public Result<PaymentResultVO> queryPayment(
            @Parameter(description = "支付单号") @PathVariable String paymentNo) {
        PaymentResultVO result = paymentService.queryPayment(paymentNo);
        return Result.success(result);
    }

    @Operation(summary = "关闭支付")
    @PostMapping("/{paymentNo}/close")
    @Log(value = "关闭支付", type = Log.OperationType.UPDATE)
    public Result<Boolean> closePayment(
            @Parameter(description = "支付单号") @PathVariable String paymentNo) {
        Boolean result = paymentService.closePayment(paymentNo);
        return Result.success(result);
    }

    @Operation(summary = "申请退款")
    @PostMapping("/refund")
    @Log(value = "申请退款", type = Log.OperationType.INSERT)
    public Result<Boolean> applyRefund(@Valid @RequestBody RefundApplyDTO applyDTO) {
        Boolean result = paymentService.applyRefund(applyDTO);
        return Result.success(result);
    }

    @Operation(summary = "查询退款结果")
    @GetMapping("/refund/{refundNo}")
    public Result<Object> queryRefund(
            @Parameter(description = "退款单号") @PathVariable String refundNo) {
        Object result = paymentService.queryRefund(refundNo);
        return Result.success(result);
    }
}
