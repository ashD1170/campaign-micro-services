package com.ashish.in.Async_file_service.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "file_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileRecord {
    @Id
    private String id;
    private String userId;
    private String campaignId;
    private String phoneNumber;
    private String fileName;
    private Map<String,String> data;
}
