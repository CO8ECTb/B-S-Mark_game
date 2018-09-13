package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

 class Gui {
    private Double width;
    private Double height;
    private Double headerHeight,buttonsHeight;
    private VBox root, body;
    private HBox headerBox;
    private GridPane gameGrid, buttonsGrid;
    private HBox exitButtons;
    private ChoiceBox<String> classChoiseBox;
    private List<Element> items = new ArrayList<>();
    private static final Integer firstLvl = 1;
    private static final Integer secondLvl = 2;
    private static final Integer thirdLvl = 3;
    private static final Integer fourthLvl = 4;
    private Integer dimension;
    private Integer counter = 9990;
    private Label counterLabel = new Label(counter.toString());

    private final int lvlView = checkElementStyle();


    public Gui(Double width, Double height){
        this.width = width;
        this.height = height;
        headerHeight = height*0.1;
        buttonsHeight = height * 0.1;
    }

    public void skeleton(){
        root = new VBox();
        headerBox = new HBox();
        body = new VBox();
        gameGrid = new GridPane();
        buttonsGrid = new GridPane();
        //buttonsGrid.setGridLinesVisible(true);
        //buttonsGrid.setPrefWidth(width*0.95);
        buttonsGrid.setMaxWidth(width-10);
        buttonsGrid.setMinWidth(width*0.95);
        buttonsGrid.setAlignment(Pos.CENTER);
        buttonsGrid.setPrefHeight(buttonsHeight);
        body.getChildren().add(buttonsGrid);

        headerBox.setPrefHeight(headerHeight);

        body.getChildren().add(gameGrid);

        gameGrid.setPrefHeight(height-(2*headerHeight+buttonsHeight)+12);
        gameGrid.setPrefWidth(width*0.9);
        gameGrid.setMaxHeight(height-(2*headerHeight+buttonsHeight)+12);
        gameGrid.setMinHeight(height-(2*headerHeight+buttonsHeight)+12);
        exitButtons = new HBox();
        exitButtons.setPrefHeight(headerHeight);
        root.getChildren().add(0,headerBox);
        root.getChildren().add(1,body);
        root.getChildren().add(2,exitButtons);
    }

    public VBox getRoot(){
        return root;
    }

    public void setStyle(){
        headerBox.setId("header");
        if(lvlView == 1)
        body.setId("back1");
        else body.setId("back2");
        gameGrid.setAlignment(Pos.CENTER);
        exitButtons.setId("exit");
        root.getStylesheets().addAll(this.getClass().getResource("/style/style.css").toExternalForm());
        Font font = Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),18);
        try {
            Field f = Font.class.getDeclaredField("DEFAULT");
            f.setAccessible(true);
            f.set(null, font);
        } catch(Exception e){
            System.out.println("Не удалось установить шрифт");
            e.printStackTrace();
        }

    }

    public void setComponentsLook(){
        Label labelComplexity = new Label("Выберите класс:");

        labelComplexity.setTextFill(Color.WHITE);
        labelComplexity.setAlignment(Pos.CENTER);
        classChoiseBox = new ChoiceBox<>(FXCollections.observableArrayList(firstLvl.toString(), secondLvl.toString(),thirdLvl.toString(),fourthLvl.toString()));
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        ColumnConstraints col5 = new ColumnConstraints();
        ColumnConstraints col6 = new ColumnConstraints();
        col1.setPercentWidth(26);
        col2.setPercentWidth(10);
        col3.setPercentWidth(15);
        col4.setPercentWidth(15);
        col5.setPercentWidth(15);
        col6.setPercentWidth(30);
        buttonsGrid.getColumnConstraints().addAll(col1,col2,col3,col4,col5);
        buttonsGrid.add(labelComplexity,0,0);
        buttonsGrid.add(classChoiseBox,1,0);
        buttonsGrid.add(counterLabel,6,0);
        //buttonsGrid.addColumn(1,counterLabel);
        //buttonsGrid.getColumnConstraints().get(1).setPercentWidth(50);

        counterLabel.setAlignment(Pos.CENTER_RIGHT);
        counterLabel.setFont(Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),45));
        counterLabel.setTextFill(Color.WHITE);


        classChoiseBox.setOnAction(event -> {
            gameGrid.getChildren().clear();
            gameGrid.getColumnConstraints().clear();
            gameGrid.getRowConstraints().clear();
            dimension = 4;
            drawTask(dimension);
        });
    }


    private void listen(){
        for(Element el:items){
            el.getImageButton().setOnMouseClicked(event -> {
                el.clickAction();
                if(counter<9999)
                counter++;
                counterLabel.setText(counter.toString());
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

    private boolean checkRow(List<Element> list, int row) {
        for (int i = 0; i < dimension; ++i) {
            if (!list.get(row + dimension * i).isRotated()) {
                return false;
            }
        }
        return true;
    }

//    private boolean checkColumn(List<Element> list, int column) {
//        for (int i = 0; i < dimension; ++i) {
//            if (list.get(i + dimension * column).isRotated()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void addColumn(List<Integer> ids, int column) {
//        for (int i = 0; i < dimension; ++i) {
//            ids.add(i + dimension * column);
//        }
//    }

    private void addRow(List<Integer> ids, int row) {
        for (int i = 0; i < dimension; ++i) {
            ids.add(row + dimension * i);
        }
    }

    private List<Integer> getRedStoneLight(List<Element> list) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < dimension; ++i) {
           // if (checkColumn(list, i))addColumn(ids, i);
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

            for(int j = 0; j < dimension; j++) {
                Element item = new Element(i,j,false,false,lvlView);
                item.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-5);
                item.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-5);
                item.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                item.getImageButton().setBackground(Background.EMPTY);
                gameGrid.add(item.getImageButton(),i,j);
                gameGrid.setMargin(item.getImageButton(), new Insets(5));
                items.add(item);

            }
        }
        listen();
    }


    public void reDrawTask(List<Element> items){
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        for(Element el:items){
            el.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-15);
            el.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-15);
            el.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            el.getImageButton().setBackground(Background.EMPTY);
            gameGrid.add(el.getImageButton(),el.getColumn(),el.getRow());
        }
    }

    public int checkElementStyle(){
        Random ra = new Random();
        if (ra.nextInt()%2 == 0)return 0;
        else return 1;
    }
}
 