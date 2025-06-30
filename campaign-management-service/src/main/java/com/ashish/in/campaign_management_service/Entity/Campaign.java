package com.ashish.in.campaign_management_service.Entity;

import com.ashish.in.campaign_management_service.Enum.CampaignStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campaign")
public class Campaign {

    @Id
    @Column(length = 36)
    private String campaignId;
    @Column(nullable = false,length = 36)
    private String userId;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private CampaignStatus campaignStatus;


}
