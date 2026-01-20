package com.novax.drvpp.service;

import com.novax.drvpp.dto.CreateEventDTO;
import com.novax.drvpp.entity.DemandResponseEvent;
import com.novax.drvpp.entity.VppDevice;

import java.util.List;

public interface DrVppService {

    DemandResponseEvent createEvent(CreateEventDTO dto);

    Boolean startEvent(Long eventId);

    Boolean completeEvent(Long eventId);

    List<VppDevice> getAvailableDevices();

    List<DemandResponseEvent> getActiveEvents();

    DemandResponseEvent getEventById(Long eventId);
}
