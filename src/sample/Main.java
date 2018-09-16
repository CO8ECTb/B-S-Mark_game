package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private Double width;
    private Double height;
    private VBox root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        if (SaveMaker.TestSaveMaker()) {
            System.out.println("Success!");
        } else {
            System.out.println("Fail!");
        }

        //LvlGenerator.GenAll();
        Helper.TestGetTip();
        primaryStage.getIcons().add(new Image("resources/icon.png"));
        width = Screen.getPrimary().getBounds().getWidth()*0.4;
        height = Screen.getPrimary().getBounds().getHeight()*0.8;
        Gui gui = new Gui(width,height);
        gui.skeleton();
        gui.setStyle();
        this.root = gui.getRoot();
        Scene scene = new Scene(root,width,height);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        gui.setComponentsLook();
        primaryStage.setTitle("BS-Mark Game");
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.RIGHT){
                gui.getNextLvl().fire();
            }
            if(event.getCode() == KeyCode.LEFT){
                gui.getPrevLvl().fire();
            }

            if(event.getCode() == KeyCode.F1){
                gui.getHelp();
            }

        });
    }




    public static void main(String[] args) {
        launch(args);
    }
}
