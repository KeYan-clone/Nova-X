package com.novax.billing.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.billing.dto.BillQueryDTO;
import com.novax.billing.entity.Bill;
import com.novax.billing.service.BillService;
import com.novax.billing.vo.BillVO;
import com.novax.common.core.result.Result;
import com.novax.common.log.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 账单控制器
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Tag(name = "账单管理", description = "账单相关接口")
@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @Operation(summary = "根据会话ID生成账单")
    @PostMapping("/generate/{sessionId}")
    @Log(title = "生成账单", businessType = "INSERT")
    public Result<Bill> generateBill(
            @Parameter(description = "会话ID") @PathVariable Long sessionId) {
        Bill bill = billService.generateBillBySession(sessionId);
        return Result.success(bill);
    }

    @Operation(summary = "根据ID查询账单")
    @GetMapping("/{id}")
    public Result<BillVO> getBillById(
            @Parameter(description = "账单ID") @PathVariable Long id) {
        BillVO bill = billService.getBillById(id);
        return Result.success(bill);
    }

    @Operation(summary = "根据账单编号查询账单")
    @GetMapping("/no/{billNo}")
    public Result<BillVO> getBillByBillNo(
            @Parameter(description = "账单编号") @PathVariable String billNo) {
        BillVO bill = billService.getBillByBillNo(billNo);
        return Result.success(bill);
    }

    @Operation(summary = "分页查询账单")
    @PostMapping("/query")
    public Result<Page<BillVO>> queryBills(@Valid @RequestBody BillQueryDTO queryDTO) {
        Page<BillVO> page = billService.queryBills(queryDTO);
        return Result.success(page);
    }

    @Operation(summary = "支付账单")
    @PostMapping("/{id}/pay")
    @Log(title = "支付账单", businessType = "UPDATE")
    public Result<Boolean> payBill(
            @Parameter(description = "账单ID") @PathVariable Long id,
            @Parameter(description = "支付方式") @RequestParam String paymentMethod,
            @Parameter(description = "支付流水号") @RequestParam String transactionNo) {
        Boolean result = billService.payBill(id, paymentMethod, transactionNo);
        return Result.success(result);
    }

    @Operation(summary = "取消账单")
    @PostMapping("/{id}/cancel")
    @Log(title = "取消账单", businessType = "UPDATE")
    public Result<Boolean> cancelBill(
            @Parameter(description = "账单ID") @PathVariable Long id) {
        Boolean result = billService.cancelBill(id);
        return Result.success(result);
    }

    @Operation(summary = "退款")
    @PostMapping("/{id}/refund")
    @Log(title = "退款", businessType = "UPDATE")
    public Result<Boolean> refundBill(
            @Parameter(description = "账单ID") @PathVariable Long id) {
        Boolean result = billService.refundBill(id);
        return Result.success(result);
    }
}
