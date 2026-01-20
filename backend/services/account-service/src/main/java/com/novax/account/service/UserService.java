package com.novax.account.service;

import com.novax.account.domain.dto.UserRegisterDTO;
import com.novax.account.domain.vo.UserVO;
import com.novax.common.core.page.PageQuery;
import com.novax.common.core.page.PageResult;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    Long register(UserRegisterDTO registerDTO);

    /**
     * 根据ID获取用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 根据手机号获取用户
     */
    UserVO getUserByPhone(String phone);

    /**
     * 更新用户信息
     */
    void updateUser(Long userId, UserVO userVO);

    /**
     * 实名认证
     */
    void verifyRealName(Long userId, String realName, String idCard);

    /**
     * 分页查询用户列表
     */
    PageResult<UserVO> getUserList(PageQuery pageQuery);

    /**
     * 禁用用户
     */
    void disableUser(Long userId);

    /**
     * 启用用户
     */
    void enableUser(Long userId);
}
