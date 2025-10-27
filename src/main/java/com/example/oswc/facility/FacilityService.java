package com.example.oswc.facility;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.facility.dto.FacilityResponse;
import com.example.oswc.facility.dto.FacilitySearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacilityService {

  private final FacilityRepository facilityRepository;

  public FacilityService(FacilityRepository facilityRepository) {
    this.facilityRepository = facilityRepository;
  }

  /**
   * 주변 시설 검색 (명세서 기준)
   */
  public ApiResponse<?> searchFacilities(FacilitySearchRequest request) {
    // 좌표 생성 (GeoJSON: 경도, 위도 순서)
    GeoJsonPoint location = new GeoJsonPoint(request.getLongitude(), request.getLatitude());

    // 페이징 설정 (page는 1-based, Spring은 0-based)
    Pageable pageable = PageRequest.of(request.getPageForPagination(), request.getLimit());

    // 운영중 여부 필터
    Boolean isOperating = request.getOpenNow() ? true : null;

    // MongoDB GeoNear 쿼리 실행
    Page<FacilityDocument> facilitiesPage = facilityRepository.findByLocationNear(
        location,
        request.getRadius(),
        request.getType(),
        isOperating,
        pageable);

    // Response로 변환
    List<FacilityResponse> facilities = facilitiesPage.getContent().stream()
        .map(this::toResponse)
        .collect(Collectors.toList());

    // 명세서 응답 구조에 맞춰 생성
    Map<String, Object> responseData = new HashMap<>();
    
    // Pagination 정보
    Map<String, Object> pagination = new HashMap<>();
    pagination.put("currentPage", facilitiesPage.getNumber() + 1); // 0-based → 1-based
    pagination.put("totalPages", facilitiesPage.getTotalPages());
    pagination.put("totalItems", facilitiesPage.getTotalElements());
    
    responseData.put("pagination", pagination);
    responseData.put("facilities", facilities);

    return ApiResponse.ok(responseData);
  }

  /**
   * Document를 Response로 변환 (명세서 형식)
   */
  private FacilityResponse toResponse(FacilityDocument doc) {
    FacilityResponse response = new FacilityResponse();
    response.setId(doc.getId());
    response.setName(doc.getName());
    response.setType(doc.getType());
    response.setAddress(doc.getAddress());
    response.setPhone(doc.getPhone());
    
    // Location 객체 생성
    FacilityResponse.LocationInfo location = new FacilityResponse.LocationInfo(
        doc.getLatitude(),
        doc.getLongitude()
    );
    response.setLocation(location);
    
    // 거리 정보 (TODO: 실제 계산된 거리)
    response.setDistanceMeters(0);
    
    // 운영중 여부
    response.setIsOpen(doc.getIsOperating());
    
    // 운영시간 문자열 생성
    if (doc.getOpenTime() != null && doc.getCloseTime() != null) {
      String operatingHours = doc.getOpenTime().toString() + " - " + doc.getCloseTime().toString();
      response.setOperatingHoursToday(operatingHours);
    } else {
      response.setOperatingHoursToday("24시간 운영 (응급실)");
    }
    
    return response;
  }
}
