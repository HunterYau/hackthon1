package hackathon1.ui;

import hackathon1.data.PortfolioRepository;
import hackathon1.service.PortfolioService;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ScreenshotExporter {
    private ScreenshotExporter() { }

    public static void export(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            SwingUtilities.invokeAndWait(() -> {
                PortfolioService service = new PortfolioService(new PortfolioRepository());
                PortfolioAppPanel panel = new PortfolioAppPanel(service);
                render(panel, outputDirectory.resolve("student-view.png"));
                panel.showRole("counselor");
                render(panel, outputDirectory.resolve("counselor-view.png"));
            });
        } catch (Exception exception) {
            throw new IllegalStateException("Could not export screenshots", exception);
        }
    }

    private static void render(PortfolioAppPanel panel, Path output) {
        panel.setSize(460, 840);
        layoutTree(panel);
        BufferedImage image = new BufferedImage(460, 840, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        panel.printAll(graphics);
        graphics.dispose();
        try {
            ImageIO.write(image, "png", output.toFile());
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static void layoutTree(java.awt.Container container) {
        container.doLayout();
        for (java.awt.Component child : container.getComponents()) {
            if (child instanceof java.awt.Container nested) layoutTree(nested);
        }
    }
}
