package com.hzp.test.controller;

import com.hzp.test.dto.common.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(tags = "上传")
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @ApiOperation("上传头像和照片")
    @PostMapping("/userImg")
    public ResponseEntity upload(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestPart("headerImg") MultipartFile headerImg,
            @RequestPart("photos") MultipartFile[] photos) throws IOException {
        log.info("上传的信息：email={} ，username={}，headerImg={},photos={}", email, username, headerImg.getSize(), photos.length);
        if (!headerImg.isEmpty()) {
            String originalFilename = headerImg.getOriginalFilename();
            headerImg.transferTo(new File("C:\\Users\\Administrator\\Desktop\\image\\" + originalFilename));
        }
        if (photos.length > 0) {
            for (MultipartFile photo : photos) {
                String originalFilename = photo.getOriginalFilename();
                photo.transferTo(new File("C:\\Users\\Administrator\\Desktop\\image\\" + originalFilename));
            }
        }
        return new ResponseEntity();
    }

}
