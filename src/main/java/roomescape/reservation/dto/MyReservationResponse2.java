package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class MyReservationResponse2 {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startAt;

    protected MyReservationResponse2(LocalDate date, LocalTime startAt) {
        this.date = date;
        this.startAt = startAt;
    }

    public final LocalDate getDate() {
        return date;
    }

    public final LocalTime getStartAt() {
        return startAt;
    }
}
