package com.example.oswc.facility.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.facility.FacilityService;
import com.example.oswc.facility.dto.FacilitySearchRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facilities")
public class FacilitiesController {

  private final FacilityService facilityService;

  public FacilitiesController(FacilityService facilityService) {
    this.facilityService = facilityService;
  }

  /**
   * 시설 검색 (병원/약국)
   * 
   * @param request 검색 요청 (위도, 경도, 유형, 운영 여부 등)
   * @return 주변 시설 목록 (페이징)
   */
  @GetMapping("/search")
  public ResponseEntity<?> searchFacilities(@Valid FacilitySearchRequest request) {
    ApiResponse<?> response = facilityService.searchFacilities(request);
    return ResponseEntity.ok(response);
  }
}

