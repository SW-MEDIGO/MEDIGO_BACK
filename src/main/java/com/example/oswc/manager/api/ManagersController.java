package com.example.oswc.manager.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.review.ReviewDocument;
import com.example.oswc.review.ReviewRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers")
public class ManagersController {

  private final ReviewRepository reviewRepository;

  public ManagersController(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  /** GET /managers/{id} - 매니저 프로필 상세(후기 포함 요약) */
  @GetMapping("/{id}")
  public ResponseEntity<?> getManagerProfile(@PathVariable String id) {
    // 인증/프로필 값은 간소화: name/is_verified/profile_image_url 등은 추후 User/Manager 컬렉션 연동
    List<ReviewDocument> reviews = reviewRepository.findByManagerId(id);
    double avg =
        reviews.isEmpty()
            ? 0.0
            : reviews.stream().mapToInt(ReviewDocument::getRating).average().orElse(0.0);
    int count = reviews.size();

    List<Map<String, Object>> latest =
        reviewRepository.findByManagerIdOrderByCreatedAtDesc(id).stream()
            .limit(2)
            .map(
                r -> {
                  Map<String, Object> m = new java.util.HashMap<>();
                  m.put("reviewer_name", "이*정");
                  m.put("rating", r.getRating());
                  m.put("comment", r.getComment());
                  return m;
                })
            .collect(Collectors.toList());

    Map<String, Object> data = new java.util.HashMap<>();
    data.put("id", id);
    data.put("name", "매니저명");
    data.put("profile_image_url", null);
    data.put("is_verified", true);
    data.put("introduction", "마음 편한 동행을 약속드립니다.");
    data.put("average_rating", Math.round(avg * 10.0) / 10.0);
    data.put("review_count", count);
    data.put("latest_reviews", latest);

    return ResponseEntity.ok(ApiResponse.ok(data));
  }
}
