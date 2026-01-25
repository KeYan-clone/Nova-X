package com.novax.account.controller;

import com.novax.account.domain.dto.UserRegisterDTO;
import com.novax.account.domain.vo.UserVO;
import com.novax.account.service.UserService;
import com.novax.common.core.page.PageQuery;
import com.novax.common.core.page.PageResult;
import com.novax.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Long> register(@Validated @RequestBody UserRegisterDTO registerDTO) {
        Long userId = userService.register(registerDTO);
        return Result.success(userId);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    /**
     * 根据手机号获取用户
     */
    @GetMapping("/phone/{phone}")
    public Result<UserVO> getUserByPhone(@PathVariable String phone) {
        UserVO userVO = userService.getUserByPhone(phone);
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     */
    @PatchMapping("/{userId}")
    public Result<Void> updateUser(@PathVariable Long userId, @RequestBody UserVO userVO) {
        userService.updateUser(userId, userVO);
        return Result.success();
    }

    /**
     * 实名认证
     */
    @PostMapping("/{userId}/verify")
    public Result<Void> verifyRealName(
            @PathVariable Long userId,
            @RequestParam String realName,
            @RequestParam String idCard) {
        userService.verifyRealName(userId, realName, idCard);
        return Result.success();
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public Result<PageResult<UserVO>> getUserList(@Validated PageQuery pageQuery) {
        PageResult<UserVO> pageResult = userService.getUserList(pageQuery);
        return Result.success(pageResult);
    }

    /**
     * 禁用用户
     */
    @PostMapping("/{userId}/disable")
    public Result<Void> disableUser(@PathVariable Long userId) {
        userService.disableUser(userId);
        return Result.success();
    }

    /**
     * 启用用户
     */
    @PostMapping("/{userId}/enable")
    public Result<Void> enableUser(@PathVariable Long userId) {
        userService.enableUser(userId);
        return Result.success();
    }

    /**
     * 上传用户头像
     */
    @PostMapping(value = "/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> uploadAvatar(@PathVariable Long userId, @RequestPart("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(userId, file);
        return Result.success(avatarUrl);
    }
}
