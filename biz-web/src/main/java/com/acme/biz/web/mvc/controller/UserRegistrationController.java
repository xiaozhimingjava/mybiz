package com.acme.biz.web.mvc.controller;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class UserRegistrationController implements UserRegistrationService {


    @Override
    @ResponseBody
    public Boolean registerUser(User user) {
        return true;
    }
}
