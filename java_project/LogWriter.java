import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogWriter {
    // Updated log file path to user_inputs.txt for clarity
    private static final String LOG_FILE = "data/user_inputs.txt";
    private static final Path LOG_PATH = Paths.get(LOG_FILE);
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            Path dir = LOG_PATH.getParent();
            if (dir != null && !Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            if (!Files.exists(LOG_PATH)) {
                Files.createFile(LOG_PATH);
                Files.write(LOG_PATH, (
                    "# User Data Input Log\n" +
                    "# Each line: [timestamp] action | key=value; key=value; ...\n\n"
                ).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not initialize log file: " + e.getMessage());
        }
    }

    public static synchronized void append(String action, String details) {
        String line = String.format("[%s] %s | %s%n", LocalDateTime.now().format(TS), action, details);
        try {
            Files.write(LOG_PATH, line.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Warning: Could not write to log file: " + e.getMessage());
        }
    }
}
