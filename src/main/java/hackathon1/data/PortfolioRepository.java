package hackathon1.data;

import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PortfolioRepository {
    private final Path file;

    public PortfolioRepository(Path file) {
        this.file = file;
    }

    public List<Experience> load() {
        if (file == null || Files.notExists(file)) {
            return sampleData();
        }
        try {
            List<Experience> experiences = new ArrayList<>();
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\t", -1);
                if (parts.length != 7) {
                    continue;
                }
                experiences.add(new Experience(
                        parts[0], decode(parts[1]), ExperienceType.valueOf(parts[2]),
                        LocalDate.parse(parts[3]), Double.parseDouble(parts[4]),
                        decode(parts[5]), Boolean.parseBoolean(parts[6])));
            }
            return experiences.isEmpty() ? sampleData() : experiences;
        } catch (RuntimeException | IOException exception) {
            return sampleData();
        }
    }

    public void save(List<Experience> experiences) {
        if (file == null) {
            return;
        }
        try {
            Files.createDirectories(file.getParent());
            List<String> lines = new ArrayList<>();
            lines.add("# Pathfolio local data v1");
            for (Experience experience : experiences) {
                lines.add(String.join("\t",
                        experience.id(), encode(experience.title()), experience.type().name(),
                        experience.date().toString(), Double.toString(experience.hours()),
                        encode(experience.description()), Boolean.toString(experience.verified())));
            }
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save portfolio data", exception);
        }
    }

    private static String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }

    public static List<Experience> sampleData() {
        return new ArrayList<>(List.of(
                new Experience("robotics", "Robotics Club Programmer", ExperienceType.CLUB,
                        LocalDate.of(2026, 9, 12), 42, "Built autonomous routines and mentored new members.", true),
                new Experience("food-drive", "Community Food Drive", ExperienceType.VOLUNTEERING,
                        LocalDate.of(2026, 5, 18), 12.5, "Sorted donations and coordinated pickup stations.", true),
                new Experience("app-project", "Study Planner App", ExperienceType.PROJECT,
                        LocalDate.of(2026, 3, 4), 24, "Designed and tested a Java study planner with classmates.", false),
                new Experience("award", "Regional Coding Finalist", ExperienceType.AWARD,
                        LocalDate.of(2025, 11, 8), 0, "Placed in the top ten at the regional programming contest.", false),
                new Experience("soccer", "Junior Varsity Soccer", ExperienceType.SPORT,
                        LocalDate.of(2025, 8, 20), 68, "Practiced four days a week and supported team fundraising.", true)
        ));
    }
}
