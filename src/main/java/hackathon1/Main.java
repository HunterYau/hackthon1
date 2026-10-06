package hackathon1;

import hackathon1.data.PortfolioRepository;
import hackathon1.service.PortfolioService;
import hackathon1.ui.PortfolioAppPanel;
import hackathon1.ui.ScreenshotExporter;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2 && "--screenshots".equals(args[0])) {
            System.setProperty("java.awt.headless", "true");
            ScreenshotExporter.export(Path.of(args[1]));
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // The cross-platform look and feel is bundled with Java; default is still usable.
            }

            Path dataFile = Path.of(System.getProperty("user.home"), ".pathfolio", "experiences.tsv");
            PortfolioService service = new PortfolioService(new PortfolioRepository(dataFile));
            JFrame frame = new JFrame("Pathfolio - High School Portfolio");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new PortfolioAppPanel(service));
            frame.setSize(460, 840);
            frame.setMinimumSize(new java.awt.Dimension(420, 720));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
