package com.khoubyari.example.api.rest;

import org.apache.catalina.servlet4preview.ServletContext;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by baimi on 2017/10/13.
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin
public class FileUploadController extends AbstractRestHandler{
    @RequestMapping(value = "/files", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFiles(HttpServletRequest request) {
//        String uploadFilePath = fileUploadPath.getUploadFilePath();
        String baseFilePath = context.getRealPath("");
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString = df.format(new Date());
        String pathname = baseFilePath + File.separator
                + dateString + File.separator;
        File file = new File(pathname);
        if (!file.exists()) {
            System.out.println("making dir: " + file);
            file.mkdirs();
        }
        MultipartHttpServletRequest muti = (MultipartHttpServletRequest) request;
        System.out.println(muti.getMultiFileMap().size());

        MultiValueMap<String, MultipartFile> map = muti.getMultiFileMap();
        for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {

            List<MultipartFile> list = entry.getValue();
            for (MultipartFile multipartFile : list) {
                try {
                    multipartFile.transferTo(new File(pathname
                            + multipartFile.getOriginalFilename()));
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Autowired
    ServletContext context;
}
