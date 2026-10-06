package hackathon1;

import hackathon1.data.PortfolioRepository;
import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;
import hackathon1.service.PortfolioService;

import java.time.LocalDate;

public class PortfolioServiceTest {
    public static void main(String[] args) {
        PortfolioService service = new PortfolioService(new PortfolioRepository(null));

        assert service.find("robotics", null, PortfolioService.SortMode.NEWEST).size() == 1;
        assert service.find("", ExperienceType.AWARD, PortfolioService.SortMode.NEWEST).size() == 1;
        assert service.find("", null, PortfolioService.SortMode.NEWEST).get(0).date()
                .isAfter(service.find("", null, PortfolioService.SortMode.NEWEST).get(1).date());
        assert service.badges().contains("All-Rounder");
        assert service.buildResumeText().contains("Counselor verified");

        int before = service.all().size();
        service.add(Experience.create("School Newspaper", ExperienceType.CLUB,
                LocalDate.of(2026, 10, 1), 6, "Edited two student profiles."));
        assert service.all().size() == before + 1;
        assert service.pending().stream().anyMatch(item -> item.title().equals("School Newspaper"));

        System.out.println("PortfolioServiceTest passed");
    }
}
