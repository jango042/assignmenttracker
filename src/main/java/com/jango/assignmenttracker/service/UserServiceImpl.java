package com.jango.assignmenttracker.service;

import com.jango.assignmenttracker.model.PlagiarismSummary;
import com.jango.assignmenttracker.model.Student;
import com.jango.assignmenttracker.model.User;
import com.jango.assignmenttracker.pojo.ComPareStudentFilesPojo;
import com.jango.assignmenttracker.pojo.Response;
import com.jango.assignmenttracker.repository.PlagiarismSummaryRepository;
import com.jango.assignmenttracker.repository.StudentRepository;
import com.jango.assignmenttracker.repository.UserRepository;
import com.jango.assignmenttracker.security.AuthenticatedUserFacade;
import com.jango.assignmenttracker.util.CloudinaryUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatedUserFacade authenticatedUserFacade;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    @Autowired
    private PlagiarismSummaryRepository summaryRepository;





    @Override
    public Response compareFiles(String student1Name, String student2Name, MultipartFile file1, MultipartFile file2) {

        try {
            File student1File = CloudinaryUpload.convert2(file1);
            File student2File = CloudinaryUpload.convert2(file2);

            String percentage =  filesCompareByLine(student1File.toPath(), student2File.toPath());

            PlagiarismSummary plagiarismSummary1 = plagiarismSummary(student1Name, student2Name, file1, file2, percentage, authenticatedUserFacade.getUser());

            createStudent(student1Name, file1.getName(), authenticatedUserFacade.getUser().getId());
            createStudent(student2Name, file2.getName(), authenticatedUserFacade.getUser().getId());



            return new Response.ResponseBuilder<>()
                    .data(plagiarismSummary1)
                    .code(200)
                    .status(true)
                    .message("Data Pulled Successfully")
                    .build();
        }catch (IOException e){
            return new Response.ResponseBuilder<>()
                    .code(500)
                    .status(false)
                    .message("Data Pulled failed")
                    .build();
        }
    }

    private PlagiarismSummary plagiarismSummary(String student1Name, String student2Name, MultipartFile file1, MultipartFile file2, String percentage, User user) {
        PlagiarismSummary plagiarismSummary = new PlagiarismSummary();
        plagiarismSummary.setId(0L);
        plagiarismSummary.setPlagiarismPercentage(percentage);
        plagiarismSummary.setStudentOneFileName(file1.getName());
        plagiarismSummary.setStudentOneFileUrl(cloudinaryUpload.uploadFile(file1));
        plagiarismSummary.setStudentTwoFileName(file2.getName());
        plagiarismSummary.setStudentTwoFileUrl(cloudinaryUpload.uploadFile(file2));
        plagiarismSummary.setStudentOneName(student1Name);
        plagiarismSummary.setStudentTwoName(student2Name);
        plagiarismSummary.setTimeOfCheck(LocalDateTime.now());
        plagiarismSummary.setUserId(user.getId());
        plagiarismSummary.setUserEmail(user.getEmail());
        return summaryRepository.save(plagiarismSummary);
    }

    private void createStudent(String name, String fileName, Long userId) {
        Student student = new Student();
        student.setName(name);
        student.setFileName(fileName);
        student.setUploadTime(LocalDateTime.now());
        student.setId(0L);
        student.setIsChecked(true);
        student.setUserId(userId);
        studentRepository.save(student);
    }

    @Override
    public Response findAllComparedFilesByAssistant(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading User.....");
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(email);
        }else {
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(),
                    user.get().getPassword(), true, true, true, true, new ArrayList<>());

        }
    }

    public  String filesCompareByLine(Path path1, Path path2) throws IOException {
         List<Integer> matchComparator = new ArrayList<>();
        BufferedReader bf1 = Files.newBufferedReader(path1);
             BufferedReader bf2 = Files.newBufferedReader(path2);

            long lineNumber = 1;
            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    matchComparator.add(0);
                } else {
                    matchComparator.add(1);
                }
                lineNumber++;
            }

        log.info("matchComparator  {}",matchComparator.size());

    long totalMatched=   matchComparator.stream().filter(data-> data==1).count();
            log.info("totalMatched  {}",totalMatched);
            double percentage = (totalMatched/ (double) matchComparator.size())*100;
        log.info("percentage  {}",percentage);
      return  String.format("%.1f", percentage)+"%";
    }


    public  String filesCompareByLine(String file1, String file2) throws IOException {
        URL url1 = new URL(file1);
        URL url2 = new URL(file2);
        List<Integer> matchComparator = new ArrayList<>();
        BufferedReader bf1 = new BufferedReader(
                new InputStreamReader(url1.openStream()));
        BufferedReader bf2 = new BufferedReader(
                new InputStreamReader(url2.openStream()));

        long lineNumber = 1;
        String line1 = "", line2 = "";
        while ((line1 = bf1.readLine()) != null) {
            line2 = bf2.readLine();
            if (line2 == null || !line1.equals(line2)) {
                matchComparator.add(0);
            } else {
                matchComparator.add(1);
            }
            lineNumber++;
        }

        log.info("matchComparator  {}",matchComparator.size());

        long totalMatched=   matchComparator.stream().filter(data-> data==1).count();
        log.info("totalMatched  {}",totalMatched);
        double percentage = (totalMatched/ (double) matchComparator.size())*100;
        log.info("percentage  {}",percentage);
        return  String.format("%.1f", percentage)+"%";
    }



}
