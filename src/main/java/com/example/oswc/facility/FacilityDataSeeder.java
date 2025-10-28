package com.example.oswc.facility;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * 시설 샘플 데이터 생성 (개발/테스트용)
 */
@Component
public class FacilityDataSeeder {

  private static final Logger log = LoggerFactory.getLogger(FacilityDataSeeder.class);

  @Autowired
  private FacilityRepository facilityRepository;

  @PostConstruct
  public void seed() {
    if (facilityRepository.count() > 0) {
      log.info("시설 데이터가 이미 존재합니다. 스킵합니다.");
      return;
    }

    log.info("시설 샘플 데이터 생성 중...");

    List<FacilityDocument> facilities = Arrays.asList(
        // 서울 강남구 병원들
        createFacility("서울대학교병원", FacilityType.HOSPITAL, 37.5665, 127.0308, "서울특별시 종로구 대학로 101", "", "02-2072-2114", true, LocalTime.of(9, 0), LocalTime.of(18, 0)),
        createFacility("세브란스병원", FacilityType.HOSPITAL, 37.5625, 126.9398, "서울특별시 서대문구 신촌로 50", "", "02-2225-5800", true, LocalTime.of(9, 0), LocalTime.of(17, 0)),
        createFacility("서울아산병원", FacilityType.HOSPITAL, 37.5267, 127.1033, "서울특별시 송파구 올림픽로43길 88", "", "02-3010-3114", true, LocalTime.of(8, 30), LocalTime.of(17, 30)),
        
        // 강남구 약국들
        createFacility("강남메디팜", FacilityType.PHARMACY, 37.4979, 127.0276, "서울특별시 강남구 테헤란로 152", "1층", "02-558-8876", true, LocalTime.of(9, 0), LocalTime.of(21, 0)),
        createFacility("바이오약국", FacilityType.PHARMACY, 37.5012, 127.0273, "서울특별시 강남구 테헤란로 124", "지하1층", "02-556-0123", true, LocalTime.of(9, 0), LocalTime.of(22, 0)),
        createFacility("올림픽약국", FacilityType.PHARMACY, 37.5153, 127.1026, "서울특별시 송파구 올림픽로 300", "지하2층", "02-423-4567", true, LocalTime.of(8, 30), LocalTime.of(20, 0)),
        
        // 종로구 병원/약국
        createFacility("고려대학교병원", FacilityType.HOSPITAL, 37.5892, 127.0254, "서울특별시 종로구 새문안로 28", "", "02-2626-1114", true, LocalTime.of(9, 0), LocalTime.of(18, 0)),
        createFacility("종로백화점약국", FacilityType.PHARMACY, 37.5702, 126.9925, "서울특별시 종로구 종로 1가", "지하1층", "02-1234-5678", true, LocalTime.of(10, 0), LocalTime.of(22, 0)),
        
        // 영등포구
        createFacility("삼성서울병원", FacilityType.HOSPITAL, 37.4880, 127.0874, "서울특별시 강남구 일원로 81", "", "02-3410-2114", true, LocalTime.of(9, 0), LocalTime.of(17, 0)),
        createFacility("영등포약국", FacilityType.PHARMACY, 37.5172, 126.9014, "서울특별시 영등포구 당산로 123", "", "02-2638-9876", true, LocalTime.of(9, 0), LocalTime.of(21, 0))
    );

    facilityRepository.saveAll(facilities);
    log.info("시설 샘플 데이터 {}개 생성 완료", facilities.size());
  }

  private FacilityDocument createFacility(String name, FacilityType type, double lat, double lng, 
      String address, String detailAddress, String phone, boolean isOperating, 
      LocalTime openTime, LocalTime closeTime) {
    
    FacilityDocument facility = new FacilityDocument();
    facility.setName(name);
    facility.setType(type);
    facility.setLatitude(lat);
    facility.setLongitude(lng);
    facility.setAddress(address);
    facility.setDetailAddress(detailAddress);
    facility.setPhone(phone);
    facility.setIsOperating(isOperating);
    facility.setOpenTime(openTime);
    facility.setCloseTime(closeTime);
    facility.setLocation(new org.springframework.data.mongodb.core.geo.GeoJsonPoint(lng, lat));
    
    return facility;
  }
}

