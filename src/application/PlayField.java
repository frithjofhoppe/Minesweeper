package application;

import com.sun.istack.internal.Nullable;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Random;

public class PlayField {

    MineSweeper launcher;
    VBox playingField;
    int probability = 5;
    int dimensionOfField = 7;
    int generatedBombs = 0;
    int fieldCounter = 0;

    public PlayField(MineSweeper launcher, int probability) {
        this.probability = probability;
        playingField = createPlayField(dimensionOfField, probability);
        this.launcher = launcher;
    }

    VBox createPlayField(int expansion, int chance) {
        VBox back = new VBox();

        for (int a = 0; a < expansion; a++) {
            HBox line = new HBox();

            for (int b = 0; b < expansion; b++) {
                Field f;

                if (getRandomNumber(chance) == 1) {
                    f = new Field(true);
                    generatedBombs++;
                } else {
                    f = new Field(false);
                }
                f.getStyleClass().add("FieldButtonNotClicked");
                f.setyPos(a);
                f.setxPos(b);
                f.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            defaultFieldListener(f);
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            if(!f.isClicked) {
                                markFieldListener(f);
                                System.out.println("marked");
                            }
                        }
                    }
                });

                line.getChildren().add(f);
            }

            back.getChildren().add(line);
        }

        System.out.println(Integer.toString(generatedBombs));
        return back;
    }

    public int getRandomNumber(int max) {
        Random rnd = new Random();
        return rnd.nextInt(max) + 1;
    }

    private void markFieldListener(Field f) {
        if (!f.isMarked) {
            removeAllStyles(f);
            f.getStyleClass().add("FieldButtonMarked");
            f.isMarked = true;
        } else {
            removeAllStyles(f);
            f.getStyleClass().add("FieldButtonNotClicked");
            System.out.println(f.getStyle().toString());
            f.isMarked = false;
        }
    }

    private void defaultFieldListener(Field f) {
        if (!f.isClicked) {
            f.isClicked = true;
            if (f.isBomb == false) {
                removeAllStyles(f);
                f.getStyleClass().add("FieldButtonClicked");
                f.setText(Integer.toString(getBombCount(f.getxPos(), f.getyPos())));
            } else {
                removeAllStyles(f);
                f.getStyleClass().add("FieldBomb");
                launcher.finished(false);
            }
            fieldCounter++;
            checkIfGameHasToFinish();
        }
    }

    private void checkIfGameHasToFinish() {
        if (isPlayFinished()) {
            launcher.finished(true);
        }
    }

    private boolean isPlayFinished() {
        if (fieldCounter >= (dimensionOfField * dimensionOfField - generatedBombs)) {
            return true;
        }
        return false;
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

    private void markField(Field field) {

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

    private void removeAllStyles(Field field)
    {
        //field.getStyleClass().remove("FieldButtonNotClicked");
        field.getStyleClass().remove("FieldButtonClicked");
        field.getStyleClass().remove("FieldButtonMarked");
        field.getStyleClass().remove("FieldBomb");
    }
}
