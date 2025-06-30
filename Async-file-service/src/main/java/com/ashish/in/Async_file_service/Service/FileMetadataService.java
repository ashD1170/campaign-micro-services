package com.ashish.in.Async_file_service.Service;

import com.ashish.in.Async_file_service.DTO.FileUploadEvent;
import com.ashish.in.Async_file_service.Enum.FileUploadStatus;
import com.ashish.in.Async_file_service.Model.FileMetadata;
import com.ashish.in.Async_file_service.Repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMetadataService {

    private final FileMetadataRepository fileMetadataRepository;

    public void saveMetadata(FileMetadata metadata) {
        fileMetadataRepository.save(metadata);
    }

    public void markAsCompleted(String id) {
        FileMetadata metadata = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metadata not found"));

        metadata.setStatus(FileUploadStatus.PROCESSING_COMPLETED);
        fileMetadataRepository.save(metadata);
    }

    public FileUploadEvent getFileMetadata(String userId, String campaignId) {
        FileMetadata fileMetadata = fileMetadataRepository.getFileMetadataByUserIdAndCampaignId(userId,campaignId);
        FileUploadEvent fileUploadEvent = new FileUploadEvent();
        fileUploadEvent.setCampaignId(fileMetadata.getCampaignId());
        fileUploadEvent.setUserId(fileMetadata.getUserId());
        fileUploadEvent.setFilename(fileMetadata.getFileName());
        fileUploadEvent.setStatus(fileMetadata.getStatus());
        fileUploadEvent.setUploadedAt(fileMetadata.getUploadedAt());
        return fileUploadEvent;
    }
}
