package com.ashish.in.Async_file_service.Repository;

import com.ashish.in.Async_file_service.Model.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileMetadataRepository extends MongoRepository<FileMetadata,String> {
    public FileMetadata getFileMetadataByUserIdAndCampaignId(String userId, String campaignId);
}
