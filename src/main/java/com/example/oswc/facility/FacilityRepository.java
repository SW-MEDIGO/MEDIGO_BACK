package com.example.oswc.facility;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FacilityRepository extends MongoRepository<FacilityDocument, String> {

  /**
   * 위치 기반 검색 (반경 내 시설 조회)
   *
   * @param location 중심 좌표
   * @param radius 반경 (미터 단위)
   * @param type 시설 유형 (선택)
   * @param isOperating 운영 여부 (선택)
   * @param pageable 페이징
   * @return 시설 목록
   */
  @Query(
      value =
          "{'location': {$near: {$geometry: ?0, $maxDistance: ?1}}, "
              + "'type': ?#{?2 != null ? {$eq: ?2} : {$exists: true}}, "
              + "'isOperating': ?#{?3 != null ? {$eq: ?3} : {$exists: true}}}")
  Page<FacilityDocument> findByLocationNear(
      GeoJsonPoint location,
      double radius,
      FacilityType type,
      Boolean isOperating,
      Pageable pageable);
}
