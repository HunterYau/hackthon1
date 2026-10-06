package hackathon1.service;

import hackathon1.data.PortfolioRepository;
import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PortfolioService {
    public enum SortMode {
        NEWEST("Newest first"), OLDEST("Oldest first"), CATEGORY("Category");

        private final String label;
        SortMode(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    private final PortfolioRepository repository;
    private final List<Experience> experiences;

    public PortfolioService(PortfolioRepository repository) {
        this.repository = repository;
        this.experiences = new ArrayList<>(repository.load());
    }

    public List<Experience> all() {
        return List.copyOf(experiences);
    }

    public List<Experience> find(String query, ExperienceType type, SortMode sortMode) {
        String normalized = query == null ? "" : query.strip().toLowerCase(Locale.ROOT);
        Comparator<Experience> comparator = switch (sortMode) {
            case OLDEST -> Comparator.comparing(Experience::date);
            case CATEGORY -> Comparator.comparing((Experience item) -> item.type().label())
                    .thenComparing(Experience::date).reversed();
            case NEWEST -> Comparator.comparing(Experience::date).reversed();
        };

        return experiences.stream()
                .filter(item -> type == null || item.type() == type)
                .filter(item -> normalized.isEmpty()
                        || item.title().toLowerCase(Locale.ROOT).contains(normalized)
                        || item.description().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(comparator)
                .toList();
    }

    public void add(Experience experience) {
        experiences.add(experience);
        repository.save(experiences);
    }

    public void verify(String id) {
        for (int i = 0; i < experiences.size(); i++) {
            Experience item = experiences.get(i);
            if (item.id().equals(id)) {
                experiences.set(i, item.withVerified(true));
                repository.save(experiences);
                return;
            }
        }
    }

    public double totalHours() {
        return experiences.stream().mapToDouble(Experience::hours).sum();
    }

    public long verifiedCount() {
        return experiences.stream().filter(Experience::verified).count();
    }

    public List<Experience> pending() {
        return experiences.stream()
                .filter(item -> !item.verified())
                .sorted(Comparator.comparing(Experience::date).reversed())
                .toList();
    }

    public Set<String> badges() {
        Set<String> badges = new LinkedHashSet<>();
        if (!experiences.isEmpty()) badges.add("First Step");
        double volunteerHours = experiences.stream()
                .filter(item -> item.type() == ExperienceType.VOLUNTEERING)
                .mapToDouble(Experience::hours).sum();
        if (volunteerHours >= 10) badges.add("Community Builder");
        if (experiences.stream().map(Experience::type).distinct().count() >= 4) badges.add("All-Rounder");
        if (verifiedCount() >= 3) badges.add("Verified Story");
        return badges;
    }

    public String buildResumeText() {
        StringBuilder text = new StringBuilder();
        text.append("MAYA CHEN - HIGH SCHOOL EXPERIENCE PORTFOLIO\n\n");
        for (Experience item : find("", null, SortMode.CATEGORY)) {
            text.append(item.type().label().toUpperCase(Locale.ROOT)).append(" | ")
                    .append(item.title()).append(" | ")
                    .append(item.date().format(DateTimeFormatter.ofPattern("MMM yyyy")));
            if (item.hours() > 0) text.append(" | ").append(formatHours(item.hours())).append(" hours");
            if (item.verified()) text.append(" | Counselor verified");
            text.append('\n').append(item.description()).append("\n\n");
        }
        return text.toString();
    }

    public static String formatHours(double hours) {
        return hours == Math.rint(hours) ? String.format(Locale.ROOT, "%.0f", hours)
                : String.format(Locale.ROOT, "%.1f", hours);
    }
}
