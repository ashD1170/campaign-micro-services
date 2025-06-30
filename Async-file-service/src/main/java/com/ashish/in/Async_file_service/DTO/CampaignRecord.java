package com.ashish.in.Async_file_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRecord {
    private String userId;
    private String campaignId;
    private String phoneNumber;
    private Map<String,String> data;
}
