package sample;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.*;
import java.lang.reflect.Field;

class Gui {
    private Double width;
    private Double height;
    private Integer level = 1;
    private Double headerHeight,buttonsHeight;
    private VBox root, body;
    private HBox headerBox;
    private GridPane gameGrid, buttonsGrid;
    private HBox footerButtons;
    private ChoiceBox<String> classChoiseBox;
    private Button prevLvl, nextLvl, reset,quit, cheatButton;
    private List<Element> items = new ArrayList<>();
    private static final Integer firstLvl = 1;
    private static final Integer secondLvl = 2;
    private static final Integer thirdLvl = 3;
    private static final Integer fourthLvl = 4;
    private final Integer dimension = 4;
    private Integer grade;
    private Integer counter = 0;
    private Label counterLabel = new Label(counter.toString());
    private Double fontSize;
    private Double counterSize;

    private int lvlView;


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
        footerButtons = new HBox();
        footerButtons.setPrefHeight(headerHeight);
        root.getChildren().add(0,headerBox);
        root.getChildren().add(1,body);
        root.getChildren().add(2,footerButtons);
    }

    public VBox getRoot(){
        return root;
    }

    public void setStyle(){
        headerBox.setId("header");
        body.setId("startBack");
        gameGrid.setAlignment(Pos.CENTER);
        footerButtons.setId("footer");
        root.getStylesheets().addAll(this.getClass().getResource("/style/style.css").toExternalForm());
        fontSize = (this.width/42);
        counterSize = this.width/17;

        Font font = Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),fontSize);
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

        Label labelComplexity = new Label("Класс: ");
        labelComplexity.setTextFill(Color.WHITE);

        //Выбор класса
        classChoiseBox = new ChoiceBox<>(FXCollections.observableArrayList(firstLvl.toString(), secondLvl.toString(),thirdLvl.toString(),fourthLvl.toString()));

        //Кнопка пред лвл
        prevLvl = new Button("<-");
        prevLvl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(prevLvl, Priority.ALWAYS);
        GridPane.setVgrow(prevLvl, Priority.ALWAYS);
        GridPane.setMargin(prevLvl, new Insets(8));

        //Кнопка след лвл
        nextLvl = new Button("->");
        nextLvl.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(nextLvl, Priority.ALWAYS);
        GridPane.setVgrow(nextLvl, Priority.ALWAYS);
        GridPane.setMargin(nextLvl, new Insets(8));


        //Кнопка заново
        reset = new Button("Заново");
        reset.setMaxWidth(Double.MAX_VALUE);
        reset.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(reset, Priority.ALWAYS);
        GridPane.setVgrow(reset, Priority.ALWAYS);
        GridPane.setMargin(reset, new Insets(8));

        //Счетчик нажатий
        counterLabel.setAlignment(Pos.CENTER_RIGHT);
        counterLabel.setFont(Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),counterSize));
        counterLabel.setTextFill(Color.WHITE);
        buttonsGrid.setHalignment(counterLabel, HPos.RIGHT);

        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        ColumnConstraints col5 = new ColumnConstraints();

        col0.setPercentWidth(11);
        col1.setPercentWidth(12);
        col2.setPercentWidth(18);
        col3.setPercentWidth(18);
        col4.setPercentWidth(20);
        col5.setPercentWidth(20);

        buttonsGrid.getColumnConstraints().addAll(col0,col1,col2,col3,col4,col5);
        buttonsGrid.add(labelComplexity,0,0);
        buttonsGrid.add(classChoiseBox,1,0);
        buttonsGrid.add(prevLvl,2,0);
        buttonsGrid.add(nextLvl,3,0);
        buttonsGrid.add(reset,4,0);
        buttonsGrid.add(counterLabel,5,0);

        reset.setDisable(true);
        prevLvl.setDisable(true);
        nextLvl.setDisable(true);

        classChoiseBox.setOnAction(event -> {
            if(reset.isDisable() || prevLvl.isDisable() || nextLvl.isDisable()){
                if(level != 1) prevLvl.setDisable(false);
                else prevLvl.setDisable(true);

                if(level < LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setDisable(false);
                else nextLvl.setDisable(true);

                nextLvl.setDisable(false);
                reset.setDisable(false);
            }
            classChoiseBox.setDisable(true);
            grade = Integer.parseInt(classChoiseBox.getValue());
            gameGrid.getChildren().clear();
            gameGrid.getColumnConstraints().clear();
            gameGrid.getRowConstraints().clear();

            //drawTask(dimension,grade,"1");
            loadLevel(level.toString(),grade);
        });


        prevLvl.setOnAction(event -> {
            level--;
            if(level <= 1){
                prevLvl.setDisable(true);
            }
            else  prevLvl.setDisable(false);
            if(level >= LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setDisable(true);
            else nextLvl.setDisable(false);
            loadLevel(level.toString(),grade);
        });

        nextLvl.setOnAction(event -> {
            level++;
            if(level > 1) prevLvl.setDisable(false);
            loadLevel(level.toString(),grade);
            if(level >= LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setDisable(true);
        });


    }


    private void listen(){
        for(Element el:items){
            el.getImageButton().setOnMouseClicked(event -> {
                el.clickAction();
                if(counter<9999) counter++;

                if(level != 1) prevLvl.setDisable(false);
                else prevLvl.setDisable(true);

                if(level < LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setDisable(false);
                else nextLvl.setDisable(true);

                counterLabel.setText(counter.toString());
                for(Element el2:items){
                    if ((el.getColumn() == el2.getColumn() && el.getRow() != el2.getRow()) || (el.getColumn() != el2.getColumn() && el.getRow() ==el2.getRow()))
                        el2.clickAction();
                }
                List<Integer> list = getLight(items);
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

    private List<Integer> getLight(List<Element> list) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < dimension; ++i) {
           // if (checkColumn(list, i))addColumn(ids, i);
            if(checkRow(list,i)) addRow(ids,i);
        }
        return ids;
    }

    public void loadLevel(String lvl, Integer grade){
        this.lvlView = SaveMaker.getLevelStyle(SaveMaker.ReadDataFromFile(lvl,grade));
        if(lvlView == 1)
        body.setId("back1");
        else body.setId("back2");
        drawTask(SaveMaker.parseCollection(SaveMaker.ReadDataFromFile(lvl,grade),dimension),dimension);
    }


    public void drawTask(List<Element> list, Integer dimension){
        items.clear();
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        gameGrid.setGridLinesVisible(true);
        for(int i = 0; i< dimension; i++){
            double rowHeight = gameGrid.getPrefHeight() / dimension;
            double columnWidth = gameGrid.getPrefWidth() / dimension;
            gameGrid.getRowConstraints().add(new RowConstraints(rowHeight));
            gameGrid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }
        for(int i = 0; i < dimension*dimension; i++) {
            int col = i/dimension;
            int row = i-col*dimension;
            Element item = list.get(i);
            item.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-5);
            item.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-5);
            item.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            item.getImageButton().setBackground(Background.EMPTY);
            gameGrid.add(item.getImageButton(),col,row);
            gameGrid.setMargin(item.getImageButton(), new Insets(5));
            items.add(item);
        }

        listen();
    }


    public void reDrawTask(List<Element> items){
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        gameGrid.setGridLinesVisible(true);
        for(Element el:items){
            el.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-15);
            el.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-15);
            el.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            el.getImageButton().setBackground(Background.EMPTY);
            gameGrid.add(el.getImageButton(),el.getColumn(),el.getRow());
        }
    }

//    public int checkElementStyle(){
//        Random ra = new Random();
//        if (ra.nextInt()%2 == 0)return 0;
//        else return 1;
//    }
}
 