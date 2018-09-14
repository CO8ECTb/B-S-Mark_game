package sample;

import javafx.animation.RotateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Element{

    private Button imageButton = new Button();
    private RotateTransition rt = new RotateTransition();
    private ImageView view;
    private boolean activity;
    private boolean rotated;
    private int column;
    private int row;
    private int lvlView;

    public Element( int column, int row, boolean activity, boolean rotated, int lvlView){
        this.activity = activity;
        this.column = column;
        this.rotated = rotated;
        this.row = row;
        this.lvlView = lvlView;
        checkElement();
    }

    public void checkElement(){
        view = new ImageView();
        Image im;
        if(lvlView == 0) {
            if (!activity) {
                im = new Image("resources/rails.png");
            } else {
                im = new Image("resources/activeRails.gif");
            }
        } else{
            if (!activity) {
                im = new Image("resources/red.png");
            } else {
                im = new Image("resources/redA.png");
            }
        }
        view.setImage(im);
        imageButton.setGraphic(view);
    }


    public ImageView getView() {
        return view;
    }

    public Button getImageButton() {
        if(isRotated()) imageButton.setRotate(90);
        return imageButton;
    }

    public boolean isActivity() {
        return activity;
    }

    public boolean isRotated() {
        return rotated;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }



    public void setActivity(boolean activity) {
        this.activity = activity;
        checkElement();
    }

    public void clickAction(){
        rt.setDuration(Duration.millis(200));
        rt.setNode(imageButton);
        if(this.imageButton.getRotate() == 0) {
            this.rotated = true;
            rt.setFromAngle(0);
            rt.setToAngle(90);
        }
        else {
            this.rotated = false;
            rt.setFromAngle(90);
            rt.setToAngle(0);
        }
        rt.play();
    }


    public void setRotated(boolean rotated) {
        this.rotated = rotated;
        clickAction();
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
