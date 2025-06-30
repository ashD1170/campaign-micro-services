package com.ashish.in.Async_file_service.DTO;

import com.ashish.in.Async_file_service.Enum.FileUploadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadEvent {
    private String userId;
    private String campaignId;
    private String filename;
    private LocalDateTime uploadedAt;
    private FileUploadStatus status;
}
