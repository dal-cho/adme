//package com.dalcho.adme.controller;
//
//import com.dalcho.adme.domain.TenSeconds;
//import com.dalcho.adme.dto.VideoDto;
//import com.dalcho.adme.security.UserDetailsImpl;
//import com.dalcho.adme.service.TenSecondsService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.coobird.thumbnailator.Thumbnails;
//import net.coobird.thumbnailator.name.Rename;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.nio.file.Files;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@RequiredArgsConstructor // final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성 (@Autowierd 를 해주지 않아도 된다.)
//@Controller
//@Slf4j
//public class TenSecondsController {
//
//    private final TenSecondsService tenSecondsService;
//
//    // application.properties 에서 설정한 파일 저장 경로
//    @Value("${spring.servlet.multipart.location}")
//    private String uploadFolderPath;
//
////    // 신규 영상 등록
////    @PostMapping("/10s/videos")
////    public TenSeconds createVideo(@RequestBody VideoDto videoDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws SQLException {
////        // 로그인 되어 있는 ID
////        Long userId = userDetails.getUser().getId();
////        TenSeconds tenSeconds = tenSecondsService.createVideo(videoDto, userId);
////        return tenSeconds;
////    }
//
////    // Id로 등록된 전체 영상 목록 조회
////    @GetMapping("/10s/videos")
////    public List<TenSeconds> getVideos(@AuthenticationPrincipal UserDetailsImpl userDetails) throws SQLException {
////        // 로그인한 사용자가 등록한 영상들 조회
////        Long userId = userDetails.getUser().getId();
////        return tenSecondsService.getVideos(userId);
////    }
//
//    // 이미지 파일 로컬로 저장 및 가져오기
//    @PostMapping(value = "/10s/videos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public ResponseEntity<List<VideoDto>> uploadAjaxPost(MultipartFile[] uploadFile) {
//        log.info("update file post.................");
//
//        // 폴더 만들기
//        File destinationDir = new File(uploadFolderPath + "thumbnails");
//        if(!destinationDir.exists()) {
//            destinationDir.mkdirs();
//        }
//
//        // DTO 설정
//        List<VideoDto> list = new ArrayList<>();
//
//        for(MultipartFile multipartFile : uploadFile) {
//            log.info("---------------------------------");
//            log.info("Upload File Name :" + multipartFile.getOriginalFilename());
//            log.info("Upload File Size : " + multipartFile.getSize());
//
//            // 썸네일용 파일 이름 설정
//            String originalFilename = multipartFile.getOriginalFilename();
//
//            // DTO 설정
//            VideoDto attachFileDTO = new VideoDto();
//            attachFileDTO.setFileName(originalFilename);
//
//            // 중복 방지를 위한 UUID 적용
//            UUID uuid = UUID.randomUUID();
//            String uploadFileName = uuid.toString() + "_" + originalFilename;
//
//            try {
//                // 해당 위치에 지정한 이름으로 파일 저장
//                File saveFile = new File(uploadFolderPath, uploadFileName);
//                multipartFile.transferTo(saveFile);
//
//                // DTO 설정
//                attachFileDTO.setUuid(uuid.toString());
//                attachFileDTO.setUploadPath(uploadFolderPath);
//
//                // 이미지 타입인지 확인하고 썸네일 설정, 파일 이름은 s_로 시작
//                if(checkImageType(saveFile)) {
//                    // DTO 설정
//                    attachFileDTO.setImage(true);
//
//                    // 썸네일 사진 가져와서 만들기
//                    Thumbnails.of(new File(uploadFolderPath + File.separator + uploadFileName))
//                            .size(200, 200) // 사이즈 조절
//                            .toFiles(destinationDir, Rename.NO_CHANGE);
//                }
//                // List 추가
//                list.add(attachFileDTO);
//
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }
//
//    // 썸네일 이미지 가져오기
//    @GetMapping("/10s/videos")
//    @ResponseBody
//    public ResponseEntity<byte[]> getFile(String fileName){
//
//        log.info("fileName : " + fileName);
//
//        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//
//        File file = new File(uploadFolderPath + "thumbnails/" + fileName);
//
//        log.info("file:" + file);
//
//        ResponseEntity<byte[]> result = null;
//
//        try {
//            HttpHeaders header = new HttpHeaders();
//
//            header.add("Content - Type", Files.probeContentType(file.toPath()));
//            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
//
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//    // 업로드 이미지 클릭시 해당 파일 다운로드
//    @GetMapping(value = "10s/video/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    @ResponseBody
//    public ResponseEntity<Resource> downloadFile(String fileName){
//
//        log.info("다운로드 시작");
//
//        // fileName 이 경로와 같이 설정되어 있어서 이름만 따로 분리해준다.
//        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//
//        // 옳바른 경로 저장
//        Resource resource = new FileSystemResource(uploadFolderPath + fileName);
//
//        String resourceName = resource.getFilename();
//
//        // uuid 제거
//        String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
//
//        HttpHeaders headers = new HttpHeaders();
//
//        try {
//            headers.add("Content-Disposition", "attachment; filename=" + new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1"));
//        }catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
//    }
//
//    // 첨부파일 삭제
//    @PostMapping("10s/video/delete")
//    @ResponseBody
//    public ResponseEntity<String> deleteFile(String fileName, String type) {
//        log.info("deleteFile : " + fileName);
//
//        File file;
//
//        try {
//            file = new File(uploadFolderPath + URLDecoder.decode(fileName, "UTF-8"));
//
//            file.delete();
//
//            file = new File(uploadFolderPath + "thumbnail/" + URLDecoder.decode(fileName, "UTF-8"));
//
//            file.delete();
//
//        }catch(UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<String>("deleted", HttpStatus.OK);
//    }
//
//    // 이미지 타입인지 확인하기
//    private boolean checkImageType(File file) {
//        try {
//            String contentType = Files.probeContentType(file.toPath());
//            return contentType.startsWith("image");
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//}
