package roomescape.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Schedule;

public class MyAdminReservationResponse extends MyReservationResponse2 {
    private static final String STATUS = "어드민 예약 완료";

    private final Long id;
    private final String themeName;
    private final String status;

    private MyAdminReservationResponse(Long id, String themeName, LocalDate date, LocalTime startAt) {
        super(date, startAt);
        this.id = id;
        this.themeName = themeName;
        this.status = STATUS;
    }

    public static MyReservationResponse2 from(Reservation reservation) {
        Schedule schedule = reservation.getSchedule();
        return new MyAdminReservationResponse(
                reservation.getId(),
                schedule.getTheme().getName(),
                schedule.getDate(),
                schedule.getTime().getStartAt());
    }

    public Long getId() {
        return id;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MyAdminReservationResponse that = (MyAdminReservationResponse) object;
        return Objects.equals(id, that.id) && Objects.equals(themeName, that.themeName)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, themeName, status);
    }
}
