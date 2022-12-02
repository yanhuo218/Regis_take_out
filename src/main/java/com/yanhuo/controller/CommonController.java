package com.yanhuo.controller;

import com.yanhuo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String Path;

    @PostMapping("/upload")
    public R<String> upload(@RequestBody MultipartFile file) throws IOException {
        String Name = UUID.randomUUID().toString();
        String filePosFix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = Name + filePosFix;
        file.transferTo(new File(Path + fileName));
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            String decode = URLDecoder.decode(name,"UTF-8");
            String filePath = Path + decode;
            response.setContentType("image/jpeg");
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ServletOutputStream outputStream = response.getOutputStream();
            int len;
            byte[] bytes = new byte[2048];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}
