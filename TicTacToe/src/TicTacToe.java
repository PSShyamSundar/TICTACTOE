import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 700; // 50px for the text panel on top and 50px for the restart button

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JButton[][] board = new JButton[3][3];
    JButton restartButton = new JButton("Restart");

    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;

    TicTacToe() {
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Update colors and fonts
        textLabel.setBackground(new Color(70, 130, 180)); // Steel Blue
        textLabel.setForeground(new Color(255, 235, 205)); // Blanched Almond
        textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 45)); // Unique font style and size
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(new Color(245, 245, 245)); // White Smoke
        frame.add(boardPanel);

        buttonPanel.setLayout(new FlowLayout());
        restartButton.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        restartButton.setBackground(new Color(255, 182, 193)); // Light Pink
        restartButton.setForeground(Color.DARK_GRAY);
        restartButton.setFocusable(false);
        buttonPanel.add(restartButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(new Color(255-51-51, 250, 205)); // Lemon Chiffon
                tile.setForeground(new Color(0, 100, 0)); // Dark Green
                tile.setFont(new Font("Comic Sans MS", Font.BOLD, 100)); // Unique font style and size
                tile.setFocusable(false);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        JButton tile = (JButton) e.getSource();
                        if (tile.getText().equals("")) {
                            tile.setText(playerX);
                            turns++;
                            checkWinner();
                            if (!gameOver) {
                                currentPlayer = playerO;
                                textLabel.setText(currentPlayer + "'s turn.");
                                makeAIMove(); // AI makes a move after the player
                            }
                        }
                    }
                });
            }
        }

        // Restart Button Action Listener
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
    }

    void checkWinner() {
        // Check for winner (same logic as before)
        // Horizontal, Vertical, and Diagonal checks
        // If there's a winner or a tie, set gameOver to true and update the textLabel
        // (No changes needed in this function for now)
        
        // Horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().equals("")) continue;

            if (board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[r][i]);
                }
                gameOver = true;
                return;
            }
        }

        // Vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().equals("")) continue;
            
            if (board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]);
                }
                gameOver = true;
                return;
            }
        }

        // Diagonal
        if (board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText()) &&
            !board[0][0].getText().equals("")) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]);
            }
            gameOver = true;
            return;
        }

        // Anti-diagonal
        if (board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText()) &&
            !board[0][2].getText().equals("")) {
            setWinner(board[0][2]);
            setWinner(board[1][1]);
            setWinner(board[2][0]);
            gameOver = true;
            return;
        }

        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    setTie(board[r][c]);
                }
            }
            gameOver = true;
        }
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.white);
        tile.setBackground(new Color(50, 205, 50)); // Lime Green
        textLabel.setText(currentPlayer + " is the winner!");
    }

    void setTie(JButton tile) {
        tile.setForeground(new Color(255-0-0, 99, 255-204-0)); // Tomato
        tile.setBackground(new Color(70, 130, 180)); // Steel Blue
        textLabel.setText("Tie!");
    }

    void resetGame() {
        currentPlayer = playerX;
        gameOver = false;
        turns = 0;
        textLabel.setText("Tic-Tac-Toe");
        
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setForeground(new Color(0, 100, 0)); // Dark Green
                board[r][c].setBackground(new Color(255, 250, 205)); // Lemon Chiffon
            }
        }
    }

    void makeAIMove() {
        if (gameOver) return;

        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        // Evaluate all possible moves
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getText().equals("")) {
                    board[r][c].setText(playerO); // Make move
                    int score = minimax(false); // Evaluate move
                    board[r][c].setText(""); // Undo move
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = r;
                        bestCol = c;
                    }
                }
            }
        }

        // Make the best move
        board[bestRow][bestCol].setText(playerO);
        turns++;
        checkWinner();

        if (!gameOver) {
            currentPlayer = playerX;
            textLabel.setText(currentPlayer + "'s turn.");
        }
    }

    int minimax(boolean isMaximizing) {
        if (checkWin(playerO)) return 10;
        if (checkWin(playerX)) return -10;
        if (isTie()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().equals("")) {
                        board[r][c].setText(playerO);
                        int score = minimax(false);
                        board[r][c].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c].getText().equals("")) {
                        board[r][c].setText(playerX);
                        int score = minimax(true);
                        board[r][c].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    boolean checkWin(String player) {
        // Logic to check if the specified player has won
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().equals(player) &&
                board[r][1].getText().equals(player) &&
                board[r][2].getText().equals(player)) {
                return true;
            }
        }

        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().equals(player) &&
                board[1][c].getText().equals(player) &&
                board[2][c].getText().equals(player)) {
                return true;
            }
        }

        if (board[0][0].getText().equals(player) &&
            board[1][1].getText().equals(player) &&
            board[2][2].getText().equals(player)) {
            return true;
        }

        if (board[0][2].getText().equals(player) &&
            board[1][1].getText().equals(player) &&
            board[2][0].getText().equals(player)) {
            return true;
        }

        return false;
    }

    boolean isTie() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
