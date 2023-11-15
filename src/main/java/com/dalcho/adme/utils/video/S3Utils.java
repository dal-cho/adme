package com.dalcho.adme.utils.video;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.video.VideoMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Utils {
    private final AmazonS3Client amazonS3Client;

    @Value("${thumbnail.location}")
    String thumbnailDirectory;
    @Value("${ten.location}")
    String tenVideoDirectory;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3 Thumbnail 저장
    public String thumbnailUpload(File thumbFile) throws IOException {
        log.info("[S3Utils] thumbnailUpload");

        InputStream thumbInputStream = new FileInputStream(thumbFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(Files.probeContentType(thumbFile.toPath()));
        // 파일 크기 설정
        objectMetadata.setContentLength(thumbFile.length());

        // 저장 경로 (bucket 내의 폴더 경로)
        String key = "thumbnail/" + thumbFile.getName();

        try (InputStream inputStream = thumbInputStream) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }
        log.info("[S3Utils] thumbnailUpload Completed!");

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    // S3 TenVideo 저장
    public String tenVideoUpload(VideoMultipartFile videoFile) throws IOException {
        return tenVideoUpload(videoFile.getUuid());
    }
    public String tenVideoUpload(VideoFile videoFile) throws IOException{
        return tenVideoUpload(videoFile.getUuid());
    }

    private String tenVideoUpload(UUID uuid) throws IOException {
        log.info("[S3Utils] tenVideoUpload");

        File tenFile = new File(tenVideoDirectory + uuid + ".mp4");
        InputStream tenInputStream = new FileInputStream(tenFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(Files.probeContentType(tenFile.toPath()));
        // 파일 크기 설정
        objectMetadata.setContentLength(tenFile.length());

        // 저장 경로 (bucket 내의 폴더 경로)
        String key = "tenVideo/" + uuid + ".mp4";

        try (InputStream inputStream = tenInputStream) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }
        log.info("[S3Utils] tenVideoUpload Completed!");
        return amazonS3Client.getUrl(bucket, key).toString();
    }

    // S3 Thumbnail 삭제
    public void deleteThumbnail(String fileName) {
        log.info("[S3Utils] deleteThumbnail");
        String key = "thumbnail/" + fileName;
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket,key));
        log.info("[S3Utils] deleteThumbnail Completed!");
    }

    // S3 TenVideo 삭제
    public void deleteTenVideo(String fileName) {
        log.info("[S3Utils] deleteTenVideo");
        String key = "tenVideo/" + fileName;
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket,key));
        log.info("[S3Utils] deleteTenVideo Completed!");
    }
}