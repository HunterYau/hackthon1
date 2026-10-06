package hackathon1.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

final class Theme {
    static final Color INK = new Color(39, 35, 34);
    static final Color MUTED = new Color(103, 96, 89);
    static final Color PAPER = new Color(250, 246, 237);
    static final Color WHITE = new Color(255, 253, 248);
    static final Color ORANGE = new Color(224, 92, 42);
    static final Color TEAL = new Color(42, 132, 123);
    static final Color BLUE = new Color(73, 111, 178);
    static final Color YELLOW = new Color(249, 205, 76);
    static final Color PINK = new Color(233, 142, 145);
    static final Color LINE = new Color(226, 216, 197);

    private Theme() { }

    static Font font(float size, int style) {
        return new Font("SansSerif", style, Math.round(size));
    }

    static JLabel label(String text, float size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font(size, style));
        label.setForeground(color);
        return label;
    }

    static JButton button(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(font(13, Font.BOLD));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker(), 1),
                BorderFactory.createEmptyBorder(9, 13, 9, 13)));
        return button;
    }

    static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    static JPanel paperPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(236, 227, 208));
                for (int y = 42; y < getHeight(); y += 30) {
                    g2.drawLine(0, y, getWidth(), y);
                }
                g2.dispose();
            }
        };
    }

    static void opaque(JComponent component, Color background) {
        component.setOpaque(true);
        component.setBackground(background);
    }
}
