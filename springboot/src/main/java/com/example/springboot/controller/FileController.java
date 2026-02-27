package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {
    // 文件上傳路徑
    // System.getProperty("user.dir")：獲取當前這個項目根目錄
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    // 文件上傳
    @PostMapping("/upload")
    public Result upload(MultipartFile file) { //文件流的形式接收
        String originalFileName = file.getOriginalFilename(); //文件自帶的名稱

        // 判斷上傳資料夾是否存在
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }

        // 給文件名加上時間戳(避免有原名稱重複的檔案)
        String fileName = System.currentTimeMillis() + "_" + originalFileName;
        String realPath = filePath + fileName;

        // 將輸入上傳到上傳地址(文件字節數, 上傳路徑)
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        String url = "http://localhost:9090/files/download/"+fileName;
        return Result.success(url);
    }
    // 文件下載
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response){
        String realPath = filePath + fileName;
        // 獲取文件字節 數組
        byte[] bytes = FileUtil.readBytes(realPath);

        try {
            OutputStream os = response.getOutputStream();

            // 文件名統一為UTF-8編碼
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            // 下載響應頭
            response.setContentType("application/octet-stream");

            os.write(bytes);
            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 文件上傳
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) { //文件流的形式接收
        String originalFileName = file.getOriginalFilename(); //文件自帶的名稱

        // 判斷上傳資料夾是否存在
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }

        // 給文件名加上時間戳(避免有原名稱重複的檔案)
        String fileName = System.currentTimeMillis() + "_" + originalFileName;
        String realPath = filePath + fileName;

        // 將輸入上傳到上傳地址(文件字節數, 上傳路徑)
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        String url = "http://localhost:9090/files/download/"+fileName;

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> urlMap = new HashMap<>();

        urlMap.put("url", url);
        resMap.put("errno", 0);
        resMap.put("data", urlMap);
        return resMap;
    }

}
