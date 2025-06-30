package com.ashish.in.Async_file_service.Service;

import com.ashish.in.Async_file_service.DTO.FileUploadEvent;
import com.ashish.in.Async_file_service.Enum.FileUploadStatus;
import com.ashish.in.Async_file_service.Model.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadListenerService {

    private final FileMetadataService fileMetadataService;
    private final FileRecordService fileRecordService;


    @KafkaListener(topics = "${kafka.topic.file-upload}", groupId = "file-processing-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleFileUploadEvent(FileUploadEvent event) throws IOException {
        log.info("Received FileUploadEvent: {}", event);
        FileMetadata fileMetadata = getFileMetadataObject(event);
        fileMetadataService.saveMetadata(fileMetadata);
        log.info("Metadata Saved: {}", fileMetadata);
        fileRecordService.processFile(event);
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
