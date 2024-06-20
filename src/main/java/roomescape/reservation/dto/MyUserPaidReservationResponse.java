package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.payment.domain.Payment;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Schedule;

public class MyUserPaidReservationResponse extends MyReservationResponse2 {
    private static final String STATUS = "예약 완료";

    private final Long id;
    private final String themeName;
    private final String status;
    private final String paymentKey;
    private final BigDecimal amount;

    private MyUserPaidReservationResponse(Long id, String themeName, LocalDate date, LocalTime startAt,
                                          String paymentKey, BigDecimal amount) {
        super(date, startAt);
        this.id = id;
        this.themeName = themeName;
        this.status = STATUS;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }

    public static MyReservationResponse2 from(Reservation reservation, Payment payment) {
        Schedule schedule = reservation.getSchedule();
        return new MyUserPaidReservationResponse(
                reservation.getId(),
                schedule.getTheme().getName(),
                schedule.getDate(),
                schedule.getTime().getStartAt(),
                payment.getPaymentKey(),
                payment.getAmount()
        );
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

    public String getPaymentKey() {
        return paymentKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MyUserPaidReservationResponse that = (MyUserPaidReservationResponse) object;
        return Objects.equals(id, that.id) && Objects.equals(themeName, that.themeName)
                && Objects.equals(status, that.status) && Objects.equals(paymentKey, that.paymentKey)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, themeName, status, paymentKey, amount);
    }
}
