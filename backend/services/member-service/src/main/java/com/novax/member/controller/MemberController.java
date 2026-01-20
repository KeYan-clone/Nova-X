package com.novax.member.controller;

import com.novax.common.core.result.Result;
import com.novax.common.log.annotation.Log;
import com.novax.member.dto.RechargeDTO;
import com.novax.member.service.MemberService;
import com.novax.member.vo.MemberInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 会员控制器
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Tag(name = "会员管理", description = "会员相关接口")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "开通会员")
    @PostMapping("/open")
    @Log(title = "开通会员", businessType = "INSERT")
    public Result<MemberInfoVO> openMembership(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        MemberInfoVO member = memberService.openMembership(userId);
        return Result.success(member);
    }

    @Operation(summary = "查询会员信息")
    @GetMapping("/user/{userId}")
    public Result<MemberInfoVO> getMemberByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        MemberInfoVO member = memberService.getMemberByUserId(userId);
        return Result.success(member);
    }

    @Operation(summary = "余额充值")
    @PostMapping("/recharge")
    @Log(title = "余额充值", businessType = "UPDATE")
    public Result<Boolean> recharge(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Valid @RequestBody RechargeDTO rechargeDTO) {
        Boolean result = memberService.recharge(userId, rechargeDTO);
        return Result.success(result);
    }

    @Operation(summary = "查询积分余额")
    @GetMapping("/points/{userId}")
    public Result<Integer> getPoints(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        MemberInfoVO member = memberService.getMemberByUserId(userId);
        return Result.success(member.getCurrentPoints());
    }

    @Operation(summary = "查询账户余额")
    @GetMapping("/balance/{userId}")
    public Result<Object> getBalance(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        MemberInfoVO member = memberService.getMemberByUserId(userId);
        return Result.success(member.getBalance());
    }
}
