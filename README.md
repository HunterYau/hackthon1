# Pathfolio

Pathfolio is a Java prototype for the CSNHS high school portfolio hackathon. It gives students one place to record clubs, sports, volunteering, awards, projects, and jobs across all four years of high school.

## What is included

- A phone-sized student dashboard with local persistence
- Add-experience form with date and hours validation
- Search by title or description
- Filter by activity category
- Sort by newest, oldest, or category
- Automatic hour totals and badges
- Counselor review queue with one-click verification
- Resume-ready text export
- A seven-slide presentation with the problem, solution, both user experiences, flowchart, and pseudocode

## Run the app

Requires Java 17 or newer.

```bash
java -jar output/app/Pathfolio.jar
```

Pathfolio stores changes in `~/.pathfolio/experiences.tsv`. If that file does not exist, the app starts with demo data.

## Build from source

With Maven:

```bash
mvn package
java -jar target/hackthon-1.0-SNAPSHOT.jar
```

The project has no third-party runtime dependencies. The packaged submission JAR in `output/app` was compiled directly with Java 17 compatibility.

## Source layout

- `src/main/java/hackathon1/model`: experience data model and categories
- `src/main/java/hackathon1/data`: local TSV persistence and demo data
- `src/main/java/hackathon1/service`: search, filter, sort, badge, verification, and resume logic
- `src/main/java/hackathon1/ui`: student and counselor Swing interfaces
- `src/test/java`: assertion-based service tests
- `output/presentation`: final PowerPoint submission
- `output/screenshots`: student and counselor GUI previews

## Verification

The service test covers searching, category filtering, date sorting, badges, resume generation, and adding a new pending entry. The final presentation was structurally validated and every slide was rendered for visual inspection.
