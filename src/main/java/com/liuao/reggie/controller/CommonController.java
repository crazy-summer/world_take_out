package com.liuao.reggie.controller;

import com.liuao.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.IconUIResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.bathpath}")
    private String bathPath;

    @PostMapping("/upload")
    public R<String> uploadFile(@RequestParam("file") MultipartFile f){
        log.info("file:"+f);

        // 重新命名文件
        String id = UUID.randomUUID().toString();
        String name = f.getOriginalFilename();
        String suffix = name.substring(name.lastIndexOf("."));
        String fileName = id + suffix;

        // 判断文件夹是否存在
        File parent = new File(bathPath);
        if(!parent.exists()){
            parent.mkdirs();
        }

        try {
            f.transferTo(new File(bathPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    @GetMapping("download")
    public void downLoadImage(String name, HttpServletResponse response){
        File source = new File(bathPath + name);
        FileInputStream fis = null;
        ServletOutputStream outputStream = null;
        int len = 0;
        try {
            fis = new FileInputStream(source);
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while((len = fis.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
