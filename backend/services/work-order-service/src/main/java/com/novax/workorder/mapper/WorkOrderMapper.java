package com.novax.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novax.workorder.entity.WorkOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
}
