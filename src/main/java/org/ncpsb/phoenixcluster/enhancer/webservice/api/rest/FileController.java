package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by baimi on 2017/10/13.
 */
@RestController
@RequestMapping(value = "/example/v1/file")
@CrossOrigin
public class FileController extends AbstractRestHandler{
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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


    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> download(
            @ApiParam(value = "The file url path", required = true)
                                      @RequestParam(value = "filepath", required = true) String filepath
            ) throws IOException {

//        String uploadFilePath = fileUploadPath.getUploadFilePath();
        String baseFilePath = context.getRealPath("");

        String pathname = baseFilePath + File.separator
                + filepath;
        File file = new File(pathname);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("content-disposition", "attachment; filename=\"" + filepath+"\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }


    @Autowired
    ServletContext context;
}
