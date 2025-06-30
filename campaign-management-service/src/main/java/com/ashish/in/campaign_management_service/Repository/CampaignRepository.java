package com.ashish.in.campaign_management_service.Repository;

import com.ashish.in.campaign_management_service.Entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign,Long> {
    List<Campaign> getCampaignsByUserId(String userId);
    Campaign getCampaignsByUserIdAndCampaignId(String userId, String campaignId);
    void deleteByUserIdAndCampaignId(String userId, String campaignId);
}
