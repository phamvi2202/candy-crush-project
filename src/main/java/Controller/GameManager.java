package Controller;
import candycrush.model.*;
import View.GameFrame;
import java.awt.Point;
import java.util.*;
import javax.swing.JOptionPane;

public class GameManager {
    private Game gameModel;
    private GameFrame viewFrame;
    private boolean isInputLocked = false;

    public GameManager(GameFrame viewFrame) {
        this.gameModel = new Game();
        this.viewFrame = viewFrame;
        
        // Sự kiện chuyển màn chính
        this.viewFrame.getStartPanel().getBtnStart().addActionListener(e -> { viewFrame.showGameScreen(); startGame(); });
        this.viewFrame.getEndPanel().getBtnHome().addActionListener(e -> viewFrame.showStartScreen());
        
        // Sự kiện cho thanh điều khiển dưới bàn cờ
        this.viewFrame.getControlPanel().getBtnEnd().addActionListener(e -> endGameEarly());
        this.viewFrame.getControlPanel().getBtnReset().addActionListener(e -> startGame());
    }

    public void startGame() {
        isInputLocked = false;
        gameModel.newGame();
        updateUI();
        viewFrame.getBoardPanel().updateBoard(getBoardData());
    }

    // Xử lý khi bấm nút "Kết thúc sớm"
    private void endGameEarly() {
        isInputLocked = true; // Khóa bàn cờ
        viewFrame.showEndScreen(gameModel.getLevel(), gameModel.getScore()); // Về thẳng màn hình thua
    }

    private void updateUI() {
        viewFrame.getTopPanel().updateInfo(gameModel.getLevel(), gameModel.getScore(), gameModel.getMovesLeft(), gameModel.getTarget()); 
    }

    private int[][] getBoardData() {
        int[][] data = new int[Game.ROWS][Game.COLS];
        for (int r = 0; r < Game.ROWS; r++) {
            for (int c = 0; c < Game.COLS; c++) {
                CandyType candy = gameModel.getCandy(r, c);
                data[r][c] = (candy != null) ? candy.ordinal() : -1;
            }
        }
        return data;
    }

    public void processPlayerMove(Position pos1, Position pos2) {
        // Bỏ biến isPaused khỏi điều kiện
        if (isInputLocked || gameModel.getStatus() != GameStatus.PLAYING) return;
        
        isInputLocked = true;
        boolean success = gameModel.trySwap(pos1, pos2);
        int r1 = pos1.getRow(), c1 = pos1.getCol();
        int r2 = pos2.getRow(), c2 = pos2.getCol();

        if (success) {
            updateUI();
            viewFrame.getBoardPanel().startSwapAnimation(r1, c1, r2, c2, () -> processMatches(1));
        } else {
            viewFrame.getBoardPanel().startSwapAnimation(r1, c1, r2, c2, () -> {
                viewFrame.getBoardPanel().startSwapAnimation(r2, c2, r1, c1, () -> isInputLocked = false);
            });
        }
    }

    private void processMatches(int combo) {
        Set<Position> matches = gameModel.findMatches();
        
        if (!matches.isEmpty()) {
            gameModel.clearMatches(matches, combo);
            updateUI();
            
            List<Point> viewMatches = new ArrayList<>();
            for (Position p : matches) viewMatches.add(new Point(p.getRow(), p.getCol()));

            viewFrame.getBoardPanel().startExplosionAnimation(viewMatches, () -> {
                gameModel.dropAndRefill();
                viewFrame.getBoardPanel().startDropAnimation(getBoardData(), () -> processMatches(combo + 1));
            });
        } else {
            checkGameState();
        }
    }

    private void checkGameState() {
        if (gameModel.ensurePlayable()) viewFrame.getBoardPanel().updateBoard(getBoardData());

        GameStatus status = gameModel.getStatus();
        if (status == GameStatus.WON) {
            JOptionPane.showMessageDialog(viewFrame, "Tuyệt vời! Chuẩn bị sang Level " + (gameModel.getLevel() + 1), "Qua màn!", JOptionPane.INFORMATION_MESSAGE);
            gameModel.nextLevel();
            updateUI();
            viewFrame.getBoardPanel().updateBoard(getBoardData());
            isInputLocked = false;
        } else if (status == GameStatus.LOST) {
            viewFrame.showEndScreen(gameModel.getLevel(), gameModel.getScore());
            isInputLocked = false; 
        } else { 
            isInputLocked = false; 
        }
    }
}