package hackathon1.model;

public enum ExperienceType {
    CLUB("Club"),
    SPORT("Sport"),
    VOLUNTEERING("Volunteering"),
    AWARD("Award"),
    PROJECT("Project"),
    JOB("Job");

    private final String label;

    ExperienceType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
