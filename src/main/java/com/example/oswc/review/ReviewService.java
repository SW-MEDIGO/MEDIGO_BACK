package com.example.oswc.review;

import com.example.oswc.common.ApiResponse;
import com.example.oswc.reservation.ReservationDocument;
import com.example.oswc.reservation.ReservationRepository;
import com.example.oswc.reservation.ReservationStatus;
import com.example.oswc.review.dto.CreateReviewRequest;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ReservationRepository reservationRepository;

  public ReviewService(
      ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
    this.reviewRepository = reviewRepository;
    this.reservationRepository = reservationRepository;
  }

  public ApiResponse<?> createReview(String reservationId, String userId, CreateReviewRequest req) {
    // 예약 확인
    ReservationDocument r =
        reservationRepository
            .findById(reservationId)
            .orElseThrow(() -> new RuntimeException("해당 예약을 찾을 수 없습니다."));

    if (!r.getUserId().equals(userId)) {
      throw new RuntimeException("해당 예약 정보를 조회할 권한이 없습니다.");
    }
    if (r.getStatus() != ReservationStatus.COMPLETED) {
      throw new RuntimeException("완료된 예약에 대해서만 후기를 작성할 수 있습니다.");
    }
    // 중복 리뷰 방지
    if (reviewRepository.findByReservationId(reservationId).isPresent()) {
      throw new IllegalArgumentException("REVIEW_ALREADY_EXISTS");
    }

    ReviewDocument d = new ReviewDocument();
    d.setReservationId(reservationId);
    d.setUserId(userId);
    d.setManagerId(r.getManagerId());
    d.setRating(req.getRating());
    d.setComment(req.getComment());
    d.setCreatedAt(Instant.now());
    reviewRepository.save(d);

    return ApiResponse.ok(Map.of("message", "소중한 후기를 남겨주셔서 감사합니다."));
  }
}
