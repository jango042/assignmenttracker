package com.jango.assignmenttracker.controller;

import com.jango.assignmenttracker.pojo.Response;
import com.jango.assignmenttracker.service.PlagiarismSummaryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class History {

    @Autowired
    private PlagiarismSummaryService plagiarismSummaryService;



    @ApiOperation(value = "List of already compared file with user id", notes = "List of already compared file with user id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "", paramType = "header")
    })
    @GetMapping("/by/id/{userId}")
    public ResponseEntity<Response> findComparisonSessionById(@PathVariable("userId") Long userId) {
        Response history = plagiarismSummaryService.findSummaryByUserId(userId);

        if (!history.getStatus()) {
            return new ResponseEntity<>(history, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "List of already compared file with user email", notes = "List of already compared file with user email")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "", paramType = "header")
    })
    @GetMapping("/by/email/{email}")
    public ResponseEntity<Response> findComparisonSessionByEmail(@PathVariable("email") String email) {
        Response history = plagiarismSummaryService.findSummaryByUserEmail(email);

        if (!history.getStatus()) {
            return new ResponseEntity<>(history, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Re run file comparison for students with the historyId", notes = "Re run file comparison for students with the historyId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "", paramType = "header")
    })
    @PostMapping("/recompare")
    public ResponseEntity<Response> reRunComparison(@RequestParam("resultId") Long resultId) {
        Response history = plagiarismSummaryService.reCompareFiles(resultId);

        if (!history.getStatus()) {
            return new ResponseEntity<>(history, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
    }

}
