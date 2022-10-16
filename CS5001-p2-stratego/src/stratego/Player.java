package stratego;

public class Player {
    private String name;
    private int playerNumber;
    private boolean lossFlag;

    public Player(String name, int playerNumber) {
        this.name = name;
        this.playerNumber = playerNumber;
        lossFlag = false;

    }

    public void loseGame() {
        lossFlag = true;
    }

    public boolean hasLost() {
        return lossFlag;
    }

    public String getName() {
        return name;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
