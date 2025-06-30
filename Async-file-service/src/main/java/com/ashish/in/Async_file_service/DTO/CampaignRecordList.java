package com.ashish.in.Async_file_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRecordList {
    private List<CampaignRecord> campaignRecordList;
    private long count;
}
