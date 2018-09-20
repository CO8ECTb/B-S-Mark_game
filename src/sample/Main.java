package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

import static sample.Stats.GetColorsAndScores;
import static sample.Stats.GetScoreForGrade;


public class Main extends Application {
    private Double width;
    private Double height;
    private VBox root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        LvlGenerator.GenAll();

        /*
        SAMPLE for getColors

        List<Pair<Integer, Integer>> colorsAndScores = Stats.GetColorsAndScores(Stats.GetScoreForGrade(1), 1);
        for (int i = 0; i < colorsAndScores.size(); ++i) {
            System.out.println(colorsAndScores.get(i).getKey() + " " + colorsAndScores.get(i).getValue());
        }

        */

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
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.RIGHT){
                if(gui.getLevel() < LvlGenerator.GetLvlCountByGrade(0))
                gui.getNextLvl().fire();
            }
            if(event.getCode() == KeyCode.LEFT){
                if(gui.getLevel() > 1 )
                gui.getPrevLvl().fire();
            }

            if(event.getCode() == KeyCode.F1){
                gui.getHelp();
            }

            System.out.println(event.getCode());

            if(event.getCode() == KeyCode.ESCAPE){
                alertCloseMessage(event);
            }

        });
        primaryStage.setOnCloseRequest(event -> {
            alertCloseMessage(event);
        });
    }


    public void alertCloseMessage(Event event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText("Вы точно хотите выйти?");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK) System.exit(0);
        else event.consume();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
