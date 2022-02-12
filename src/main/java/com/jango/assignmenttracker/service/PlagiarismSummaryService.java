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

    public static String readToString(String targetURL) throws IOException
    {
        URL url = new URL(targetURL);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(url.openStream()));

        StringBuilder stringBuilder = new StringBuilder();

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null)
        {
            log.info("File::::{}", inputLine);
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
            log.info("File lower::::{}", stringBuilder);
        }

        bufferedReader.close();
        return stringBuilder.toString().trim();
    }

    public void readFile(String fileUrl, String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                System.out.println(":::::::::::"+bytesRead);
                System.out.println(":::::::::::"+fileOutputStream.toString());
            }
        } catch (IOException e) {
            // handle exception
        }
    }

    public void mRead(String fileUrl) {
        try
        {
            File file=new File(fileUrl);    //creates a new file instance
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String line;
            while((line=br.readLine())!=null)
            {
                sb.append(line);      //appends line to string buffer
                sb.append("\n");     //line feed
            }
            fr.close();    //closes the stream and release the resources
            System.out.println("Contents of File: ");
            System.out.println(sb.toString());   //returns a string that textually represents the object
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
