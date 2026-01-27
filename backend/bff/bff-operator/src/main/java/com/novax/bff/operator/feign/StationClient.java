package com.novax.bff.operator.feign;

import com.novax.common.core.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 站点服务客户端
 */
@FeignClient(name = "station-service", path = "/stations")
public interface StationClient {

  @GetMapping
  Result<?> listStations(@RequestParam(required = false) Long operatorId,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "20") Integer size);

  @GetMapping("/{stationId}")
  Result<?> getStation(@PathVariable Long stationId);

  @PostMapping
  Result<?> createStation(@RequestBody Object dto);

  @PutMapping("/{stationId}")
  Result<?> updateStation(@PathVariable Long stationId, @RequestBody Object dto);

  @GetMapping("/{stationId}/devices")
  Result<?> getStationDevices(@PathVariable Long stationId);
}
