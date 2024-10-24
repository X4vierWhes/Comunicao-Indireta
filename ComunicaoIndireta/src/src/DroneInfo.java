package src;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


public record DroneInfo(
        double temperature,
        double radiation,
        double atmospherePressure,
        double moisture,
        LocalDateTime timestamp,
        int id
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
