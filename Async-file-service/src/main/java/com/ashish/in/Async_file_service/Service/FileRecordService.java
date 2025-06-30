package com.ashish.in.Async_file_service.Service;

import com.ashish.in.Async_file_service.DTO.CampaignRecord;
import com.ashish.in.Async_file_service.DTO.CampaignRecordList;
import com.ashish.in.Async_file_service.DTO.FileUploadEvent;
import com.ashish.in.Async_file_service.Model.FileRecord;
import com.ashish.in.Async_file_service.Repository.FileRecordRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileRecordService {

    private final S3Service s3Service;
    private final FileRecordRepository repository;
    private final FileRecordRepository fileRecordRepository;
    @Value("${file.upload.batch-size}")
    private int batchSize;

    public void processFile(FileUploadEvent event) throws IOException {
        InputStream inputStream = s3Service.downloadFile(getKey(event));
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                CSVReader csvReader = new CSVReader(reader)
        ) {

            String[] headers = csvReader.readNext();
            List<FileRecord> records = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                FileRecord record = getFileRecord(event, headers, line);


                records.add(record);
                if(records.size() >= batchSize) {
                    repository.saveAll(records);
                    records.clear();
                }
            }
            if(!records.isEmpty()) {
                repository.saveAll(records);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    private static FileRecord getFileRecord(FileUploadEvent event, String[] headers, String[] line) {
        Map<String, String> rowData = new HashMap<>();
        for (int i = 0; i < headers.length && i < line.length; i++) {
            String header = headers[i].trim();
            if(header.isEmpty()) continue;
            rowData.put(headers[i], line[i]);
        }

        FileRecord record = new FileRecord();
        record.setUserId(event.getUserId());
        record.setCampaignId(event.getCampaignId());
        record.setFileName(event.getFilename());
        record.setPhoneNumber(rowData.get("Phone"));
        record.setData(rowData);
        return record;
    }

    private static String getKey(FileUploadEvent event) {
        return event.getUserId() + "/" + event.getCampaignId();
    }

    public void deleteAllRecords(String userId, String campaignId) {
        fileRecordRepository.deleteByUserIdAndCampaignId(userId,campaignId);
    }

    public CampaignRecordList getRecords(String userId, String campaignId) {
        List<FileRecord> fileRecord = fileRecordRepository.getByUserIdAndCampaignId(userId,campaignId);
        List<CampaignRecord> campaignRecords = fileRecord.stream().
                map(FileRecordService::createCampaignRecordObject).toList();

        CampaignRecordList campaignRecordList = new CampaignRecordList();
        campaignRecordList.setCampaignRecordList(campaignRecords);
        campaignRecordList.setCount(campaignRecords.size());
        return campaignRecordList;
    }

    private static CampaignRecord createCampaignRecordObject(FileRecord record) {
        CampaignRecord campaignRecord = new CampaignRecord();
        campaignRecord.setUserId(record.getUserId());
        campaignRecord.setCampaignId(record.getCampaignId());
        campaignRecord.setPhoneNumber(record.getPhoneNumber());
        campaignRecord.setData(record.getData());
        return campaignRecord;
    }
}
