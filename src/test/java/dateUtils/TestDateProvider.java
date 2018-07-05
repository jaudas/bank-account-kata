package dateUtils;

import java.time.Instant;

public class TestDateProvider implements DateProvider {

    private Instant date;
    final private Long offsetSeconds;

    public TestDateProvider(Instant date, Long offsetInSeconds) {
        this.date = date;
        this.offsetSeconds = offsetInSeconds;
    }

    @Override
    public Instant getCurrentTime() {
        date = date.plusSeconds(offsetSeconds);

        return date;
    }
}
