package com.jango.assignmenttracker.controller;

import com.jango.assignmenttracker.pojo.ComPareStudentFilesPojo;
import com.jango.assignmenttracker.pojo.Response;
import com.jango.assignmenttracker.service.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/compare")
public class CompareController {

    @Autowired
    private UserServiceImpl userService;



    @ApiOperation(value = "Upload file of Students to compare", notes = "Upload file of Students to compare")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "", paramType = "header")
    })
    @PostMapping("/students/file")
    public ResponseEntity<Response> compareFiles(@RequestParam("student1") String student1,@RequestParam("student2") String student2, @RequestPart("file1") MultipartFile file1, @RequestPart("file2") MultipartFile file2) {
        Response comparedFile = userService.compareFiles(student1, student2, file1, file2);

        if (!comparedFile.getStatus()) {
            return new ResponseEntity<>(comparedFile, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(comparedFile, HttpStatus.OK);
        }
    }

}
