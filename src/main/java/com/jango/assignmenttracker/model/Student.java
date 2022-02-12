package com.jango.assignmenttracker.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime uploadTime;
    private String filePath;
    private String fileName;
    @NotNull(message = "The User Id Cannot be null")
    private Long userId;
    private Boolean isChecked = false;
}
