package roomescape.time.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.ReservationTime;

@Repository
public interface TimeRepository extends ListCrudRepository<ReservationTime, Long> {

    @Query("""
            SELECT t FROM Reservation AS r
            INNER JOIN r.schedule.time AS t
            WHERE r.schedule.date = :date
            AND r.schedule.theme.id = :themeId
            """)
    List<ReservationTime> findByReservationDateAndThemeId(LocalDate date, Long themeId);
}
