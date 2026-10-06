package hackathon1.ui;

import hackathon1.model.Experience;
import hackathon1.model.ExperienceType;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

final class ExperienceDialog extends JDialog {
    private Experience result;

    private ExperienceDialog(JFrame owner) {
        super(owner, "Add experience", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.PAPER);

        JTextField title = new JTextField();
        JComboBox<ExperienceType> type = new JComboBox<>(ExperienceType.values());
        JTextField date = new JTextField(LocalDate.now().toString());
        JSpinner hours = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1000.0, 0.5));
        JTextArea description = new JTextArea(4, 24);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(14, 14, 8, 14));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 4, 6, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        addRow(form, c, 0, "Title", title);
        addRow(form, c, 1, "Category", type);
        addRow(form, c, 2, "Date (YYYY-MM-DD)", date);
        addRow(form, c, 3, "Hours", hours);
        addRow(form, c, 4, "What did you do?", new JScrollPane(description));
        add(form, BorderLayout.CENTER);

        javax.swing.JButton save = Theme.button("Save experience", Theme.ORANGE, java.awt.Color.WHITE);
        save.addActionListener(event -> {
            try {
                result = Experience.create(title.getText(), (ExperienceType) type.getSelectedItem(),
                        LocalDate.parse(date.getText().trim()), ((Number) hours.getValue()).doubleValue(),
                        description.getText());
                dispose();
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(this, "Check the title, date, and hours.",
                        "Could not save", JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 10, 14, 10));
        actions.add(save);
        add(actions, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(380, 480));
        pack();
        setLocationRelativeTo(owner);
    }

    private static void addRow(JPanel panel, GridBagConstraints c, int row, String label,
                               java.awt.Component component) {
        c.gridy = row * 2;
        c.gridx = 0;
        c.weighty = 0;
        panel.add(Theme.label(label, 12, java.awt.Font.BOLD, Theme.MUTED), c);
        c.gridy++;
        c.weighty = component instanceof JScrollPane ? 1 : 0;
        c.fill = component instanceof JScrollPane ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
        panel.add(component, c);
        c.fill = GridBagConstraints.HORIZONTAL;
    }

    static Experience show(JFrame owner) {
        ExperienceDialog dialog = new ExperienceDialog(owner);
        dialog.setVisible(true);
        return dialog.result;
    }
}
