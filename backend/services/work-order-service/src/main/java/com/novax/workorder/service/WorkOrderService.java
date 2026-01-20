package com.novax.workorder.service;

import com.novax.workorder.dto.CreateWorkOrderDTO;
import com.novax.workorder.entity.WorkOrder;

import java.util.List;

public interface WorkOrderService {

    WorkOrder createWorkOrder(CreateWorkOrderDTO dto);

    Boolean assignWorkOrder(Long orderId, Long handlerId);

    Boolean startHandle(Long orderId);

    Boolean completeWorkOrder(Long orderId, String handleResult);

    WorkOrder getById(Long orderId);

    List<WorkOrder> getPendingOrders();

    List<WorkOrder> getMyOrders(Long handlerId);
}
