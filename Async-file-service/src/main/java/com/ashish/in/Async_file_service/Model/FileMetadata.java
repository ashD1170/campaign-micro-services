package com.ashish.in.Async_file_service.Model;

import com.ashish.in.Async_file_service.Enum.FileUploadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "file_metadata")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {
    @Id
    private String id;
    private String userId;
    private String campaignId;
    private String fileName;
    private LocalDateTime uploadedAt;
    private FileUploadStatus status;
}
