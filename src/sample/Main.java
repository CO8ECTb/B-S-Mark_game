package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class Main extends Application {
    Double width;
    Double height;
    VBox root;
    @Override
    public void start(Stage primaryStage) throws Exception{
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
        gui.setComplexityButtons();
        primaryStage.setTitle("B-S-Mark Game");
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
