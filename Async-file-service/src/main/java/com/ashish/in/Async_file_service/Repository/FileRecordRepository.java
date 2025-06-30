package com.ashish.in.Async_file_service.Repository;

import com.ashish.in.Async_file_service.Model.FileRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRecordRepository extends MongoRepository<FileRecord,String> {

}
