package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.reservation.domain.Schedule;
import roomescape.waiting.domain.Waiting;

public class MyWaitingResponse extends MyReservationResponse2 {
    private static final String STATUS_FORMAT = "%d번째 예약 대기";

    private final String themeName;
    private final String status;
    private final Long waitingId;

    private MyWaitingResponse(String themeName, LocalDate date, LocalTime startAt, String status, Long waitingId) {
        super(date, startAt);
        this.themeName = themeName;
        this.status = status;
        this.waitingId = waitingId;
    }

    public static MyReservationResponse2 from(Waiting waiting, Long order) {
        String status = STATUS_FORMAT.formatted(order);
        Schedule schedule = waiting.getSchedule();
        return new MyWaitingResponse(
                schedule.getTheme().getName(),
                schedule.getDate(),
                schedule.getTime().getStartAt(),
                status,
                waiting.getId());
    }

    public String getThemeName() {
        return themeName;
    }

    public String getStatus() {
        return status;
    }

    public Long getWaitingId() {
        return waitingId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MyWaitingResponse that = (MyWaitingResponse) object;
        return Objects.equals(themeName, that.themeName) && Objects.equals(status, that.status)
                && Objects.equals(waitingId, that.waitingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(themeName, status, waitingId);
    }
}
