package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.text.html.StyleSheet;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Gui {
    private Double width;
    private Double height;
    private Double headerHeight,buttonsHeight;
    private VBox root, head;
    private HBox complexityButtons, headerBox;
    private GridPane gameGrid;
    private HBox exitButtons;
    private ChoiceBox<String> choiseComplexity;
    private List<Element> items = new ArrayList<>();
    private static final Integer firstLvl = 1;
    private static final Integer secondLvl = 2;
    private static final Integer thirdLvl = 3;
    private static final Integer fourthLvl = 4;
    private Integer dimension;


    public Gui(Double width, Double height){
        this.width = width;
        this.height = height;
        headerHeight = height*0.1;
        buttonsHeight = height * 0.1;
    }

    public void skeleton(){
        root = new VBox();
        headerBox = new HBox();
        head = new VBox();

        headerBox.setPrefHeight(headerHeight);
        complexityButtons = new HBox();
        complexityButtons.setPrefHeight(buttonsHeight);
        head.getChildren().add(complexityButtons);
        gameGrid = new GridPane();
        head.getChildren().add(gameGrid);
        gameGrid.setPrefHeight(height-(2*headerHeight+buttonsHeight)+12);
        gameGrid.setPrefWidth(gameGrid.getPrefHeight());
        gameGrid.setMaxHeight(height-(2*headerHeight+buttonsHeight)+12);
        gameGrid.setMinHeight(height-(2*headerHeight+buttonsHeight)+12);
        exitButtons = new HBox();
        exitButtons.setPrefHeight(headerHeight);
        root.getChildren().add(0,headerBox);
//        root.getChildren().add(1,complexityButtons);
//        root.getChildren().add(2,gameGrid);
        root.getChildren().add(1,head);
        root.getChildren().add(2,exitButtons);
    }

    public VBox getRoot(){
        return root;
    }

    public void setStyle(){
        headerBox.setId("header");
        head.setId("back");
        gameGrid.setAlignment(Pos.CENTER);
        exitButtons.setId("exit");
        root.getStylesheets().addAll(this.getClass().getResource("/style/style.css").toExternalForm());
        Font font = Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),18);
        //complexityButtons.setId("exit");
        try {
            Field f = Font.class.getDeclaredField("DEFAULT");
            f.setAccessible(true);
            f.set(null, font);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void setComplexityButtons(){
        Label labelComplexity = new Label("Выберите класс:");
        labelComplexity.setTextFill(Color.WHITE);


        System.out.println(labelComplexity.getFont());
        choiseComplexity = new ChoiceBox<>(FXCollections.observableArrayList(firstLvl.toString(), secondLvl.toString(),thirdLvl.toString(),fourthLvl.toString()));
        //choiseComplexity
        complexityButtons.setSpacing(15);
        complexityButtons.getChildren().add(labelComplexity);
        complexityButtons.getChildren().add(choiseComplexity);
        choiseComplexity.setOnAction(event -> {
            gameGrid.getChildren().clear();
            gameGrid.getColumnConstraints().clear();
            gameGrid.getRowConstraints().clear();
            Integer complexity = Integer.parseInt(choiseComplexity.getValue());
            if(complexity == firstLvl || complexity == secondLvl) dimension = 4;
            else if(complexity == thirdLvl || complexity == fourthLvl) dimension = 10;
            drawTask(dimension);
        });
    }


    private void listen(){
        for(Element el:items){
            el.getImageButton().setOnMouseClicked(event -> {
                el.clickAction();
                for(Element el2:items){
                    if ((el.getColumn() == el2.getColumn() && el.getRow() != el2.getRow()) || (el.getColumn() != el2.getColumn() && el.getRow() ==el2.getRow()))
                        el2.clickAction();
                }
                List<Integer> list = getRedStoneLight(items);
                for(int i = 0; i < dimension*dimension; ++i){
                    items.get(i).setActivity(false);
                }

                for(int i=0; i<list.size();++i){
                   items.get(list.get(i)).setActivity(true);
                }

                if(checkWin(items)) System.out.println("You win!");
                reDrawTask(items);
            });
        }




    }

    private boolean checkWin(List<Element> list){
        //Проверяет на активность все элементы
        //и возвращает true, если пользователь победил
        for(int i=0; i<dimension*dimension;++i){
            if(!list.get(i).isActivity()) return false;
        }
        return true;
    }

    private boolean checkColumn(List<Element> list, int column) {
        for (int i = 0; i < dimension; ++i) {
            if (list.get(i + dimension * column).isRotated()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRow(List<Element> list, int row) {
        for (int i = 0; i < dimension; ++i) {
            if (!list.get(row + dimension * i).isRotated()) {
                return false;
            }
        }
        return true;
    }

    private void addColumn(List<Integer> ids, int column) {
        for (int i = 0; i < dimension; ++i) {
            ids.add(i + dimension * column);
        }
    }

    private void addRow(List<Integer> ids, int row) {
        for (int i = 0; i < dimension; ++i) {
            ids.add(row + dimension * i);
        }
    }

    private List<Integer> getRedStoneLight(List<Element> list) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < dimension; ++i) {
            if (checkColumn(list, i))addColumn(ids, i);
            if(checkRow(list,i)) addRow(ids,i);
        }
        return ids;
    }


    public void drawTask(Integer dimension){
        items.clear();
        for(int i=0; i < dimension; i++){
            double rowHeight = gameGrid.getPrefHeight() / dimension;
            double columnWidth = gameGrid.getPrefWidth() / dimension;
            gameGrid.getRowConstraints().add(new RowConstraints(rowHeight));
            gameGrid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
            for(int j=0; j < dimension; j++) {
                Element item = new Element(i,j,false,false);
                item.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-5);
                item.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-5);
                item.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                item.getImageButton().setBackground(Background.EMPTY);
                gameGrid.add(item.getImageButton(),i,j);
                gameGrid.setMargin(item.getImageButton(), new Insets(0));
                items.add(item);

            }
        }
        listen();
    }


    public void reDrawTask(List<Element> items){
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        // gameGrid.setGridLinesVisible(true);
        for(Element el:items){
            el.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-5);
            el.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-5);
            el.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            el.getImageButton().setBackground(Background.EMPTY);
            gameGrid.add(el.getImageButton(),el.getColumn(),el.getRow());
        }
    }




}
 