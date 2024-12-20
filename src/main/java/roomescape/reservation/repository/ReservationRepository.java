package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Schedule;

@Repository
public interface ReservationRepository extends ListCrudRepository<Reservation, Long> {
    List<Reservation> findByMemberId(Long memberId);

    Optional<Reservation> findByScheduleDateAndScheduleTimeIdAndScheduleThemeId(
            LocalDate date, Long timeId, Long themeId);

    boolean existsByScheduleAndMember(Schedule schedule, Member member);

    @Query("""
            SELECT r FROM Reservation AS r
            WHERE (:themeId IS NULL OR r.schedule.theme.id = :themeId)
            AND (:memberId IS NULL OR r.member.id = :memberId)
            AND (:startDate IS NULL OR r.schedule.date >= :startDate)
            AND (:endDate IS NULL OR r.schedule.date <= :endDate)
            """)
    List<Reservation> findByCondition(Long memberId, Long themeId, LocalDate startDate, LocalDate endDate);
}
