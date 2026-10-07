package hackathon1;

import hackathon1.data.PortfolioRepository;
import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;
import hackathon1.service.PortfolioService;

import java.time.LocalDate;

public class PortfolioServiceTest {
    public static void main(String[] args) {
        PortfolioService service = new PortfolioService(new PortfolioRepository());

        assert service.sorted(PortfolioService.SortMode.NEWEST).get(0).date()
                .isAfter(service.sorted(PortfolioService.SortMode.NEWEST).get(1).date());
        assert service.sorted(PortfolioService.SortMode.OLDEST).get(0).date()
                .isBefore(service.sorted(PortfolioService.SortMode.OLDEST).get(1).date());
        assert service.sorted(PortfolioService.SortMode.CATEGORY).get(0).type() == ExperienceType.AWARD;
        assert service.buildResumeText().contains("Robotics Club Programmer");
        assert service.totalHours() == 146.5;

        int before = service.all().size();
        service.add(Experience.create("School Newspaper", ExperienceType.CLUB,
                LocalDate.of(2026, 10, 1), 6, "Edited two student profiles."));
        assert service.all().size() == before + 1;
        assert service.sorted(PortfolioService.SortMode.NEWEST).stream()
                .anyMatch(item -> item.title().equals("School Newspaper"));

        System.out.println("PortfolioServiceTest passed");
    }
}
