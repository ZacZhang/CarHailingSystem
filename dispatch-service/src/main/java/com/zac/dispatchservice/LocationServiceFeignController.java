package com.zac.dispatchservice;

import org.springframework.beans.factory.annotation.Autowired;

public class LocationServiceFeignController {

    @Autowired
    private LocationServiceFeignClient locationServiceFeignClient;
}
