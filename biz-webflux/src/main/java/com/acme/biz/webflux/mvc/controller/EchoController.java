package com.acme.biz.webflux.mvc.controller;

import com.acme.biz.api.ApiResponse;
import com.acme.biz.api.model.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("echo")
public class EchoController {



    @PostMapping("/user")
    public ApiResponse<String> echo(@RequestBody User user) {
        return ApiResponse.ok(user.getName());
    }

}
