package com.example.oswc.review.api;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.review.ReviewService;
import com.example.oswc.review.dto.CreateReviewRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReviewController {

  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  /** POST /reservations/{id}/reviews - 후기 작성 */
  @PostMapping("/{id}/reviews")
  public ResponseEntity<?> createReview(
      Authentication auth, @PathVariable String id, @Valid @RequestBody CreateReviewRequest req) {
    String userId = auth.getName();
    ApiResponse<?> resp = reviewService.createReview(id, userId, req);
    return ResponseEntity.status(201).body(resp);
  }
}
