package com.jango.assignmenttracker.service;

import com.jango.assignmenttracker.pojo.ComPareStudentFilesPojo;
import com.jango.assignmenttracker.pojo.Response;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;


public interface UserService extends UserDetailsService {
    Response compareFiles(String student1Name, String student2Name, MultipartFile file1, MultipartFile file2);
    Response findAllComparedFilesByAssistant(Long userId);
}
