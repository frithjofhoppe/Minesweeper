package application;

import com.sun.istack.internal.Nullable;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

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

    private void openFields(int x, int y) {
        boolean isRunning = true;
        Field start = getFieldByPosition(x, y);
        int xPos = x;
        int yPos = y;
        int range = 1;

        while (isRunning) {
            int count = (range * 4) + 4;
            boolean isAllTimeBomb = true;

            yPos -= range;

            for (int a = 0; a < count; a++) {
                Field actual = getFieldByPosition(xPos, yPos);
                if(actual != null) {
                    if (!actual.isBomb) {
                        isAllTimeBomb = false;
                        actual.setText(Integer.toString(getBombCount(actual.getxPos(), actual.getyPos())));
                        fieldCounter++;
                        checkIfGameHasToFinish();
                    }
                }
            }

            if (isAllTimeBomb) {
                isRunning = false;
            }

            range += 2;
        }
    }

    VBox createPlayField(int expansion, int chance) {
        VBox back = new VBox();

        for (int a = 0; a < expansion; a++) {
            HBox line = new HBox();

            for (int b = 0; b < expansion; b++) {
                Field f;

                if (getRandomNumber(chance) == 1) {
                    f = new Field(true);
                    f.setText("b");
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
                            if (!f.isClicked) {
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
//            f.isClicked = true;
            if (!f.isBomb) {
                turnNormalField(f);
                fieldCounter++;
                checkFieldArround(f);
                System.out.println(f.getxPos() + " "+ f.getyPos());
//                removeAllStyles(f);
//                f.getStyleClass().add("FieldButtonClicked");
//                f.setText(Integer.toString(getBombCount(f.getxPos(), f.getyPos())));
            } else {
                removeAllStyles(f);
                f.getStyleClass().add("FieldBomb");
                launcher.finished(false);
            }
//            fieldCounter++;
//            checkIfGameHasToFinish();
        }
    }

    private void turnNormalField(Field f)
    {
        removeAllStyles(f);
        f.getStyleClass().add("FieldButtonClicked");
        f.setText(Integer.toString(getBombCount(f.getxPos(), f.getyPos())));
    }

    private void checkFieldArround(Field field)
    {
        List<Field> fields = new ArrayList<Field>();
        fields.add(field);
        boolean isFinished = false;
        while(!isFinished) {
            ListIterator fieldIter = fields.listIterator();
            int a = 0;
            int b = 0;
            while (fieldIter.hasNext()) {
                Field actual = (Field) fieldIter.next();
                ArrayList<Field> suroundingFields = getFieldsArround(actual);
                a++;
                fieldIter.remove();
                for (Field f : suroundingFields) {
                    b++;
                    if (f != null) {
                        if (!f.isBomb) {
                            if (!f.isClicked) {
                                f.isClicked = true;
                                turnNormalField(f);
//                                fieldIter.add(f);
                                fieldCounter++;
                                checkIfGameHasToFinish();
                                System.out.println("OUT :" + fieldCounter + " " + generatedBombs);

                                if (getBombCount(f.getxPos(), f.getyPos()) == 0) {
                                    fieldIter.add(f);
                                }
                            }
                        }
                    }
                }
                b = 0;
            }
            if(fields.size() == 0)
            {
                isFinished = true;
            }
            else
            {
                System.out.println("SIZE != 0");
            }
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

    private ArrayList<Field> getFieldsArround(Field field)
    {
        ArrayList<Field> list = new ArrayList<Field>();

        list.add(getFieldByPosition(field.getxPos() + 1, field.getyPos() - 1));
        list.add(getFieldByPosition(field.getxPos() + 1, field.getyPos() + 1));
        list.add(getFieldByPosition(field.getxPos(), field.getyPos() + 1));
        list.add(getFieldByPosition(field.getxPos() - 1, field.getyPos() + 1));
        list.add(getFieldByPosition(field.getxPos() - 1, field.getyPos()));
        list.add(getFieldByPosition(field.getxPos() - 1, field.getyPos() - 1));
        list.add(getFieldByPosition(field.getxPos(), field.getyPos() - 1));
        list.add(getFieldByPosition(field.getxPos() + 1, field.getyPos()));

        return list;
    }

    private void removeAllStyles(Field field) {
        //field.getStyleClass().remove("FieldButtonNotClicked");
        field.getStyleClass().remove("FieldButtonClicked");
        field.getStyleClass().remove("FieldButtonMarked");
        field.getStyleClass().remove("FieldBomb");
    }
}
