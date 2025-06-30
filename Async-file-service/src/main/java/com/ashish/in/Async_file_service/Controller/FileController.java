package com.ashish.in.Async_file_service.Controller;

import com.ashish.in.Async_file_service.DTO.CampaignRecordList;
import com.ashish.in.Async_file_service.DTO.FileUploadEvent;
import com.ashish.in.Async_file_service.Service.FileMetadataService;
import com.ashish.in.Async_file_service.Service.FileRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileMetadataService fileMetadataService;
    private final FileRecordService fileRecordService;

    @GetMapping("/file/upload/status")
    public ResponseEntity<FileUploadEvent> getFileMetadata(@RequestParam String userId,
                                           @RequestParam String campaignId) {
        FileUploadEvent fileUploadEvent =  fileMetadataService.getFileMetadata(userId,campaignId);
        return ResponseEntity.status(HttpStatus.OK).body(fileUploadEvent);
    }

    @GetMapping("/user/{userId}/campaign/{campaignId}/records")
    public ResponseEntity<?> getFileRecords(@PathVariable String userId, @PathVariable String campaignId) {
        CampaignRecordList campaignRecordList =  fileRecordService.getRecords(userId,campaignId);
        return ResponseEntity.status(HttpStatus.OK).body(campaignRecordList);
    }

}
