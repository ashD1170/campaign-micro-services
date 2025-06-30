package com.ashish.in.campaign_management_service.Service;

import com.ashish.in.campaign_management_service.Dto.FileUploadEvent;
import com.ashish.in.campaign_management_service.Enum.FileExtensions;
import com.ashish.in.campaign_management_service.Enum.FileUploadStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingService {
    private final S3Service s3Service;
    private final KafkaProducerService kafkaProducerService;
    @Value("${kafka.topic.file-upload}")
    private String fileUploadTopic;

    public FileUploadEvent uploadUserFile(MultipartFile file, String userId, String campaignId) throws IOException {
        System.out.println("started file upload");
        log.info("File Upload Started for the campaignId:{} with filename:{}",campaignId, file.getOriginalFilename());

        String key = getString(file, userId, campaignId);
        s3Service.uploadFileToS3(file,key);
        FileUploadEvent fileUploadEvent = generateFileUploadEvent(file, userId, campaignId);

        System.out.println("file upload completed");
        log.info("File Upload Successful for the campaignId:{} with filename:{}",campaignId, file.getOriginalFilename());

        kafkaProducerService.sendEvent(fileUploadTopic,fileUploadEvent);

        log.info("File Upload Event Sent to the Kaka Topic:{}",fileUploadTopic);
        return fileUploadEvent;
    }

    public void validateFileExtension(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(extension == null || !FileExtensions.isAllowed(extension)){
            throw new IllegalArgumentException("Unsupported File Extension :" + extension);
        }
    }

    private FileUploadEvent generateFileUploadEvent(MultipartFile file, String userId, String campaignId) {
        FileUploadEvent fileUploadEvent = new FileUploadEvent();
        fileUploadEvent.setUserId(userId);
        fileUploadEvent.setCampaignId(campaignId);
        fileUploadEvent.setFilename(file.getOriginalFilename());
        fileUploadEvent.setUploadedAt(LocalDateTime.now());
        fileUploadEvent.setStatus(FileUploadStatus.UPLOADED);
        return fileUploadEvent;
    }

    private static String getString(MultipartFile file, String userId, String campaignId) {
        return userId + "/" + campaignId;
    }
}

