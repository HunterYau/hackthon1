package hackathon1.data;

import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioRepository {
    public List<Experience> load() {
        return sampleData();
    }

    public static List<Experience> sampleData() {
        return new ArrayList<>(List.of(
                new Experience("robotics", "Robotics Club Programmer", ExperienceType.CLUB,
                        LocalDate.of(2026, 9, 12), 42, "Built autonomous routines and mentored new members."),
                new Experience("food-drive", "Community Food Drive", ExperienceType.VOLUNTEERING,
                        LocalDate.of(2026, 5, 18), 12.5, "Sorted donations and coordinated pickup stations."),
                new Experience("app-project", "Study Planner App", ExperienceType.PROJECT,
                        LocalDate.of(2026, 3, 4), 24, "Designed and tested a Java study planner with classmates."),
                new Experience("award", "Regional Coding Finalist", ExperienceType.AWARD,
                        LocalDate.of(2025, 11, 8), 0, "Placed in the top ten at the regional programming contest."),
                new Experience("soccer", "Junior Varsity Soccer", ExperienceType.SPORT,
                        LocalDate.of(2025, 8, 20), 68, "Practiced four days a week and supported team fundraising.")
        ));
    }
}
