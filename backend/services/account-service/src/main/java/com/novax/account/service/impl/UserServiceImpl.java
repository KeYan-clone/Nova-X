package com.novax.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novax.account.domain.dto.UserRegisterDTO;
import com.novax.account.domain.entity.User;
import com.novax.account.domain.vo.UserVO;
import com.novax.account.mapper.UserMapper;
import com.novax.account.service.UserService;
import com.novax.common.core.exception.BusinessException;
import com.novax.common.core.page.PageQuery;
import com.novax.common.core.page.PageResult;
import com.novax.common.core.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(UserRegisterDTO registerDTO) {
        // 检查手机号是否已注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, registerDTO.getPhone());
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCode.RESOURCE_ALREADY_EXISTS, "Phone number already registered");
        }

        // TODO: 验证验证码

        // 创建用户
        User user = new User();
        user.setPhone(registerDTO.getPhone());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : "User_" + registerDTO.getPhone().substring(7));
        user.setStatus(1); // 启用
        user.setVerified(0); // 未认证
        user.setUserType(1); // 普通用户

        userMapper.insert(user);
        log.info("User registered: userId={}, phone={}", user.getId(), user.getPhone());

        return user.getId();
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }

        return convertToVO(user);
    }

    @Override
    public UserVO getUserByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }

        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, UserVO userVO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }

        // 更新允许修改的字段
        if (userVO.getNickname() != null) {
            user.setNickname(userVO.getNickname());
        }
        if (userVO.getAvatar() != null) {
            user.setAvatar(userVO.getAvatar());
        }
        if (userVO.getGender() != null) {
            user.setGender(userVO.getGender());
        }

        userMapper.updateById(user);
        log.info("User updated: userId={}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyRealName(Long userId, String realName, String idCard) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }

        if (user.getVerified() == 1) {
            throw new BusinessException(ResultCode.CONFLICT, "User already verified");
        }

        // TODO: 调用实名认证服务

        user.setRealName(realName);
        user.setIdCard(idCard);
        user.setVerified(1);

        userMapper.updateById(user);
        log.info("User verified: userId={}, realName={}", userId, realName);
    }

    @Override
    public PageResult<UserVO> getUserList(PageQuery pageQuery) {
        Page<User> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        Page<User> resultPage = userMapper.selectPage(page, null);

        List<UserVO> records = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                pageQuery.getPage(),
                pageQuery.getPageSize(),
                resultPage.getTotal(),
                records
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }

        user.setStatus(0);
        userMapper.updateById(user);
        log.info("User disabled: userId={}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.ACCOUNT_NOT_FOUND);
        }

        user.setStatus(1);
        userMapper.updateById(user);
        log.info("User enabled: userId={}", userId);
    }

    /**
     * 转换为 VO（脱敏处理）
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        // 手机号脱敏
        if (user.getPhone() != null) {
            vo.setPhone(user.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }

        // 邮箱脱敏
        if (user.getEmail() != null) {
            vo.setEmail(user.getEmail().replaceAll("(^\\w{1,3}).*(@.*)", "$1***$2"));
        }

        // 不返回密码
        return vo;
    }
}
