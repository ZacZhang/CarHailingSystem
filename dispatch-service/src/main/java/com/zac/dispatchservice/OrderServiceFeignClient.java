package com.zac.dispatchservice;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "order-service")
public interface OrderServiceFeignClient extends OrderService {
}
