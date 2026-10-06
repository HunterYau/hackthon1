package hackathon1.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record Experience(
        String id,
        String title,
        ExperienceType type,
        LocalDate date,
        double hours,
        String description,
        boolean verified
) {
    public Experience {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
        Objects.requireNonNull(type);
        Objects.requireNonNull(date);
        Objects.requireNonNull(description);
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (hours < 0) {
            throw new IllegalArgumentException("Hours cannot be negative");
        }
    }

    public static Experience create(String title, ExperienceType type, LocalDate date,
                                    double hours, String description) {
        return new Experience(UUID.randomUUID().toString(), title.trim(), type, date,
                hours, description.trim(), false);
    }

    public Experience withVerified(boolean value) {
        return new Experience(id, title, type, date, hours, description, value);
    }
}
