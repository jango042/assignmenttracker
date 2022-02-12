package com.jango.assignmenttracker.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
@Slf4j
public class CloudinaryUpload {

    Cloudinary cloudinary;

    @Value("max-file-size")
    private static long maxUploadSize;

    public CloudinaryUpload(){
        log.info("==============Cloudinary upload Constructor=============");
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "jango042",
                "api_key", "286354597951694",
                "api_secret", "10hfSlsgm7iHLYXpC6C3-6iQdXc"));
    }





    public static File convert2(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());

        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (MaxUploadSizeExceededException maxUploadSizeExceededException ) {
            maxUploadSizeExceededException.printStackTrace();

        }
        return convFile;
    }




    public String uploadFile(MultipartFile mFile){
        String photoUrl = "";
        log.info("==============Cloudinary upload method=============");
        Map uploadResult = null;

        try {
            File imageFile = convert2(mFile);

            log.info("==============Cloudinary upload method=============");
            log.info("File Name upload method::::"+imageFile.getName());
            if (imageFile != null){

                log.info("Cloudinary uploading....................");
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "jango042",
                        "api_key", "286354597951694",
                        "api_secret", "10hfSlsgm7iHLYXpC6C3-6iQdXc"));

                uploadResult = cloudinary.uploader().upload(imageFile,
                        ObjectUtils.asMap("resource_type", "auto"));
                log.info("============================After uploading========================");
                if (uploadResult.isEmpty()){
                    log.info("Result is empty");
                }else {
                    log.info("Result is not empty");
                }
                photoUrl = uploadResult.get("secure_url").toString();
                log.info("::::::::"+uploadResult.size());
                log.info("uploadResult String "+ photoUrl);

            }
            return photoUrl;
        }catch (MaxUploadSizeExceededException | IOException e){
            log.info("=====================Cloudinary Error=============");
            return "File too Large";
        }


    }

}
