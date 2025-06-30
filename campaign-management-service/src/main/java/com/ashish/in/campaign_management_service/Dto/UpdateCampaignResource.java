package com.ashish.in.campaign_management_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCampaignResource {
    private String name;
    private String message;
    private LocalDateTime scheduledAt;
}
