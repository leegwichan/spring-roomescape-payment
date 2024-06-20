package roomescape.reservation.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.payment.domain.PaymentStatus;
import roomescape.payment.repository.PaymentRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.MyAdminReservationResponse;
import roomescape.reservation.dto.MyReservationResponse2;
import roomescape.reservation.dto.MyUserNonPaidReservationResponse;
import roomescape.reservation.dto.MyUserPaidReservationResponse;
import roomescape.reservation.dto.MyWaitingResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.repository.WaitingRepository;

@Service
public class ReservationFindMineService {
    private static final Comparator<MyReservationResponse2> RESERVATION_SORTING_COMPARATOR = Comparator
            .comparing(MyReservationResponse2::getDate).thenComparing(MyReservationResponse2::getStartAt);

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final PaymentRepository paymentRepository;

    public ReservationFindMineService(ReservationRepository reservationRepository,
                                      WaitingRepository waitingRepository,
                                      PaymentRepository paymentRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public List<MyReservationResponse2> findMyReservations(Long memberId) {
        List<MyReservationResponse2> reservations = findReservations(memberId);
        List<MyReservationResponse2> waitings = findWaitings(memberId);

        return makeMyReservations(reservations, waitings);
    }

    private List<MyReservationResponse2> findReservations(Long memberId) {
        return reservationRepository.findByMemberId(memberId)
                .stream()
                .map(this::createResponse)
                .toList();
    }

    private MyReservationResponse2 createResponse(Reservation reservation) {
        return paymentRepository.findByScheduleAndMemberAndStatus(
                        reservation.getSchedule(), reservation.getMember(), PaymentStatus.PAID)
                .map(payment -> MyUserPaidReservationResponse.from(reservation, payment))
                .orElse(createNonPaidResponse(reservation));
    }

    private MyReservationResponse2 createNonPaidResponse(Reservation reservation) {
        if (reservation.isAdminReserved()) {
            return MyAdminReservationResponse.from(reservation);
        }
        return MyUserNonPaidReservationResponse.from(reservation);
    }

    private List<MyReservationResponse2> findWaitings(Long memberId) {
        return waitingRepository.findByMemberId(memberId)
                .stream()
                .map(waiting -> MyWaitingResponse.from(waiting, countOrderOfWaiting(waiting)))
                .toList();
    }

    private Long countOrderOfWaiting(Waiting waiting) {
        return waitingRepository.countByScheduleAndCreatedAtLessThanEqual(
                waiting.getSchedule(), waiting.getCreatedAt());
    }

    private List<MyReservationResponse2> makeMyReservations(List<MyReservationResponse2> reservations,
                                                           List<MyReservationResponse2> waitings) {
        List<MyReservationResponse2> response = new ArrayList<>();
        response.addAll(reservations);
        response.addAll(waitings);
        response.sort(RESERVATION_SORTING_COMPARATOR);
        return response;
    }
}
