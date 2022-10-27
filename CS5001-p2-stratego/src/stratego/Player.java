package stratego;

/**
 * Class representing a Player in a Stratego Game.
 */
public class Player {
    private String name;
    private int playerNumber;
    private boolean lossFlag;

    /**
     * Player constructor.
     * @param name Player name
     * @param playerNumber Player number
     */
    public Player(String name, int playerNumber) {
        this.name = name;
        this.playerNumber = playerNumber;
        lossFlag = false;

    }


    /** 
     * Method setting the loss flag.
     */
    public void loseGame() {
        lossFlag = true;
    }


    /** 
     * Method checking if a Player has lost the game.
     * @return boolean
     */
    public boolean hasLost() {
        return lossFlag;
    }


    /** 
     * Getter method for Player name.
     * @return String
     */
    public String getName() {
        return name;
    }


    /** 
     * Getter method for Player number.
     * @return int
     */
    public int getPlayerNumber() {
        return playerNumber;
    }
}
