package com.acme.biz.web.client.cloud.controller;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xzm
 * @description
 */
@RestController
public class BizClientFeignController {
    @Autowired
    private UserRegistrationService userRegistrationService;

    @GetMapping("/user/register")
    public Object registerUser() {
        User user = new User();
        user.setId(1L);
        user.setName("ABC");
        return userRegistrationService.registerUser(user);
    }



}