package org.ncpsb.phoenixcluster.enhancer.webservice.api.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.catalina.servlet4preview.ServletContext;
import org.ncpsb.phoenixcluster.enhancer.webservice.domain.FileUploadResponse;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.AnalysisJob;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.Configure;
import org.ncpsb.phoenixcluster.enhancer.webservice.model.ResultFileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.ncpsb.phoenixcluster.enhancer.webservice.service.FileUploadService;
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
@RequestMapping("example/v1/file")
@CrossOrigin(origins = "*")
public class FileController extends AbstractRestHandler{


    @Autowired FileUploadService fileUploadService;
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileUploadResponse uploadFiles(HttpServletRequest request,
//    @ApiParam(value = "The analysis id", required = true)
//    @RequestParam(value = "myid", required = true) Integer myId)
    @RequestHeader("myId") Integer myId
    )
    {
//        String uploadFilePath = fileUploadPath.getUploadFilePath();
//        Integer myId = 1;
        String baseFilePath = Configure.ANALYSIS_DATA_PATH;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString = df.format(new Date());
        String pathname = baseFilePath + File.separator
                + dateString + File.separator + myId + File.separator;
        File file = new File(pathname);
        if (!file.exists()) {
            System.out.println("making dir: " + file);
            file.mkdirs();
        }
        fileUploadService.upsertAnalysisRecordInfo(myId, pathname, dateString, 0, "initialed");

        MultipartHttpServletRequest muti = (MultipartHttpServletRequest) request;
        System.out.println(muti.getMultiFileMap().size());

        MultiValueMap<String, MultipartFile> map = muti.getMultiFileMap();
        for (Map.Entry<String, List<MultipartFile>> entry : map.entrySet()) {

            List<MultipartFile> list = entry.getValue();
            for (MultipartFile multipartFile : list) {
                try {
                    multipartFile.transferTo(new File(pathname
                            + multipartFile.getOriginalFilename()));
                    System.out.println("File" + multipartFile.getOriginalFilename()+ "  has benn uploaded");
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("File" + pathname + "  has benn uploaded");
        }
        fileUploadService.upsertAnalysisRecordStatus(myId, "uploading");

        FileUploadResponse fileUploadResponse = new FileUploadResponse("success", myId, "NULL");
        return fileUploadResponse;
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


    @RequestMapping(value = "/apply", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AnalysisJob applyForAnalysisJob(
            ) throws IOException {
        AnalysisJob analysisJob = fileUploadService.initAnalysisJob();
        return analysisJob;
    }

    @RequestMapping(value = "/confirmFiles", method = RequestMethod.POST, produces = {"application/json"})
    public FileUploadResponse confirmFiles(HttpServletRequest request,
    @RequestHeader("myId") Integer myId,
//    @RequestBody String resultFileList
    @RequestBody ResultFileList resultFileList
    )
    {
        FileUploadResponse fileUploadResponse = new FileUploadResponse("null", myId, "");
        AnalysisJob analysisJob = fileUploadService.getAnalysisJob(myId);
        boolean correctFlag = fileUploadService.isFileListCorrect(resultFileList, myId, fileUploadResponse, analysisJob);
        if (correctFlag) {
            System.out.println("you got " + resultFileList.getFileListLength() + ": " + resultFileList.getFileList() + " file in AnalysisJob " + myId);
            fileUploadResponse.setStatus("success");
            fileUploadService.writeToResultFile(analysisJob.getFilePath(), resultFileList);
            fileUploadService.upsertAnalysisRecordStatus(myId, "uploaded");
        }else {
            fileUploadResponse.setStatus("error");
        }
        return fileUploadResponse;
    }

    @Autowired
    ServletContext context;
}
