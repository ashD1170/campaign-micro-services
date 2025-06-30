package com.ashish.in.campaign_management_service.Dto;

import com.ashish.in.campaign_management_service.Enum.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignResponse {
    private String campaignName;
    private String campaignId;
    private String userId;
    private CampaignStatus campaignStatus;
    private LocalDateTime createdAt;
}
