package com.ashish.in.Async_file_service.Service;

import com.ashish.in.Async_file_service.DTO.FileUploadEvent;
import com.ashish.in.Async_file_service.Enum.FileUploadStatus;
import com.ashish.in.Async_file_service.Model.FileMetadata;
import com.ashish.in.Async_file_service.Repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadListenerService {

    private final FileMetadataService fileMetadataService;
    private final FileRecordService fileRecordService;
    private final FileMetadataRepository fileMetadataRepository;


    @KafkaListener(topics = "${kafka.topic.file-upload}", groupId = "file-processing-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleFileUploadEvent(FileUploadEvent event) throws IOException {
        log.info("Received FileUploadEvent: {}", event);
        FileMetadata fileMetadata = fileMetadataRepository.getFileMetadataByUserIdAndCampaignId(event.getUserId(), event.getCampaignId());
        if(fileMetadata != null) {
            log.info("FileMetadata already exists for userId:{},campaignId:{}", event.getUserId(), event.getCampaignId());
            fileMetadata.setFileName(event.getFilename());
            fileMetadata.setStatus(FileUploadStatus.PROCESSING);
            log.info("FileMetadata updated: {}", fileMetadata);
            log.info("Deleting and Updating records for the userId:{},campaignId{}", fileMetadata.getUserId(), event.getCampaignId());
            fileRecordService.deleteAllRecords(event.getUserId(),event.getCampaignId());
        } else {
            fileMetadata = getFileMetadataObject(event);
        }
        fileMetadataService.saveMetadata(fileMetadata);
        log.info("Metadata Saved: {}", fileMetadata);
        log.info("Updating Records to MongoDB for userId:{},campaignId:{}", event.getUserId(), event.getCampaignId());
        fileRecordService.processFile(event);
        log.info("Records updated to MongoDB for userId:{},campaignId:{}", event.getUserId(), event.getCampaignId());
        fileMetadataService.markAsCompleted(fileMetadata.getId());
    }

    private static FileMetadata getFileMetadataObject(FileUploadEvent event) {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(event.getFilename());
        fileMetadata.setCampaignId(event.getCampaignId());
        fileMetadata.setUserId(event.getUserId());
        fileMetadata.setUploadedAt(event.getUploadedAt());
        fileMetadata.setStatus(FileUploadStatus.PROCESSING);
        return fileMetadata;
    }
}
