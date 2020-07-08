package com.jakarta.jakartaback.controllers;

import com.alibaba.fastjson.JSONObject;
import com.jakarta.jakartaback.exceptions.ExceptionType;
import com.jakarta.jakartaback.token.PassToken;
import com.jakarta.jakartaback.utils.Utils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("files-api")
public class FileAPIControllers {

    private final SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MMdd/");

    private final String pathHeader = System.getProperty("user.dir") + "/uploaded";

    // 上传文件接口
    @CrossOrigin
    @PassToken
    @PostMapping("/upload")
    public JSONObject upload(MultipartHttpServletRequest request, HttpServletRequest req) {
        Map<String, MultipartFile> files = request.getFileMap();//得到文件map对象
        MultipartFile file = files.get("avatar");
        if (file == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "No data received.");
        }
        // 构建文件文件夹
        String format = sdf.format(new Date());
        File folder = new File(pathHeader + format);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Server was unable to create folders.");
            }
        }
        // 渲染文件名
        String oldName = file.getOriginalFilename();
        if (oldName == null) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "The uploading service reports an error.");
        }
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        // 保存
        File savedTo = new File(folder, newName);
        try {
            file.transferTo(savedTo);
        } catch (IOException e) {
            return Utils.failedReturn(ExceptionType.EXECUTE_FAILED, "Server's storage was too bad to store an image");
        }
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;
        // test
        JSONObject fileURI = new JSONObject();
        fileURI.put("uri", url);
        return Utils.succeededReturn(fileURI, "Upload succeeded.");
    }
}
