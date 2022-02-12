package com.jango.assignmenttracker.controller;

import com.jango.assignmenttracker.pojo.LoginDetailsPojo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("User login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Response Headers")})
    @PostMapping("/login")
    public void Login(@RequestBody LoginDetailsPojo loginRequestModel){

        throw new IllegalStateException("Error Occurred");
    }

}
