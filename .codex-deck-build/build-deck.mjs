import fs from "node:fs/promises";
import path from "node:path";
import { pathToFileURL } from "node:url";
import { Presentation, PresentationFile } from "@oai/artifact-tool";

const workspaceDir = "/Users/hunteryau/VSC/Java/hackthon1";
const SKILL_DIR = "/Users/hunteryau/.codex/plugins/cache/openai-primary-runtime/presentations/26.909.11814/skills/presentations";
const RUNTIME_PYTHON = "/Users/hunteryau/.cache/codex-runtimes/codex-primary-runtime/dependencies/python/bin/python3.12";
const TMP_DIR = path.join(workspaceDir, ".codex-deck-build");
const FINAL_PPTX = path.join(workspaceDir, "output/presentation/Pathfolio-Hackathon-Presentation-Submission.pptx");
const { resolvePresentationFont, finalizePresentation } = await import(
  pathToFileURL(path.join(SKILL_DIR, "container_tools/artifact_tool_utils.mjs")).href,
);

const font = resolvePresentationFont();
const colors = {
  ink: "#272322",
  muted: "#665F59",
  paper: "#FAF6ED",
  white: "#FFFDF8",
  orange: "#E05C2A",
  teal: "#2A847B",
  blue: "#496FB2",
  yellow: "#F9CD4C",
  pink: "#E98E91",
  line: "#DED2BD",
};

await fs.mkdir(TMP_DIR, { recursive: true });
await fs.mkdir(path.dirname(FINAL_PPTX), { recursive: true });
const studentBytes = new Uint8Array(await fs.readFile(path.join(workspaceDir, "output/screenshots/student-view.png")));
const counselorBytes = new Uint8Array(await fs.readFile(path.join(workspaceDir, "output/screenshots/counselor-view.png")));

const presentation = Presentation.create({ slideSize: { width: 1280, height: 720 } });

function addBackground(slide) {
  slide.background.fill = colors.paper;
}

function addText(slide, text, position, size = 24, options = {}) {
  const shape = slide.shapes.add({
    geometry: "textbox",
    position,
    fill: options.fill ?? "none",
    line: options.line ?? { fill: "none", width: 0 },
    borderRadius: options.borderRadius,
    shadow: options.shadow,
  });
  shape.text = text;
  shape.text.style = {
    typeface: font,
    fontSize: size,
    bold: options.bold ?? false,
    color: options.color ?? colors.ink,
    autoFit: options.autoFit ?? "shrinkText",
    alignment: options.alignment ?? "left",
  };
  return shape;
}

function addTitle(slide, title, subtitle) {
  addText(slide, title, { left: 76, top: 54, width: 1120, height: 62 }, 38,
    { bold: true, color: colors.ink, autoFit: "none" });
  if (subtitle) {
    addText(slide, subtitle, { left: 78, top: 118, width: 1080, height: 36 }, 17,
      { color: colors.muted, autoFit: "none" });
  }
}

function addFooter(slide, number) {
  addText(slide, String(number).padStart(2, "0"), { left: 1160, top: 666, width: 54, height: 24 }, 12,
    { bold: true, color: colors.orange, alignment: "right", autoFit: "none" });
}

function addPanel(slide, position, fill = colors.white, lineColor = colors.line) {
  return slide.shapes.add({
    geometry: "rect",
    position,
    fill,
    line: { style: "solid", fill: lineColor, width: 1 },
    shadow: "shadow-sm",
  });
}

function addLabel(slide, text, position, color) {
  const box = slide.shapes.add({
    geometry: "roundRect",
    position,
    fill: color,
    line: { fill: color, width: 0 },
    borderRadius: "rounded-full",
  });
  box.text = text;
  box.text.style = { typeface: font, fontSize: 12, bold: true, color: colors.white,
    alignment: "center", autoFit: "shrinkText" };
  return box;
}

// 1. Cover
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addText(slide, "CSNHS 26-27 HACKATHON", { left: 78, top: 112, width: 560, height: 34 }, 18,
    { bold: true, color: colors.orange, autoFit: "none" });
  addText(slide, "Pathfolio", { left: 72, top: 176, width: 730, height: 112 }, 72,
    { bold: true, color: colors.ink, autoFit: "none" });
  addText(slide, "A high school experience tracker", { left: 78, top: 298, width: 700, height: 50 }, 28,
    { color: colors.ink, autoFit: "none" });
  addText(slide, "Capture the details now. Use them when applications begin.",
    { left: 78, top: 382, width: 650, height: 84 }, 22,
    { color: colors.muted, autoFit: "none" });
  addLabel(slide, "WORKING JAVA DEMO", { left: 78, top: 514, width: 220, height: 38 }, colors.teal);
    { left: 78, top: 568, width: 650, height: 42 }, 17,
    { color: colors.muted, autoFit: "none" });
  slide.speakerNotes.textFrame.setText("Challenge source: Hackathon -3 Building Your High School Portfolio (1).pdf, supplied by the user.");
}

// 2. Problem statement
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addTitle(slide, "The memory gap", "Students complete meaningful work, but the evidence gets scattered across four years.");
  addPanel(slide, { left: 76, top: 196, width: 670, height: 386 });
  addText(slide, "By senior year, names, dates, hours, and impact are easy to forget.",
    { left: 112, top: 236, width: 590, height: 96 }, 30,
    { bold: true, color: colors.ink, autoFit: "none" });
  addText(slide,
    "College applications and scholarship forms ask for specific details. Students often rebuild those details from old messages, calendars, and memory.",
    { left: 112, top: 366, width: 570, height: 120 }, 21,
    { color: colors.muted, autoFit: "none" });
  addText(slide, "The result", { left: 825, top: 214, width: 330, height: 42 }, 17,
    { bold: true, color: colors.orange, autoFit: "none" });
  addText(slide, "Incomplete applications\nMissing service hours\nGeneric resume descriptions",
    { left: 824, top: 270, width: 340, height: 190 }, 25,
    { bold: true, color: colors.ink, autoFit: "none" });
  addText(slide, "Pathfolio fixes the problem while the experience is still fresh.",
    { left: 824, top: 492, width: 340, height: 86 }, 20,
    { color: colors.teal, bold: true, autoFit: "none" });
  addFooter(slide, 2);
  slide.speakerNotes.textFrame.setText("Problem statement is based on page 2 of the supplied hackathon brief.");
}

// 3. Student experience
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addTitle(slide, "Student experience", "A phone-sized workspace turns activities into a sortable four-year record.");
  slide.images.add({
    blob: studentBytes,
    contentType: "image/png",
    alt: "Pathfolio student dashboard showing experiences, automatic hours, sorting, and resume export",
    fit: "contain",
    geometry: "roundRect",
    borderRadius: "rounded-xl",
    position: { left: 96, top: 170, width: 365, height: 500 },
  });
  addText(slide, "Record an experience", { left: 530, top: 198, width: 590, height: 40 }, 24,
    { bold: true, color: colors.orange, autoFit: "none" });
  addText(slide, "Add the title, category, date, hours, and a short description.",
    { left: 530, top: 242, width: 590, height: 62 }, 19, { color: colors.muted, autoFit: "none" });
  addText(slide, "Choose the order", { left: 530, top: 334, width: 590, height: 40 }, 24,
    { bold: true, color: colors.blue, autoFit: "none" });
  addText(slide, "Sort the full portfolio by newest, oldest, or activity category.",
    { left: 530, top: 378, width: 590, height: 64 }, 19, { color: colors.muted, autoFit: "none" });
  addText(slide, "Reuse the work", { left: 530, top: 472, width: 590, height: 40 }, 24,
    { bold: true, color: colors.teal, autoFit: "none" });
  addText(slide, "Automatic hour totals stay visible, and the app exports organized resume text.",
    { left: 530, top: 516, width: 590, height: 68 }, 19, { color: colors.muted, autoFit: "none" });
  addFooter(slide, 3);
  slide.speakerNotes.textFrame.setText("Screenshot generated from the included Pathfolio Java application. Requirements source: supplied brief, pages 3 and 4.");
}

// 4. Counselor experience
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addTitle(slide, "Counselor experience", "Counselors can read the portfolio and see how it develops across high school.");
  addText(slide, "Four-year overview", { left: 90, top: 206, width: 460, height: 42 }, 26,
    { bold: true, color: colors.teal, autoFit: "none" });
  addText(slide, "The year-by-year summary makes missing periods easy to notice.",
    { left: 90, top: 256, width: 470, height: 76 }, 20, { color: colors.muted, autoFit: "none" });
  addText(slide, "Automatic hour total", { left: 90, top: 370, width: 460, height: 42 }, 26,
    { bold: true, color: colors.orange, autoFit: "none" });
  addText(slide, "The dashboard adds activity hours and shows the total at a glance.",
    { left: 90, top: 420, width: 470, height: 78 }, 20, { color: colors.muted, autoFit: "none" });
  addText(slide, "Recent experiences", { left: 90, top: 534, width: 460, height: 42 }, 26,
    { bold: true, color: colors.blue, autoFit: "none" });
  addText(slide, "Counselors can read the latest titles, dates, hours, and descriptions.",
    { left: 90, top: 584, width: 470, height: 62 }, 20, { color: colors.muted, autoFit: "none" });
  slide.images.add({
    blob: counselorBytes,
    contentType: "image/png",
    alt: "Pathfolio counselor dashboard showing a four-year overview, total hours, and recent experiences",
    fit: "contain",
    geometry: "roundRect",
    borderRadius: "rounded-xl",
    position: { left: 700, top: 170, width: 365, height: 500 },
  });
  addFooter(slide, 4);
  slide.speakerNotes.textFrame.setText("Screenshot generated from the included Pathfolio Java application. Counselor interaction is required by page 3 of the supplied brief.");
}

// 5. Flowchart
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addTitle(slide, "Core application flow", "Each new experience updates the hour total and joins the sortable portfolio.");
  const positions = [
    { left: 70, top: 282, width: 180, height: 108 },
    { left: 300, top: 282, width: 180, height: 108 },
    { left: 530, top: 282, width: 180, height: 108 },
    { left: 760, top: 282, width: 180, height: 108 },
    { left: 990, top: 282, width: 180, height: 108 },
  ];
  const labels = ["Student adds\nexperience", "App validates\nrequired fields", "App adds entry\nand totals hours", "Counselor views\nfour-year overview", "Student sorts\nor exports"];
  const fills = [colors.orange, colors.yellow, colors.blue, colors.teal, colors.pink];
  const textColors = [colors.white, colors.ink, colors.white, colors.white, colors.ink];
  const boxes = positions.map((position, index) => {
    const box = slide.shapes.add({ geometry: "roundRect", position, fill: fills[index],
      line: { fill: fills[index], width: 0 }, borderRadius: "rounded-xl", shadow: "shadow-sm" });
    box.text = labels[index];
    box.text.style = { typeface: font, fontSize: 19, bold: true, color: textColors[index],
      alignment: "center", autoFit: "shrinkText" };
    return box;
  });
  for (let i = 0; i < boxes.length - 1; i++) {
    slide.shapes.connect(boxes[i], boxes[i + 1], {
      kind: "straight", fromSide: "right", toSide: "left",
      line: { style: "solid", fill: colors.ink, width: 2 },
      tail: { type: "triangle", width: "sm", length: "sm" },
    });
  }
  addText(slide, "If validation fails, the form stays open and highlights the missing or invalid field.",
    { left: 290, top: 474, width: 700, height: 56 }, 20,
    { color: colors.muted, alignment: "center", autoFit: "none" });
  addText(slide, "New entries remain available for the current app session.",
    { left: 320, top: 548, width: 640, height: 46 }, 18,
    { bold: true, color: colors.teal, alignment: "center", autoFit: "none" });
  addFooter(slide, 5);
  slide.speakerNotes.textFrame.setText("The flowchart documents the implemented Java application logic. Flowchart and core logic are required by pages 3 and 4 of the supplied brief.");
}

// 6. Pseudocode and CS concept
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addTitle(slide, "Sorting and hour-total logic", "Comparators change the record order, while aggregation calculates the hours.");
  addPanel(slide, { left: 72, top: 182, width: 730, height: 432 }, "#272322", "#272322");
  addText(slide,
    "INPUT sortMode\n\nresults = all experiences\n\nIF sortMode is NEWEST\n  SORT results by date descending\nELSE IF sortMode is OLDEST\n  SORT results by date ascending\nELSE\n  SORT results by category\n\ntotalHours = SUM each experience's hours\nDISPLAY results and totalHours",
    { left: 112, top: 218, width: 650, height: 360 }, 22,
    { color: "#FFFDF8", autoFit: "none" });
  addText(slide, "Computer science concepts", { left: 860, top: 198, width: 340, height: 42 }, 24,
    { bold: true, color: colors.orange, autoFit: "none" });
  addText(slide, "Data model", { left: 860, top: 270, width: 300, height: 36 }, 19,
    { bold: true, color: colors.ink, autoFit: "none" });
  addText(slide, "Each experience is one structured record with a category, date, hours, and description.",
    { left: 860, top: 310, width: 330, height: 102 }, 17, { color: colors.muted, autoFit: "none" });
  addText(slide, "Algorithms", { left: 860, top: 444, width: 300, height: 36 }, 19,
    { bold: true, color: colors.ink, autoFit: "none" });
  addText(slide, "Comparators order the records. Aggregation adds the hours from every experience.",
    { left: 860, top: 484, width: 330, height: 116 }, 17, { color: colors.muted, autoFit: "none" });
  addFooter(slide, 6);
  slide.speakerNotes.textFrame.setText("Pseudocode reflects PortfolioService.sorted and PortfolioService.totalHours in the included Java source. The CS concept requirement appears on page 3 of the supplied brief.");
}

// 7. Feasibility
{
  const slide = presentation.slides.add();
  addBackground(slide);
  addTitle(slide, "A focused prototype", "The simplified app keeps only the requested enhancements and the required user views.");
  addText(slide, "Working prototype", { left: 94, top: 202, width: 460, height: 46 }, 28,
    { bold: true, color: colors.teal, autoFit: "none" });
  addText(slide,
    "Add experiences during the current session\n\nSort by newest, oldest, or category\n\nCalculate total hours automatically\n\nExport resume-ready text\n\nShow a four-year counselor overview",
    { left: 94, top: 278, width: 500, height: 280 }, 21,
    { color: colors.ink, autoFit: "none" });
  addText(slide, "Implementation", { left: 704, top: 202, width: 460, height: 46 }, 28,
    { bold: true, color: colors.orange, autoFit: "none" });
  addText(slide,
    "Standard Java Swing interface\n\nIn-memory demo records\n\nNo external libraries\n\nRuns from a single JAR\n\nStudent and counselor views",
    { left: 704, top: 278, width: 500, height: 280 }, 21,
    { color: colors.ink, autoFit: "none" });
  addText(slide, "Pathfolio gives every experience a place before the details disappear.",
    { left: 172, top: 612, width: 940, height: 54 }, 25,
    { bold: true, color: colors.blue, alignment: "center", autoFit: "none" });
  addFooter(slide, 7);
  slide.speakerNotes.textFrame.setText("Implementation details are drawn from the included Java source. The solution stays within the brief's high-school-level feasibility requirement.");
}

const stagingDir = path.join(workspaceDir, ".codex-finalizer");
await fs.mkdir(stagingDir, { recursive: true });
const candidatePath = path.join(stagingDir, "pathfolio-candidate.pptx");
await (await PresentationFile.exportPptx(presentation)).save(candidatePath);

const result = await finalizePresentation({
  explicitTotalSlideCount: 7,
  requiredNativeTableOwnerSlides: [],
  requiredNativeChartOwnerSlides: [],
  workspaceDir,
  candidatePath,
  finalPath: FINAL_PPTX,
  pythonExecutable: RUNTIME_PYTHON,
  integrityValidatorPath: path.join(SKILL_DIR, "container_tools/inspect_presentation_package_integrity.py"),
  layoutValidatorPath: path.join(SKILL_DIR, "container_tools/inspect_presentation_layout_geometry.py"),
  layoutArgs: [
    "--expected-slide-size-emu", "12192000,6858000",
    "--validate-bullet-geometry",
    "--validate-heading-fit",
  ],
  fontPolicy: { basis: "design", families: [font] },
  verifyArtifactToolImport: true,
  receiptPath: path.join(stagingDir, "Pathfolio-Hackathon-Presentation-Submission.validation.json"),
});

console.log(JSON.stringify({ font, finalPath: FINAL_PPTX, result }, null, 2));
