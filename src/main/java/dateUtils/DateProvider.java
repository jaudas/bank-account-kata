package dateUtils;

import java.time.Instant;

public interface DateProvider {

    Instant getCurrentTime();
}
