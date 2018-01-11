package application;

import application.interfaces.MineSweeper;
import com.sun.istack.internal.Nullable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

/**
 * The Playfield represents an graphical construct which build off
 * Fields capsuled in HBoxes.
 *
 * @author Frithjof Hoppe
 * @version 1.0
 */
public class PlayField {

    MineSweeper launcher;
    VBox playingField;
    int probability = 5;
    int dimensionOfField = 7;
    int generatedBombs = 0;
    int fieldCounter = 0;

    /**
     * Constructor sets the practibility and assign playing field a new playfield
     * @param launcher: VBox including HBoxes which represents the graphical playfield
     * @param probability: Propaility how much bombs are set by the creation
     *                   e.g 1:probaility. Each second element is a bomb
     */
    public PlayField(MineSweeper launcher, int probability) {
        this.probability = probability;
        playingField = createPlayField(dimensionOfField, probability);
        this.launcher = launcher;
    }

    /**
     * Change the layout mode of a Field in the Playfield
     * @param x: The x position in the playfield
     * @param y The y position in the playfield
     */
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
                if (actual != null) {
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

    /**
     * Creates a new graphical playfield build of Hboxes and Fields
     * @param expansion: Count of the field expansion in the x and y-axis
     * @param chance: As input-parameter is usally taken the probaility
     * @return: VBox contais the playfield
     */
    VBox createPlayField(int expansion, int chance) {
        VBox back = new VBox();

        for (int a = 0; a < expansion; a++) {
            HBox line = new HBox();

            for (int b = 0; b < expansion; b++) {
                Field f;

                if (getRandomNumber(chance) == 1) {
                    f = new Field(true);
//                    f.setText("b");
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

    /**
     * Generat a random number
     * @param max: The maximal possible size of the generated
     *           random number
     * @return
     */
    public int getRandomNumber(int max) {
        Random rnd = new Random();
        return rnd.nextInt(max) + 1;
    }

    /**
     * The listener for a Field which is alredy marked.
     * Means that the layout has changed
     * @param f: The field which is marked
     */
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


    /**
     * The listener for a Field which is at the moment not marked
     * @param f:
     */
    private void defaultFieldListener(Field f) {

        if (!f.isClicked) {
            if (!f.isBomb) {
                turnNormalField(f);
                f.isClicked = true;
                if (getBombCount(f.getxPos(), f.getyPos()) == 0) {
                    checkFieldArround(f);
                    System.out.println(f.getxPos() + " " + f.getyPos());
                }
                fieldCounter++;
            } else {
                removeAllStyles(f);
                f.getStyleClass().add("FieldBomb");
                launcher.finished(false);
            }
//            fieldCounter++;
            checkIfGameHasToFinish();
        }
    }

    /**
     * Change the layout parameters of a field, so that the fields
     * like turned
     * @param f
     */
    private void turnNormalField(Field f) {
        removeAllStyles(f);
        f.getStyleClass().add("FieldButtonClicked");
        f.setText(Integer.toString(getBombCount(f.getxPos(), f.getyPos())));
    }

    /**
     * Algorithm to turn the fiels which are located around the
     * selectioned field, if some conditions are true
     * @param field
     */
    private void checkFieldArround(Field field) {
        List<Field> fields = new ArrayList<Field>();
        fields.add(field);
        boolean isFinished = false;
        while (!isFinished) {
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
            if (fields.size() == 0) {
                isFinished = true;
            } else {
                System.out.println("SIZE != 0");
            }
        }
    }

    /**
     * If the game has finish the parent is called to make some specific action
     */
    private void checkIfGameHasToFinish() {
        if (isPlayFinished()) {
            launcher.finished(true);
        }
    }

    /**
     * Indicated if the enery possible Field which is not a bomb is turned
     * @return
     */
    private boolean isPlayFinished() {
        if (fieldCounter >= (dimensionOfField * dimensionOfField - generatedBombs)) {
            return true;
        }
        return false;
    }

    /**
     * Find a Field by giving the position in the x- and y-axis as parameter
     * @param x:
     * @param y
     * @return: If a Field at the specific position exists the object will be returned
     */
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

    /**
     * Gets the HBox at the specfic index position
     * @param index: position int the VBox where the HBox is located
     * @return
     */
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

    /**
     * Determine if the Field at the specific position is a bomb
     * @param x
     * @param y
     * @return: Boolish value if the Field is a bomb
     */
    private int isBomb(int x, int y) {
        Field f = getFieldByPosition(x, y);
        if (f != null) {
            if (f.isBomb) {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Get the numbers of bombs which are located one Field arround the
     * denoted Field
     * @param x
     * @param y
     * @return: Numbers of fields
     */
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

    /**
     * Get the fields arround the denoted field
     * @param field
     * @return: List of field which surround the input field
     */
    private ArrayList<Field> getFieldsArround(Field field) {
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

    /**
     * Remove every graphical style from a field
     * @param field
     */
    private void removeAllStyles(Field field) {
        //field.getStyleClass().remove("FieldButtonNotClicked");
        field.getStyleClass().remove("FieldButtonClicked");
        field.getStyleClass().remove("FieldButtonMarked");
        field.getStyleClass().remove("FieldBomb");
    }
}
