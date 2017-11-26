package sample;

import com.sun.istack.internal.Nullable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Random;

public class PlayField {

    VBox playingField;
    int probability = 5;
    int dimensionOfField = 7;

    public PlayField() {
        playingField = createPlayField(dimensionOfField, probability);
    }

    VBox createPlayField(int expansion, int chance) {
        VBox back = new VBox();

        for (int a = 0; a < expansion; a++) {
            HBox line = new HBox();

            for (int b = 0; b < expansion; b++) {
                Field f;

                if (getRandomNumber(chance) == 1) {
                    f = new Field(true);
                } else {
                    f = new Field(false);
                }
                f.getStyleClass().add("FieldButtonNotClicked");
                f.setyPos(a);
                f.setxPos(b);

                f.setOnAction(e -> {
                    fieldListener(f);
                });

                line.getChildren().add(f);
            }

            back.getChildren().add(line);
        }

        return back;
    }

    public int getRandomNumber(int max) {
        Random rnd = new Random();
        return rnd.nextInt(max) + 1;
    }

    private void fieldListener(Field f) {
        if (f.isBomb == false) {
            f.getStyleClass().add("FieldButtonClicked");
            f.setText(Integer.toString(getBombCount(f.getxPos(), f.getyPos())));
        }
        else
        {
            f.getStyleClass().add("FieldBomb");
        }
    }

    @Nullable
    private Field getFieldByPosition(int x, int y) {
        Field f;

        try {
            HBox box = (HBox) playingField.getChildren().get(y);
            if (box != null) {
                Field fe = (Field) box.getChildren().get(x);
                if (fe != null) {
                    return fe;
                }
            }
        } catch (Exception e) {
            System.out.println("WARNING Field not found");
        }

        return null;
    }

    public VBox getPlayingField() {
        return playingField;
    }

    private HBox findRowByIndex(int index) {
        try {
            HBox test = (HBox) playingField.getChildren().get(index);
            return test;
        } catch (Exception error) {
            //System.out.println("ERROR There is no row " + Integer.toString(index));
        }
        return null;
    }

    private int isBomb(int x, int y) {
        Field f = getFieldByPosition(x, y);
        if (f != null) {
            if (f.isBomb) {
                return 1;
            }
        }

        return 0;
    }

    private int getBombCount(int x, int y) {
        int back = 0;

        back += isBomb(x + 1, y - 1);
        back += isBomb(x + 1, y + 1);
        back += isBomb(x, y + 1);
        back += isBomb(x - 1, y + 1);
        back += isBomb(x - 1, y);
        back += isBomb(x - 1, y - 1);
        back += isBomb(x, y - 1);
        back += isBomb(x + 1, y);

        return back;
    }
}
