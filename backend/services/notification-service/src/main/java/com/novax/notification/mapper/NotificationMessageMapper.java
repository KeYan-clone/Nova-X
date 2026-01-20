package com.novax.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.notification.entity.NotificationMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMessageMapper extends BaseMapper<NotificationMessage> {
}
