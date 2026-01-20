package com.novax.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.workorder.dto.CreateWorkOrderDTO;
import com.novax.workorder.entity.WorkOrder;
import com.novax.workorder.mapper.WorkOrderMapper;
import com.novax.workorder.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper workOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkOrder createWorkOrder(CreateWorkOrderDTO dto) {
        log.info("创建工单, 类型: {}, 设备ID: {}", dto.getOrderType(), dto.getDeviceId());

        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNo(generateOrderNo());
        workOrder.setOrderType(dto.getOrderType());
        workOrder.setDeviceId(dto.getDeviceId());
        workOrder.setDeviceCode("DV20260120" + String.format("%03d", dto.getDeviceId()));
        workOrder.setStationId(1L);
        workOrder.setStationName("市中心充电站");
        workOrder.setFaultDescription(dto.getFaultDescription());
        workOrder.setPriority(dto.getPriority() != null ? dto.getPriority() : "MEDIUM");
        workOrder.setOrderStatus("PENDING");
        workOrder.setReporterId(dto.getReporterId());

        workOrderMapper.insert(workOrder);

        log.info("工单创建成功, 编号: {}", workOrder.getOrderNo());
        return workOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignWorkOrder(Long orderId, Long handlerId) {
        WorkOrder workOrder = workOrderMapper.selectById(orderId);
        if (workOrder == null) {
            throw new BusinessException("工单不存在");
        }

        if (!"PENDING".equals(workOrder.getOrderStatus())) {
            throw new BusinessException("工单状态不是待处理");
        }

        workOrder.setHandlerId(handlerId);
        workOrder.setHandlerName("维修员" + handlerId);
        workOrder.setOrderStatus("ASSIGNED");
        workOrder.setAssignTime(LocalDateTime.now());

        workOrderMapper.updateById(workOrder);
        log.info("工单分配成功, 工单ID: {}, 处理人ID: {}", orderId, handlerId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean startHandle(Long orderId) {
        WorkOrder workOrder = workOrderMapper.selectById(orderId);
        if (workOrder == null) {
            throw new BusinessException("工单不存在");
        }

        workOrder.setOrderStatus("IN_PROGRESS");
        workOrder.setStartTime(LocalDateTime.now());

        workOrderMapper.updateById(workOrder);
        log.info("开始处理工单, 工单ID: {}", orderId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeWorkOrder(Long orderId, String handleResult) {
        WorkOrder workOrder = workOrderMapper.selectById(orderId);
        if (workOrder == null) {
            throw new BusinessException("工单不存在");
        }

        workOrder.setOrderStatus("COMPLETED");
        workOrder.setCompleteTime(LocalDateTime.now());
        workOrder.setHandleResult(handleResult);

        workOrderMapper.updateById(workOrder);
        log.info("工单完成, 工单ID: {}", orderId);
        return true;
    }

    @Override
    public WorkOrder getById(Long orderId) {
        return workOrderMapper.selectById(orderId);
    }

    @Override
    public List<WorkOrder> getPendingOrders() {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkOrder::getOrderStatus, "PENDING")
                .orderByDesc(WorkOrder::getPriority)
                .orderByAsc(WorkOrder::getCreateTime);
        return workOrderMapper.selectList(wrapper);
    }

    @Override
    public List<WorkOrder> getMyOrders(Long handlerId) {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkOrder::getHandlerId, handlerId)
                .orderByDesc(WorkOrder::getCreateTime);
        return workOrderMapper.selectList(wrapper);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%03d", new Random().nextInt(1000));
        return "WO" + timestamp + random;
    }
}
