package Controller;

import candycrush.model.Game;
import candycrush.model.GameStatus;
import candycrush.model.Position;
import candycrush.model.CandyType;
import View.GameFrame;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameManager {
    private Game gameModel;
    private GameFrame viewFrame;
    private boolean isInputLocked = false;

    public GameManager(GameFrame viewFrame) {
        this.gameModel = new Game();
        this.viewFrame = viewFrame;
    }

    public void startGame() {
        gameModel.newGame();
        viewFrame.getTopPanel().updateInfo(gameModel.getScore(), gameModel.getMovesLeft(), gameModel.getTarget());
        viewFrame.getBoardPanel().updateBoard(getBoardData());
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
        if (isInputLocked || gameModel.getStatus() != GameStatus.PLAYING) return;
        
        isInputLocked = true;
        boolean success = gameModel.trySwap(pos1, pos2);
        int r1 = pos1.getRow(), c1 = pos1.getCol();
        int r2 = pos2.getRow(), c2 = pos2.getCol();

        if (success) {
            viewFrame.getTopPanel().updateInfo(gameModel.getScore(), gameModel.getMovesLeft(), gameModel.getTarget());
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
            viewFrame.getTopPanel().updateInfo(gameModel.getScore(), gameModel.getMovesLeft(), gameModel.getTarget());
            
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
        if (status == GameStatus.WON) viewFrame.showMessage("Chiến Thắng!", "Chúc mừng! Bạn đã vượt qua màn chơi.");
        else if (status == GameStatus.LOST) viewFrame.showMessage("Thất Bại", "Bạn đã hết lượt. Chúc may mắn lần sau!");
        else isInputLocked = false; 
    }
}