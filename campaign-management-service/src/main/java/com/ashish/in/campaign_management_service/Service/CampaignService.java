package com.ashish.in.campaign_management_service.Service;

import com.ashish.in.campaign_management_service.Dto.CampaignListResponse;
import com.ashish.in.campaign_management_service.Dto.CampaignRequest;
import com.ashish.in.campaign_management_service.Dto.CampaignResponse;
import com.ashish.in.campaign_management_service.Dto.UpdateCampaignResource;
import com.ashish.in.campaign_management_service.Entity.Campaign;
import com.ashish.in.campaign_management_service.Enum.CampaignStatus;
import com.ashish.in.campaign_management_service.Exception.NotFoundException;
import com.ashish.in.campaign_management_service.Repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignResponse createCampaign(CampaignRequest campaignRequest){
        log.info("Campaign creation started for the userId:{}", campaignRequest.getUserId());
        Campaign campaign = createCampaignObject(campaignRequest);

        campaignRepository.save(campaign);

        CampaignResponse campaignResponse = createCampaignResponse(campaign);

        log.info("Campaign created successfully for the userId:{} with campaignId:{}", campaignRequest.getUserId(),campaign.getCampaignId());
        return campaignResponse;
    }

    private static CampaignResponse createCampaignResponse(Campaign campaign) {
        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCampaignName(campaign.getName());
        campaignResponse.setUserId(campaign.getUserId());
        campaignResponse.setCampaignId(campaign.getCampaignId());
        campaignResponse.setCampaignStatus(CampaignStatus.CREATED);
        campaignResponse.setCreatedAt(campaign.getCreatedAt());
        return campaignResponse;
    }

    private static Campaign createCampaignObject(CampaignRequest campaignRequest) {
        Campaign campaign = new Campaign();
        campaign.setCampaignId(UUID.randomUUID().toString());
        campaign.setUserId(campaignRequest.getUserId());
        campaign.setName(campaignRequest.getName());
        campaign.setMessage(campaignRequest.getMessage());
        campaign.setCampaignStatus(CampaignStatus.CREATED);
        campaign.setCreatedAt(LocalDateTime.now());
        return campaign;
    }

    public CampaignListResponse getAllCampaigns(String userId) {
       List<Campaign> campaignList = campaignRepository.getCampaignsByUserId(userId);
       List<CampaignResponse> campaignResponseList = campaignList.stream().map(
               CampaignService::createCampaignResponse).toList();
       CampaignListResponse campaignListResponse = new CampaignListResponse();
       campaignListResponse.setList(campaignResponseList);
       campaignListResponse.setCount(campaignList.size());
        return campaignListResponse;
    }

    public CampaignResponse getCampaign(String userId, String campaignId) {
        Campaign campaign = campaignRepository.getCampaignsByUserIdAndCampaignId(userId, campaignId);
        return createCampaignResponse(campaign);
    }

    @Transactional
    public void deleteCampaign(String userId, String campaignId) {
        campaignRepository.deleteByUserIdAndCampaignId(userId, campaignId);
    }

    public CampaignResponse updateCampaign(String userId, String campaignId, UpdateCampaignResource request) {
        Campaign campaign = campaignRepository.getCampaignsByUserIdAndCampaignId(userId, campaignId);
        if(campaign == null) {
            throw new ResourceNotFoundException("Campaign not found");
        }
        if(request.getName() != null) {
            campaign.setName(request.getName());
        }
        if(request.getMessage() != null) {
            campaign.setMessage(request.getMessage());
        }
        if(request.getScheduledAt() != null) {
            campaign.setScheduledAt(request.getScheduledAt());
        }
        campaign.setUpdatedAt(LocalDateTime.now());
        campaignRepository.save(campaign);
        return createCampaignResponse(campaign);
    }
}
