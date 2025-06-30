package com.ashish.in.campaign_management_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignListResponse {
    private List<CampaignResponse> list;
    private int count;
}
