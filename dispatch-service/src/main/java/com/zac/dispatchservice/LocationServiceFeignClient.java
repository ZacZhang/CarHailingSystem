package com.zac.dispatchservice;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "location-service")
public interface LocationServiceFeignClient extends LocationService{
}
