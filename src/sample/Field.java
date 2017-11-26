package sample;

import javafx.scene.control.Button;

public class Field extends Button {
    boolean isMarked;
    boolean isBomb;
    int xPos;
    int yPos;

    Field(boolean isBomb) {
        this.isBomb = isBomb;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
