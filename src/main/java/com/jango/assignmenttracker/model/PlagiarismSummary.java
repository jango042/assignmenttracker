package com.jango.assignmenttracker.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PlagiarismSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentOneName;
    private String studentTwoName;
    private String studentOneFileUrl;
    private String studentTwoFileUrl;
    private String studentOneFileName;
    private String studentTwoFileName;
    private String plagiarismPercentage;
    private LocalDateTime timeOfCheck;
    @NotNull(message = "The User Id Cannot be null")
    private Long userId;
    @NotNull(message = "The User Email Cannot be null")
    private String userEmail;
}
