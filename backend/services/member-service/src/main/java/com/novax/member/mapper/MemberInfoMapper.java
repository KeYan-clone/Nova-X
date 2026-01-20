package com.novax.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.member.entity.MemberInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员信息Mapper
 *
 * @author Nova-X Team
 * @since 2026-01-20
 */
@Mapper
public interface MemberInfoMapper extends BaseMapper<MemberInfo> {
}
