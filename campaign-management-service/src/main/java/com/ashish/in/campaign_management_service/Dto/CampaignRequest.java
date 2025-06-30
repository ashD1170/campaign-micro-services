package com.ashish.in.campaign_management_service.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequest {

    @NotBlank(message = "Campaign name must not be Blank")
    private String name;
    @NotNull(message = "UserId is required")
    private String userId;
    @NotBlank(message = "message must not be Blank")
    private String message;

}
