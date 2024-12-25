package logic;

import java.awt.*;
import javax.swing.*;

public class TopScoresPanel extends JPanel {

    private final JLabel scoreLabel;

    public TopScoresPanel() {
        setPreferredSize(new Dimension(200, 500));  // Set the size for the leaderboard panel
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout());

        // Create a JLabel to display the top scores
        scoreLabel = new JLabel("<html>Loading...</html>");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setVerticalAlignment(SwingConstants.TOP);
        JScrollPane scrollPane = new JScrollPane(scoreLabel);
        add(scrollPane, BorderLayout.CENTER);

        // Add a title label
        JLabel label = new JLabel("Top Scores");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        // Set up the timer to update every 2 seconds
        Timer timer = new Timer(2000, _ -> fetchTopScores());
        timer.start();
    }

    // This method fetches the top scores from the PHP server
    public void fetchTopScores() {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String scoresJson = ScoreService.getScore();  // Assuming this method returns scores as a string
                scoresJson = scoresJson.replace("\"", "");
                scoresJson = scoresJson.replace("-", "<br>");

                StringBuilder sb = new StringBuilder("<html>");

                String[] scores = scoresJson.split("\n");
                for (String score : scores) {
                    if (!score.trim().isEmpty()) {  // Avoid empty lines
                        sb.append(score.replace(":", " - ")).append("<br>");  // Replace ':' with ' - ' and add line break
                    }
                }

                sb.append("</html>");
                publish(sb.toString());  // This sends data to the process() method

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Update the label text
                scoreLabel.setText(chunks.getFirst());
            }
        };

        // Start the background task
        worker.execute();
    }
}
