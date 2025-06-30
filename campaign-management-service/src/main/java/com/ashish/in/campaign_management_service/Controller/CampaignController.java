package com.ashish.in.campaign_management_service.Controller;

import com.ashish.in.campaign_management_service.Dto.*;
import com.ashish.in.campaign_management_service.Entity.Campaign;
import com.ashish.in.campaign_management_service.Service.CampaignService;
import com.ashish.in.campaign_management_service.Service.FileProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CampaignController {
    //publish campaign

    private final CampaignService campaignService;

    private final FileProcessingService fileProcessingService;


    //Create Campaign
    @PostMapping("/campaign")
    public ResponseEntity<CampaignResponse> createCampaign(@Valid @RequestBody CampaignRequest campaignRequest){
        CampaignResponse campaignResponse = campaignService.createCampaign(campaignRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignResponse);
    }

    //Upload File
    @PostMapping("/file/upload")
    public ResponseEntity<FileUploadEvent> uploadFile(@RequestParam MultipartFile file,
                                                      @RequestParam String userId,
                                                      @RequestParam String campaignId) throws IOException {
        fileProcessingService.validateFileExtension(file);
        FileUploadEvent fileUploadEvent = fileProcessingService.uploadUserFile(file,userId,campaignId);
        return ResponseEntity.status(HttpStatus.OK).body(fileUploadEvent);
    }

    //List All campaign for user
    @GetMapping("/user/{userId}/campaign")
    public ResponseEntity<CampaignListResponse> listCampaignsForUser(@PathVariable String userId){
        CampaignListResponse campaignList = campaignService.getAllCampaigns(userId);
        return ResponseEntity.status(HttpStatus.OK).body(campaignList);
    }

    //get a specific campaign for the user
    @GetMapping("/user/{userId}/campaign/{campaignId}")
    public ResponseEntity<CampaignResponse> listCampaignsForUser(@PathVariable String userId,@PathVariable String campaignId){
        CampaignResponse campaignResponse = campaignService.getCampaign(userId,campaignId);
        return ResponseEntity.status(HttpStatus.OK).body(campaignResponse);
    }

    //delete campaign
    @DeleteMapping("/user/{userId}/campaign/{campaignId}")
    public ResponseEntity<?> deleteCampaign(@PathVariable String userId,@PathVariable String campaignId){
        campaignService.deleteCampaign(userId,campaignId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/user/{userId}/campaign/{campaignId}")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable String userId,
                                            @PathVariable String campaignId,
                                            @RequestBody UpdateCampaignResource request){
        CampaignResponse campaignResponse = campaignService.updateCampaign(userId,campaignId,request);
        return ResponseEntity.status(HttpStatus.OK).body(campaignResponse);
    }



}
