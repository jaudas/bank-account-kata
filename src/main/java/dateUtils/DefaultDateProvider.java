package dateUtils;

import java.time.Instant;

public class DefaultDateProvider implements DateProvider {
    @Override
    public Instant getCurrentTime() {
        return Instant.now();
    }
}
