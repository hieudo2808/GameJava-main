import java.awt.*;
import javax.swing.*;
import logic.TopScoresPanel;
import mapboard.Board;

class App {

    private static void initWindow() {
        // Create the main window frame
        JFrame window = new JFrame("Run away from F");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String playerName = JOptionPane.showInputDialog(null, "Enter your name:");

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "character.Player";  
        }
        System.out.println(playerName);
        Board board = new Board(playerName);

        // Create the leaderboard panel (logic.TopScoresPanel class)
        TopScoresPanel topScoresPanel = new TopScoresPanel();
        
        // Create a layout to position both panels side by side
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Add the board and the leaderboard to the main panel
        mainPanel.add(board, BorderLayout.CENTER);
        mainPanel.add(topScoresPanel, BorderLayout.EAST);  // Place leaderboard on the right

        // Set the window's layout and size
        window.setLayout(new BorderLayout());
        window.add(mainPanel, BorderLayout.CENTER);

        // Pass keyboard inputs to the game board
        window.addKeyListener(board);

        // Don't allow the user to resize the window
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Fetch top scores data from server
        topScoresPanel.fetchTopScores();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::initWindow);
    }
}