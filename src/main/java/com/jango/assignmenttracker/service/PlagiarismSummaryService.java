package com.jango.assignmenttracker.service;

import com.jango.assignmenttracker.model.PlagiarismSummary;
import com.jango.assignmenttracker.pojo.Response;
import com.jango.assignmenttracker.repository.PlagiarismSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.Optional;

@Service
@Slf4j
public class PlagiarismSummaryService {

    @Autowired
    private PlagiarismSummaryRepository plagiarismSummaryRepository;

    @Autowired
    private UserServiceImpl userService;



    public Response findSummaryByUserId(Long userId) {
        try {
            return new Response.ResponseBuilder<>()
                    .data(plagiarismSummaryRepository.findByUserId(userId))
                    .code(200)
                    .status(true)
                    .message("Data Pulled Successfully")
                    .build();
        } catch (Exception e) {
            return new Response.ResponseBuilder<>()
                    .code(500)
                    .status(false)
                    .message("Data Pulled failed")
                    .build();
        }
    }

    public Response findSummaryByUserEmail(String userEmail) {
        try {
            return new Response.ResponseBuilder<>()
                    .data(plagiarismSummaryRepository.findByUserEmail(userEmail))
                    .code(200)
                    .status(true)
                    .message("Data Pulled Successfully")
                    .build();
        } catch (Exception e) {
            return new Response.ResponseBuilder<>()
                    .code(500)
                    .status(false)
                    .message("Data Pulled failed")
                    .build();
        }
    }

    public Response reCompareFiles(Long historyId) {
        Optional<PlagiarismSummary> plagiarismSummary = plagiarismSummaryRepository.findById(historyId);
        try {
            if (plagiarismSummary.isPresent()) {
                String result = userService.filesCompareByLine(plagiarismSummary.get().getStudentOneFileUrl(), plagiarismSummary.get().getStudentTwoFileUrl());
                return new Response.ResponseBuilder<>()
                        .data(result)
                        .code(200)
                        .status(true)
                        .message("Data Pulled Successfully")
                        .build();
            } else {
                return new Response.ResponseBuilder<>()
                        .code(404)
                        .status(false)
                        .message("Id provided not found")
                        .build();
            }
        } catch (IOException e) {
            return new Response.ResponseBuilder<>()
                    .code(500)
                    .status(false)
                    .message("Data Pulled failed")
                    .build();
        }

    }


}
