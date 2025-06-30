package com.ashish.in.Async_file_service.Repository;

import com.ashish.in.Async_file_service.Model.FileRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FileRecordRepository extends MongoRepository<FileRecord,String> {

    @Query(value = "{ 'userId' : ?0, 'campaignId' : ?1 }", delete = true)
    void deleteByUserIdAndCampaignId(String userId, String campaignId);

    List<FileRecord> getByUserIdAndCampaignId(String userId, String campaignId);
}
