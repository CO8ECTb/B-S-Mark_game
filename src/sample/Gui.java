package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.*;
import java.lang.reflect.Field;

class Gui {
    private Double width;
    private Double height;
    private Integer level = 1;
    private Double headerHeight,buttonsHeight;
    private VBox root, body;
    private HBox headerBox;
    private GridPane gameGrid, buttonsGrid, footerGrid;
    private HBox footerButtons;
    private ChoiceBox<String> classChoiseBox;
    private Button prevLvl, nextLvl, reset,quit, cheatButton;
    private List<Element> items = new ArrayList<>();
    private List<Button> levelButton = new ArrayList<>();
    private static final Integer firstLvl = 1;
    private static final Integer secondLvl = 2;
    private static final Integer thirdLvl = 3;
    private static final Integer fourthLvl = 4;
    private final Integer dimension = 4;
    private Integer score = 0;
    private Integer grade;
    private Integer counter = 0;
    private Label counterLabel = new Label(counter.toString());
    private Label lvlLabel = new Label();
    private Double fontSize;
    private Double counterSize, lvlLabelSize;
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
        footerGrid = new GridPane();
        buttonsGrid = new GridPane();

        buttonsGrid.setMaxWidth(width-10);
        buttonsGrid.setMinWidth(width*0.95);
        buttonsGrid.setAlignment(Pos.CENTER);
        buttonsGrid.setPrefHeight(buttonsHeight);
        body.getChildren().add(buttonsGrid);

        headerBox.setPrefHeight(headerHeight);
        lvlLabel.setVisible(false);
        headerBox.getChildren().add(lvlLabel);
        gameGrid.setPrefHeight(height-(2*headerHeight+buttonsHeight)+12);
        gameGrid.setPrefWidth(width*0.9);
        gameGrid.setMaxHeight(height-(2*headerHeight+buttonsHeight)+12);
        gameGrid.setMinHeight(height-(2*headerHeight+buttonsHeight)+12);

        body.setPrefHeight(height-(2*headerHeight)+12);
        body.setPrefWidth(width*0.9);
        body.setMaxHeight(height-(2*headerHeight)+12);
        body.setMinHeight(height-(2*headerHeight)+12);

        footerButtons = new HBox();
        footerButtons.setPrefHeight(headerHeight);



        footerGrid.setPrefHeight(headerHeight-2);
        footerGrid.setMaxHeight(headerHeight-2);
        footerGrid.setMinHeight(headerHeight-2);
        footerGrid.setPrefWidth(width);
        footerGrid.setMaxWidth(width);
        footerGrid.setMinWidth(width);
        footerButtons.getChildren().add(footerGrid);

        root.getChildren().add(0,headerBox);
        root.getChildren().add(1,body);
        root.getChildren().add(2,footerButtons);
    }

    public VBox getRoot(){
        return root;
    }

    public Button getPrevLvl(){
        return prevLvl;
    }

    public Button getNextLvl(){
        return nextLvl;
    }

    public Integer getLevel() {
        return level;
    }

    public List<Button> getLevelButton(){
        return levelButton;
    }

    public void getHelp(){
        Timer timer = new Timer();
        int nxtIdx = Helper.GetTip(items, 0);
        if (nxtIdx < 0) return;
        items.get(nxtIdx).getImageButton().setBackground(new Background(
                new BackgroundFill(Color.YELLOW,new CornerRadii(25),Insets.EMPTY)));
        timer.schedule(new TimerTask() {
           @Override
           public void run() {
               items.get(Helper.GetTip(items,0)).getImageButton().setBackground(Background.EMPTY);
           }
        },500);
    }

    public void setStyle(){
        headerBox.setId("header");
        body.setId("startBack");
        gameGrid.setAlignment(Pos.CENTER);
        footerGrid.setAlignment(Pos.CENTER);
        headerBox.setAlignment(Pos.CENTER);
        footerButtons.setId("footer");
        root.getStylesheets().addAll(this.getClass().getResource("/style/style.css").toExternalForm());
        fontSize = (this.width/42);
        counterSize = this.width/17;
        lvlLabelSize = this.width/20;

        Font font = Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),fontSize);
        try {
            Field f = Font.class.getDeclaredField("DEFAULT");
            f.setAccessible(true);
            f.set(null, font);
        } catch(Exception e){
            System.out.println("Не удалось установить шрифт");
            e.printStackTrace();
        }

        lvlLabel.setTextFill(Color.WHITE);
        lvlLabel.setFont(Font.loadFont(Main.class.getResourceAsStream("/resources/minecraft-font.ttf"),lvlLabelSize));

    }

    public void setComponentsLook(){

        Label labelComplexity = new Label("Класс: ");
        TextArea rools = new TextArea();
        rools.setEditable(false);
        rools.setCursor(null);
        rools.setPrefWidth(width);
        rools.setPrefHeight(height-(2*headerHeight+buttonsHeight)+12);
        rools.setBackground(Background.EMPTY);

        rools.skinProperty().addListener(new ChangeListener<Skin<?>>() {

            @Override
            public void changed(
                    ObservableValue<? extends Skin<?>> ov, Skin<?> t, Skin<?> t1) {
                if (t1 != null && t1.getNode() instanceof Region) {
                    Region r = (Region) t1.getNode();
                    r.setBackground(Background.EMPTY);

                    r.getChildrenUnmodifiable().stream().
                            filter(n -> n instanceof Region).
                            map(n -> (Region) n).
                            forEach(n -> n.setBackground(Background.EMPTY));

                    r.getChildrenUnmodifiable().stream().
                            filter(n -> n instanceof Control).
                            map(n -> (Control) n).
                            forEach(c -> c.skinProperty().addListener(this)); // *
                }
            }
        });

        rools.setText("Тут должны быть правила, но, раз уж их нет,\n" +
                " то тут будет стихотворение!\n \n " +
                "   Духовной жаждою томим,\n" +
                "   В пустыне мрачной я влачился, —\n" +
                "   И шестикрылый серафим\n" +
                "   На перепутье мне явился.\n" +
                "   Перстами легкими как сон\n" +
                "   Моих зениц коснулся он.\n" +
                "   Отверзлись вещие зеницы,\n" +
                "   Как у испуганной орлицы.\n" +
                "   Моих ушей коснулся он, —\n" +
                "   И их наполнил шум и звон:\n" +
                "   И внял я неба содроганье,\n" +
                "   И горний ангелов полет,\n" +
                "   И гад морских подводный ход,\n" +
                "   И дольней лозы прозябанье.\n" +
                "   И он к устам моим приник,\n" +
                "   И вырвал грешный мой язык,\n" +
                "   И празднословный и лукавый,\n" +
                "   И жало мудрыя змеи\n" +
                "   В уста замершие мои\n" +
                "   Вложил десницею кровавой.\n" +
                "   И он мне грудь рассек мечом,\n" +
                "   И сердце трепетное вынул,\n" +
                "   И угль, пылающий огнем,\n" +
                "   Во грудь отверстую водвинул.\n" +
                "   Как труп в пустыне я лежал,\n" +
                "   И бога глас ко мне воззвал:\n" +
                "   «Восстань, пророк, и виждь, и внемли,\n" +
                "   Исполнись волею моей,\n" +
                "   И, обходя моря и земли,\n" +
                "   Глаголом жги сердца людей».");


        labelComplexity.setTextFill(Color.WHITE);
        body.getChildren().add(rools);
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

        ColumnConstraints controlCol0 = new ColumnConstraints();
        ColumnConstraints controlCol1 = new ColumnConstraints();
        ColumnConstraints controlCol2 = new ColumnConstraints();
        ColumnConstraints controlCol3 = new ColumnConstraints();
        ColumnConstraints controlCol4 = new ColumnConstraints();
        ColumnConstraints controlCol5 = new ColumnConstraints();

        controlCol0.setPercentWidth(11);
        controlCol1.setPercentWidth(12);
        controlCol2.setPercentWidth(18);
        controlCol3.setPercentWidth(18);
        controlCol4.setPercentWidth(20);
        controlCol5.setPercentWidth(20);

        buttonsGrid.getColumnConstraints().addAll(controlCol0,controlCol1,controlCol2,controlCol3,controlCol4,controlCol5);
        buttonsGrid.add(labelComplexity,0,0);
        buttonsGrid.add(classChoiseBox,1,0);
        buttonsGrid.add(prevLvl,2,0);
        buttonsGrid.add(nextLvl,3,0);
        buttonsGrid.add(reset,4,0);
        buttonsGrid.add(counterLabel,5,0);

        reset.setVisible(false);
        prevLvl.setVisible(false);
        nextLvl.setVisible(false);

        RowConstraints footerRow0 = new RowConstraints();
        ColumnConstraints footerCol0 = new ColumnConstraints();
        ColumnConstraints footerCol1 = new ColumnConstraints();
        ColumnConstraints footerCol2 = new ColumnConstraints();
        ColumnConstraints footerCol3 = new ColumnConstraints();
        ColumnConstraints footerCol4 = new ColumnConstraints();
        ColumnConstraints footerCol5 = new ColumnConstraints();
        ColumnConstraints footerCol6 = new ColumnConstraints();
        ColumnConstraints footerCol7 = new ColumnConstraints();

        footerRow0.setMaxHeight(footerGrid.getMaxHeight());
        footerCol0.setPercentWidth(7);
        footerCol1.setPercentWidth(7);
        footerCol2.setPercentWidth(7);
        footerCol3.setPercentWidth(7);
        footerCol4.setPercentWidth(7);
        footerCol5.setPercentWidth(7);
        footerCol6.setPercentWidth(7);
        footerCol7.setPercentWidth(35);

        footerCol0.setHalignment(HPos.CENTER);
        footerCol1.setHalignment(HPos.CENTER);
        footerCol2.setHalignment(HPos.CENTER);
        footerCol3.setHalignment(HPos.CENTER);
        footerCol4.setHalignment(HPos.CENTER);
        footerCol5.setHalignment(HPos.CENTER);
        footerCol6.setHalignment(HPos.CENTER);
        footerGrid.setVisible(false);
        footerGrid.getColumnConstraints().addAll(footerCol0,footerCol1,footerCol2,footerCol3,footerCol4,footerCol5,footerCol6,footerCol7);
        footerGrid.getRowConstraints().add(footerRow0);


        for(int i=0; i<LvlGenerator.GetLvlCountByGrade(1)+1;++i){
            Button levelNumber = new Button((i+1)+"");
            levelNumber.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            levelNumber.setAlignment(Pos.CENTER);
            levelNumber.setTextFill(Color.WHITESMOKE);
            levelNumber.setBackground(Background.EMPTY);
            levelNumber.setStyle("-fx-border-color: #ffffff");
            if(i == LvlGenerator.GetLvlCountByGrade(1)){
                levelNumber.setStyle("-fx-border-color: NONE");
                levelNumber.setTextFill(Color.WHITE);
                levelNumber.setText("       Результат: " + score);
            }

            footerGrid.add(levelNumber,i,0);
            levelButton.add(levelNumber);
        }


        classChoiseBox.setOnAction(event -> {
            body.getChildren().removeAll(rools);
            body.getChildren().add(gameGrid);
            if(!reset.isVisible() || !prevLvl.isVisible() || !nextLvl.isVisible()){
                if(level != 1) prevLvl.setVisible(true);
                else prevLvl.setVisible(false);

                if(level < LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setVisible(true);
                else nextLvl.setVisible(false);

                lvlLabel.setText(level.toString() + " уровень");
                lvlLabel.setVisible(true);
                nextLvl.setVisible(true);
                reset.setVisible(true);
            }
            footerGrid.setVisible(true);
            classChoiseBox.setDisable(true);
            grade = Integer.parseInt(classChoiseBox.getValue());
            gameGrid.getChildren().clear();
            gameGrid.getColumnConstraints().clear();
            gameGrid.getRowConstraints().clear();

            loadSaveLevelColor(levelButton,grade);
            levelButton.get(levelButton.size()-1).setText("       Результат: " + score);
            if(SaveMaker.isFile(level.toString(),grade)){
                loadSaveLevel(level.toString(),grade);
            } else {
                loadLevel(level.toString(),grade);
            }

        });

        prevLvl.setOnAction(event -> {
            reset.setDisable(false);
            SaveMaker.WriteLvlInfoToFile(SaveMaker.parseElementCollection(items,counter,lvlView),grade,level.toString());
            level--;

            if(level <= 1){
                prevLvl.setVisible(false);
            }
            else  prevLvl.setVisible(true);
            if(level >= LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setVisible(false);
            else nextLvl.setVisible(true);

            lvlLabel.setText(level.toString() + " уровень");
            lvlLabel.setVisible(true);
            if(SaveMaker.isFile(level.toString(),grade)){
                loadSaveLevel(level.toString(),grade);
            } else {
                loadLevel(level.toString(),grade);
            }
        });

        nextLvl.setOnAction(event -> {
            reset.setDisable(false);
            SaveMaker.WriteLvlInfoToFile(SaveMaker.parseElementCollection(items,counter,lvlView),grade,level.toString());
            level++;

            if(level > 1) prevLvl.setVisible(true);
            if(level >= LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setVisible(false);

            lvlLabel.setText(level.toString() + " уровень");
            lvlLabel.setVisible(true);
            if(SaveMaker.isFile(level.toString(),grade)){
                loadSaveLevel(level.toString(),grade);
            } else loadLevel(level.toString(),grade);
        });

        reset.setOnAction(event -> {
            loadLevel(level.toString(),grade);
            lvlLabel.setText(level.toString() + " уровень");
            lvlLabel.setVisible(true);
        });


        for(int i=0; i<levelButton.size()-1;i++){
            String file = ""+(i+1);
            levelButton.get(i).setOnMouseClicked(event -> {
                SaveMaker.WriteLvlInfoToFile(SaveMaker.parseElementCollection(items,counter,lvlView),grade,level.toString());
                this.level = Integer.parseInt(file);
                if(level <= 1){
                    prevLvl.setVisible(false);
                }
                else  prevLvl.setVisible(true);

                if(level >= LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setVisible(false);
                else nextLvl.setVisible(true);

                if(SaveMaker.isFile(file,grade)){
                    loadSaveLevel(file,grade);
                } else {
                    loadLevel(file,grade);
                    lvlLabel.setText( level.toString()+ " уровень");
                }
            });
        }
    }


    private void listen(){
        for(Element el:items){
            el.getImageButton().setOnMouseClicked(event -> {
                el.clickAction();
                if(counter<9999) counter++;

                if(level != 1) prevLvl.setVisible(true);
                else prevLvl.setVisible(false);

                if(level < LvlGenerator.GetLvlCountByGrade(level)) nextLvl.setVisible(true);
                else nextLvl.setVisible(false);

                counterLabel.setText(counter.toString());

                for(Element el2:items){
                    if ((el.getColumn() == el2.getColumn() && el.getRow() != el2.getRow()) || (el.getColumn() != el2.getColumn() && el.getRow() ==el2.getRow()))
                        el2.clickAction();
                }
                List<Integer> list = getLight(items);
                for(int i = 0; i < dimension*dimension; ++i){
                    items.get(i).setActivity(false);
                }

                for(int i = 0; i < list.size(); ++i){
                   items.get(list.get(i)).setActivity(true);
                }
                checkWin(items);
                if(checkWin(items)) {
                    loadLevelColor(levelButton,grade,counter,level);
                    levelButton.get(levelButton.size()-1).setText("       Результат: " + score);
                }
                else reDrawTask(items);
            });
        }
    }

    private boolean checkWin(List<Element> list){
        //Проверяет все ли элементы повернуты на 90 градусов
        //и возвращает true, если пользователь победил
        for(int i=0; i<dimension*dimension;++i){
            if(!list.get(i).isRotated()) return false;
        }
        for(Element element: items){
            element.getButton().setDisable(true);
            updateElementsSize(element);
        }
        lvlLabel.setText(level + " уровень пройден");
        reset.setDisable(true);
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

    private void loadLevel(String lvl, Integer grade){
        this.counter = 0;
        counterLabel.setText(counter.toString());
        this.lvlView = SaveMaker.getLevelStyle(SaveMaker.ReadDataFromFile(lvl,grade));
        if(lvlView == 1)
        body.setId("back1");
        else body.setId("back2");
        drawTask(SaveMaker.parseCollection(SaveMaker.ReadDataFromFile(lvl,grade),dimension),dimension);
    }

    private void loadSaveLevel(String lvl, Integer grade){
        List<Integer> loadedData = SaveMaker.ReadLvlInfoFromFile(lvl, grade);
        this.lvlView = SaveMaker.getLevelStyle(loadedData);
        this.counter = SaveMaker.getCounter(loadedData);
        counterLabel.setText(counter.toString());
        if(lvlView == 1)
        body.setId("back1");
        else body.setId("back2");
        items = SaveMaker.parseCollection(SaveMaker.ReadLvlInfoFromFile(level.toString(),grade),dimension);
//        for(Element el: items){
//            System.out.println("col="+el.getColumn()+" row= "+el.getRow()+" rot= "+el.isRotated()+" act="+el.isActivity());
//        }
        drawTask(SaveMaker.parseCollection(SaveMaker.ReadLvlInfoFromFile(level.toString(),grade),dimension),dimension);
        checkWin(items);
        if(!checkWin(items)) lvlLabel.setText(level + " уровень");
    }


    private void loadSaveLevelColor(List<Button> list ,Integer grade){
        List<Pair<Integer, Integer>> l = Stats.GetColorsAndScores(Stats.GetScoreForGrade(grade),grade);
        int color, score;
        for(int i=0; i < list.size()-1; ++i) {
            color = l.get(i).getKey();

            score = l.get(i).getValue();
            this.score +=  score;
            switch (color){
                case 3: levelButton.get(i).setBackground(new Background(
                        new BackgroundFill(Color.DARKGREEN,new CornerRadii(1),Insets.EMPTY)));
                break;

                case 2: list.get(i).setBackground(new Background(
                        new BackgroundFill(Color.GREEN,new CornerRadii(1),Insets.EMPTY)));
                break;

                case 1: list.get(i).setBackground(new Background(
                        new BackgroundFill(Color.LIGHTGREEN,new CornerRadii(1),Insets.EMPTY)));
                break;

                default:list.get(i).setBackground(Background.EMPTY);
            }
        }
    }

    private void loadLevelColor(List<Button> list ,Integer grade, Integer counter, Integer level){
        Pair<Integer, Integer> l = Stats.GetColorAndScore(counter,grade,level-1);
        int color = l.getKey();
        int score = l.getValue();
        this.score +=  score;
        switch (color){
            case 3: levelButton.get(level-1).setBackground(new Background(
                    new BackgroundFill(Color.DARKGREEN,new CornerRadii(1),Insets.EMPTY)));
                break;

            case 2: list.get(level-1).setBackground(new Background(
                    new BackgroundFill(Color.GREEN,new CornerRadii(1),Insets.EMPTY)));
                break;

            case 1: list.get(level-1).setBackground(new Background(
                    new BackgroundFill(Color.LIGHTGREEN,new CornerRadii(1),Insets.EMPTY)));
                break;

            default:list.get(level-1).setBackground(Background.EMPTY);
        }
    }

    private void drawTask(List<Element> list, Integer dimension){
        items.clear();
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        for(int i = 0; i < dimension; i++){
            double rowHeight = gameGrid.getPrefHeight() / dimension;
            double columnWidth = gameGrid.getPrefWidth() / dimension;
            gameGrid.getRowConstraints().add(new RowConstraints(rowHeight));
            gameGrid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }
        for(int i = 0; i < dimension*dimension; i++) {
            int col = i/dimension;
            int row = i-col*dimension;
            Element item = list.get(i);
            item.getButton().setDisable(false);
            gameGrid.add(item.getImageButton(),col,row);
            items.add(item);
        }
        List<Integer> lightListIndex = getLight(items);
        for(int j=0; j<lightListIndex.size();++j){
            items.get(lightListIndex.get(j)).setActivity(true);
        }
        for(Element el: items) {
            updateElementsSize(el);
        }
        listen();
    }

    private void updateElementsSize(Element el){
            el.getView().setFitHeight((gameGrid.getPrefHeight()/dimension)-15);
            el.getView().setFitWidth((gameGrid.getPrefWidth()/dimension)-15);
            el.getImageButton().setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            el.getImageButton().setBackground(Background.EMPTY);
    }

    private void reDrawTask(List<Element> items){
        gameGrid.getChildren().clear();
        gameGrid.getColumnConstraints().clear();
        gameGrid.getRowConstraints().clear();
        for(Element el:items){
            updateElementsSize(el);
            gameGrid.add(el.getImageButton(),el.getColumn(),el.getRow());
        }
    }

}
 