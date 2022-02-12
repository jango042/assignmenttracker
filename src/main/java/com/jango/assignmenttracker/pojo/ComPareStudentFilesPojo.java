package com.jango.assignmenttracker.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComPareStudentFilesPojo {
    private String studentName1;
    private String studentName2;
    private MultipartFile data;
}
