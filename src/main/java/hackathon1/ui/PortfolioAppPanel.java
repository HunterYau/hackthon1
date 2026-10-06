package hackathon1.ui;

import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;
import hackathon1.service.PortfolioService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PortfolioAppPanel extends JPanel {
    private static final String STUDENT = "student";
    private static final String COUNSELOR = "counselor";

    private final PortfolioService service;
    private final CardLayout cards = new CardLayout();
    private final JPanel cardPanel = new JPanel(cards);
    private JButton studentTab;
    private JButton counselorTab;

    public PortfolioAppPanel(PortfolioService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(Theme.PAPER);
        add(buildHeader(), BorderLayout.NORTH);
        refreshCards(STUDENT);
    }

    private JPanel buildHeader() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.INK);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 18, 12, 18));

        JPanel title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        JLabel name = Theme.label("PATHFOLIO", 25, Font.BOLD, Color.WHITE);
        JLabel subtitle = Theme.label("Your four-year story", 12, Font.PLAIN, new Color(224, 216, 204));
        title.add(name);
        title.add(subtitle);
        wrapper.add(title, BorderLayout.WEST);

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 3));
        tabs.setOpaque(false);
        studentTab = Theme.button("Student", Theme.ORANGE, Color.WHITE);
        counselorTab = Theme.button("Counselor", new Color(70, 65, 63), Color.WHITE);
        studentTab.addActionListener(event -> showRole(STUDENT));
        counselorTab.addActionListener(event -> showRole(COUNSELOR));
        tabs.add(studentTab);
        tabs.add(counselorTab);
        wrapper.add(tabs, BorderLayout.EAST);
        return wrapper;
    }

    public void showRole(String role) {
        refreshCards(role);
    }

    private void refreshCards(String role) {
        cardPanel.removeAll();
        cardPanel.add(buildStudentView(), STUDENT);
        cardPanel.add(buildCounselorView(), COUNSELOR);
        if (cardPanel.getParent() == null) {
            add(cardPanel, BorderLayout.CENTER);
        }
        cards.show(cardPanel, role);
        studentTab.setBackground(STUDENT.equals(role) ? Theme.ORANGE : new Color(70, 65, 63));
        counselorTab.setBackground(COUNSELOR.equals(role) ? Theme.TEAL : new Color(70, 65, 63));
        revalidate();
        repaint();
    }

    private JPanel buildStudentView() {
        JPanel page = Theme.paperPanel();
        page.setBackground(Theme.PAPER);
        page.setLayout(new BorderLayout());
        page.setBorder(BorderFactory.createEmptyBorder(16, 14, 14, 14));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(sectionHeading("Hi, Maya!", "You are building a story worth remembering."));
        content.add(Box.createVerticalStrut(12));
        content.add(statRow());
        content.add(Box.createVerticalStrut(12));
        content.add(badgeStrip());
        content.add(Box.createVerticalStrut(14));

        JTextField search = new JTextField();
        search.setToolTipText("Search titles and descriptions");
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Search experiences"),
                BorderFactory.createEmptyBorder(1, 5, 2, 5)));
        JComboBox<String> type = new JComboBox<>();
        type.addItem("All categories");
        for (ExperienceType value : ExperienceType.values()) type.addItem(value.label());
        JComboBox<PortfolioService.SortMode> sort = new JComboBox<>(PortfolioService.SortMode.values());

        JPanel controls = new JPanel(new BorderLayout(6, 6));
        controls.setOpaque(false);
        controls.setAlignmentX(LEFT_ALIGNMENT);
        controls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        controls.add(search, BorderLayout.NORTH);
        JPanel selectors = new JPanel(new GridLayout(1, 2, 6, 0));
        selectors.setOpaque(false);
        selectors.add(type);
        selectors.add(sort);
        controls.add(selectors, BorderLayout.CENTER);
        content.add(controls);
        content.add(Box.createVerticalStrut(10));

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setAlignmentX(LEFT_ALIGNMENT);
        list.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        Runnable updateList = () -> {
            ExperienceType selected = type.getSelectedIndex() == 0 ? null
                    : ExperienceType.values()[type.getSelectedIndex() - 1];
            List<Experience> items = service.find(search.getText(), selected,
                    (PortfolioService.SortMode) sort.getSelectedItem());
            list.removeAll();
            if (items.isEmpty()) {
                JPanel empty = card();
                empty.add(Theme.label("No experiences match this search.", 13, Font.PLAIN, Theme.MUTED));
                list.add(empty);
            } else {
                for (Experience item : items) {
                    list.add(experienceCard(item, false));
                    list.add(Box.createVerticalStrut(8));
                }
            }
            list.revalidate();
            list.repaint();
        };
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent event) { updateList.run(); }
            public void removeUpdate(DocumentEvent event) { updateList.run(); }
            public void changedUpdate(DocumentEvent event) { updateList.run(); }
        });
        type.addActionListener(event -> updateList.run());
        sort.addActionListener(event -> updateList.run());
        updateList.run();
        content.add(list);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);

        JButton add = Theme.button("+ Add experience", Theme.ORANGE, Color.WHITE);
        add.addActionListener(event -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            Experience experience = ExperienceDialog.show(frame);
            if (experience != null) {
                service.add(experience);
                refreshCards(STUDENT);
            }
        });
        JButton resume = Theme.button("Export resume", Theme.WHITE, Theme.INK);
        resume.addActionListener(event -> exportResume());
        JPanel actions = new JPanel(new GridLayout(1, 2, 8, 0));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        actions.add(add);
        actions.add(resume);
        page.add(actions, BorderLayout.SOUTH);
        return page;
    }

    private JPanel statRow() {
        JPanel stats = new JPanel(new GridLayout(1, 3, 7, 0));
        stats.setOpaque(false);
        stats.setAlignmentX(LEFT_ALIGNMENT);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        stats.add(statCard(Integer.toString(service.all().size()), "experiences", Theme.ORANGE));
        stats.add(statCard(PortfolioService.formatHours(service.totalHours()), "hours", Theme.TEAL));
        stats.add(statCard(Long.toString(service.verifiedCount()), "verified", Theme.BLUE));
        return stats;
    }

    private JPanel statCard(String value, String label, Color color) {
        JPanel card = card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel number = Theme.label(value, 23, Font.BOLD, color);
        JLabel caption = Theme.label(label, 11, Font.PLAIN, Theme.MUTED);
        number.setAlignmentX(CENTER_ALIGNMENT);
        caption.setAlignmentX(CENTER_ALIGNMENT);
        card.add(number);
        card.add(caption);
        return card;
    }

    private JPanel badgeStrip() {
        JPanel panel = card();
        panel.setLayout(new BorderLayout(8, 5));
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        panel.add(Theme.label("BADGES", 11, Font.BOLD, Theme.MUTED), BorderLayout.NORTH);
        String badges = String.join("   ", service.badges());
        panel.add(Theme.label("<html><body style='width:340px'>" + badges + "</body></html>",
                11, Font.BOLD, Theme.INK), BorderLayout.CENTER);
        JProgressBar progress = new JProgressBar(0, 5);
        progress.setValue(service.all().size());
        progress.setStringPainted(true);
        progress.setString(Math.min(service.all().size(), 5) + " of 5 entries to next badge");
        progress.setForeground(Theme.YELLOW);
        panel.add(progress, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCounselorView() {
        JPanel page = Theme.paperPanel();
        page.setBackground(Theme.PAPER);
        page.setLayout(new BorderLayout());
        page.setBorder(BorderFactory.createEmptyBorder(16, 14, 14, 14));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(sectionHeading("Counselor review", "Maya Chen - Class of 2028"));
        content.add(Box.createVerticalStrut(12));

        JPanel overview = card();
        overview.setLayout(new BorderLayout(8, 8));
        JPanel facts = new JPanel(new GridLayout(1, 3));
        facts.setOpaque(false);
        facts.add(centeredFact(service.all().size() + "", "entries"));
        facts.add(centeredFact(service.verifiedCount() + "", "verified"));
        facts.add(centeredFact(service.pending().size() + "", "pending"));
        overview.add(facts, BorderLayout.NORTH);
        JProgressBar completeness = new JProgressBar(0, Math.max(1, service.all().size()));
        completeness.setValue((int) service.verifiedCount());
        completeness.setForeground(Theme.TEAL);
        completeness.setStringPainted(true);
        completeness.setString("Portfolio verification progress");
        overview.add(completeness, BorderLayout.SOUTH);
        content.add(overview);
        content.add(Box.createVerticalStrut(16));
        content.add(Theme.label("NEEDS REVIEW", 12, Font.BOLD, Theme.MUTED));
        content.add(Box.createVerticalStrut(7));

        if (service.pending().isEmpty()) {
            JPanel done = card();
            done.add(Theme.label("Everything is verified.", 14, Font.BOLD, Theme.TEAL));
            content.add(done);
        } else {
            for (Experience item : service.pending()) {
                content.add(experienceCard(item, true));
                content.add(Box.createVerticalStrut(8));
            }
        }
        content.add(Box.createVerticalStrut(10));
        content.add(Theme.label("FOUR-YEAR OVERVIEW", 12, Font.BOLD, Theme.MUTED));
        content.add(Box.createVerticalStrut(7));
        JPanel timeline = card();
        timeline.setLayout(new GridLayout(4, 2, 6, 5));
        timeline.add(Theme.label("Freshman", 12, Font.BOLD, Theme.INK));
        timeline.add(Theme.label("2 documented", 12, Font.PLAIN, Theme.MUTED));
        timeline.add(Theme.label("Sophomore", 12, Font.BOLD, Theme.INK));
        timeline.add(Theme.label("3 documented", 12, Font.PLAIN, Theme.MUTED));
        timeline.add(Theme.label("Junior", 12, Font.BOLD, Theme.INK));
        timeline.add(Theme.label("Planning goals", 12, Font.PLAIN, Theme.MUTED));
        timeline.add(Theme.label("Senior", 12, Font.BOLD, Theme.INK));
        timeline.add(Theme.label("Resume ready", 12, Font.PLAIN, Theme.MUTED));
        content.add(timeline);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        page.add(scroll, BorderLayout.CENTER);
        return page;
    }

    private JPanel centeredFact(String value, String label) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel number = Theme.label(value, 20, Font.BOLD, Theme.TEAL);
        JLabel caption = Theme.label(label, 11, Font.PLAIN, Theme.MUTED);
        number.setAlignmentX(CENTER_ALIGNMENT);
        caption.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(number);
        panel.add(caption);
        return panel;
    }

    private JPanel experienceCard(Experience item, boolean withVerifyButton) {
        JPanel panel = card();
        panel.setLayout(new BorderLayout(10, 5));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.add(Theme.label(item.title(), 14, Font.BOLD, Theme.INK));
        String meta = item.type().label() + "  -  "
                + item.date().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        if (item.hours() > 0) meta += "  -  " + PortfolioService.formatHours(item.hours()) + " hours";
        if (!withVerifyButton) meta += item.verified() ? "  -  Verified" : "  -  Pending review";
        text.add(Theme.label(meta, 11, Font.PLAIN, Theme.MUTED));
        JLabel description = Theme.label("<html><body style='width:205px'>" + escape(item.description())
                + "</body></html>", 11, Font.PLAIN, Theme.INK);
        text.add(Box.createVerticalStrut(4));
        text.add(description);
        panel.add(text, BorderLayout.CENTER);

        if (withVerifyButton) {
            JButton verify = Theme.button("Verify", Theme.TEAL, Color.WHITE);
            verify.addActionListener(event -> {
                service.verify(item.id());
                refreshCards(COUNSELOR);
            });
            panel.add(verify, BorderLayout.EAST);
        }
        return panel;
    }

    private JPanel sectionHeading(String title, String subtitle) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        panel.add(Theme.label(title, 24, Font.BOLD, Theme.INK));
        panel.add(Theme.label(subtitle, 12, Font.PLAIN, Theme.MUTED));
        return panel;
    }

    private JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(Theme.WHITE);
        panel.setBorder(Theme.cardBorder());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        panel.setAlignmentX(LEFT_ALIGNMENT);
        return panel;
    }

    private void exportResume() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("Maya-Chen-Pathfolio.txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            Files.writeString(chooser.getSelectedFile().toPath(), service.buildResumeText(),
                    StandardCharsets.UTF_8);
            JOptionPane.showMessageDialog(this, "Resume text exported successfully.");
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, "Could not export the resume.",
                    "Export failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
