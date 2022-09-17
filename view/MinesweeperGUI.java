package view;

import java.util.Collection;

import backtracker.Backtracker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.*;

public class MinesweeperGUI extends Application {

    private static final int COLS = 5;
    private static final int ROWS = 5;
    private static final int MINES = 5;
    private Button[][] buttons = new Button[ROWS][COLS];
    Minesweeper board = new Minesweeper(COLS, ROWS, MINES);

    @Override
    public void start(Stage stage) {
        //Minesweeper board = new Minesweeper(COLS, ROWS, MINES);

        BorderPane bPane = new BorderPane();

        //adds the right boxes
        VBox vbox = new VBox();
        Label movesCount = makeMoveCountLabel(board);
        vbox.getChildren().add(movesCount);
        Label minesCount = makeMineCountLabel(board);
        vbox.getChildren().add(minesCount);

        GridPane gridPane = new GridPane();
        Label statusLabel = makeStatusLabel(board);

        Button hint = makeHintButton();
        hint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                Collection<Location> locations = board.getPossibleSelections();
                Location location = (Location) locations.toArray()[0];
                buttons[location.getRow()][location.getCol()].setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        
        vbox.getChildren().add(hint);
        Button solveButton = makeSolveButton();
        solveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                MinesweeperConfiguration initconfig = new  MinesweeperConfiguration(board);
                Backtracker backtracker = new Backtracker(false);
                MinesweeperConfiguration goal = (MinesweeperConfiguration)backtracker.solve(initconfig);
                new Thread(() -> {
                for(Location l : goal.getMinesweeperList()){
                    Platform.runLater(() ->{
                        try{
                            board.makeSelection(l);
                            System.out.println(board.toString());
                        }catch(MinesweeperException e){

                        }
                    });
                    try{
                        Thread.sleep(200);
                    }
                    catch(InterruptedException e){

                    }
                }
                }).start();

            }
        });
        vbox.getChildren().add(solveButton);


        //set reset button on the stage
        //resets the game
        Button Reset = makeResetButton();
        Reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                //reset by creating new instance of board
                board = new Minesweeper(COLS, ROWS, MINES);
                movesCount.setText("Move count: " + Integer.toString(board.getMoves()));
                statusLabel.setText("In progress");
                statusLabel.setBackground(new Background( new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                for (int col = 0; col < COLS; col++) {
                    for (int row = 0; row < ROWS; row++) {
                        gridPane.getChildren().remove(buttons[row][col]);
                    }
                }

                for (int col = 0; col < COLS; col++) {
                    for (int row = 0; row < ROWS; row++) {
                        Button button = makeButton();
                        button.setOnAction(new CellHandler(board, new Location(row, col)));
                        buttons[row][col] = button;
                        gridPane.add(button, col, row);
                    }
                }
                board.register(new MinesweeperObserver() {
                    @Override
                    public void cellUpdated(Location location) {
                        // TODO Auto-generated method stub
                        char value = board.getSymbol(location);
                        movesCount.setText("Move count: " + Integer.toString(board.getMoves()));
                        minesCount.setText("Mine count: " + MINES);
                        if(board.getGameState() == GameState.LOST){
                            statusLabel.setBackground(new Background( new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                            statusLabel.setText("Lost");
                        }
                        else if(board.getGameState() == GameState.WON){
                            statusLabel.setBackground(new Background( new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                            statusLabel.setText("WON");
                        }
                        else if(board.getGameState() == GameState.IN_PROGRESS){
                            statusLabel.setBackground(new Background( new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                            statusLabel.setText("In progress");
                        }
                        if (value == 'M') {
                            //create the mine image
                            
                            Image img = new Image("media/images/mine24.png");
                            ImageView view = new ImageView(img);
                          
                            //button.setPrefSize(50,50);
                            view.setFitHeight(30);
                            view.setPreserveRatio(true);

                            //set the image on the button
                            buttons[location.getRow()][location.getCol()].setGraphic(view);
                            buttons[location.getRow()][location.getCol()].setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                            buttons[location.getRow()][location.getCol()].setDisable(true);
                            buttons[location.getRow()][location.getCol()].setOpacity(1);
                        } else {
                            if(value != '0'){
                                buttons[location.getRow()][location.getCol()].setText(Character.toString(value));
                                buttons[location.getRow()][location.getCol()].setFont(new Font("Arial", 24));
                                if(value == '1'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.BLUE);
                                }
                                else if(value == '2'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.RED);
                                }
                                else if(value == '3'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.GREEN);
                                }
                                else if(value == '4'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.PURPLE);
                                }
                                else if(value == '5'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.YELLOW);
                                }
                                else if(value == '6'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.LIGHTBLUE);
                                }
                                else if(value == '7'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.BROWN);
                                }
                                else if(value == '8'){
                                    buttons[location.getRow()][location.getCol()].setTextFill(Color.BLACK);
                                }
                            }
                            buttons[location.getRow()][location.getCol()].setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                            buttons[location.getRow()][location.getCol()].setDisable(true);
                            buttons[location.getRow()][location.getCol()].setOpacity(1);
                        }
                    }
                });

            }
        });


        vbox.getChildren().add(Reset);
       

        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                Button button = makeButton();
                button.setOnAction(new CellHandler(board, new Location(row, col)));
                buttons[row][col] = button;
                gridPane.add(button, col, row);
            }
        }


        board.register(new MinesweeperObserver() {

            @Override
            public void cellUpdated(Location location) {
                // TODO Auto-generated method stub
                char value = board.getSymbol(location);
                
                movesCount.setText("Move count: " + Integer.toString(board.getMoves()));
                minesCount.setText("Mine count: " + MINES);
                if(board.getGameState() == GameState.LOST){
                    statusLabel.setBackground(new Background( new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    statusLabel.setText("Lost");
                }
                else if(board.getGameState() == GameState.WON){
                    statusLabel.setBackground(new Background( new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    statusLabel.setText("WON");
                }
                else if(board.getGameState() == GameState.IN_PROGRESS){
                    statusLabel.setBackground(new Background( new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
                    statusLabel.setText("In progress");
                }
                if (value == 'M') {
                
                    //create the mine image
                    Image img = new Image("media/images/mine24.png");
                    ImageView view = new ImageView(img);
                   
                    //button.setPrefSize(50,50);
                    view.setFitHeight(30);
                    view.setPreserveRatio(true);

                    //set the image on the button
                    buttons[location.getRow()][location.getCol()].setGraphic(view);
                    buttons[location.getRow()][location.getCol()].setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    buttons[location.getRow()][location.getCol()].setDisable(true);
                    buttons[location.getRow()][location.getCol()].setOpacity(1);
                } else {
                    if(value != '0'){
                        buttons[location.getRow()][location.getCol()].setText(Character.toString(value));
                        buttons[location.getRow()][location.getCol()].setFont(new Font("Arial", 24));
                        if(value == '1'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.BLUE);
                        }
                        else if(value == '2'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.RED);
                        }
                        else if(value == '3'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.GREEN);
                        }
                        else if(value == '4'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.PURPLE);
                        }
                        else if(value == '5'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.YELLOW);
                        }
                        else if(value == '6'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.LIGHTBLUE);
                        }
                        else if(value == '7'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.BROWN);
                        }
                        else if(value == '8'){
                            buttons[location.getRow()][location.getCol()].setTextFill(Color.BLACK);
                        }
                    }
                    buttons[location.getRow()][location.getCol()].setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    buttons[location.getRow()][location.getCol()].setDisable(true);
                    buttons[location.getRow()][location.getCol()].setOpacity(1);
                }
            }
        });
       
        
        //we set the position of the parts
        bPane.setBottom(statusLabel);
        bPane.setRight(gridPane);
        bPane.setLeft(vbox);


        stage.setScene(new Scene(bPane));
        stage.setTitle("Minesweeper");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    private static Button makeButton(){
        Button button = new Button();
        button.setPrefSize(50,50);
        return button;
    }

    private static Button makeHintButton(){
        Button button = new Button("Hint");
        button.setPrefSize(100,50);
        return button;
    }
    // make reset button
    private static Button makeResetButton(){
        Button button = new Button("Reset");
        button.setPrefSize(100,50);
        return button;
    }

    private static Button makeSolveButton(){
        Button button = new Button("Solve");
        button.setPrefSize(100,50);
        return button;
    }

    private static Label makeMoveCountLabel(Minesweeper board){
        Label moveCountLabel = new Label("Move count: " + board.getMoves());
        moveCountLabel.setPrefSize(100,50);
        moveCountLabel.setAlignment(Pos.CENTER);
        moveCountLabel.setFont(new Font("Arial", 12));
        return moveCountLabel;
    }

    private static Label makeMineCountLabel(Minesweeper board){
        Label mineCountLabel = new Label("Mine count: " + MINES);
        mineCountLabel.setPrefSize(100,50);
        mineCountLabel.setAlignment(Pos.CENTER);
        mineCountLabel.setFont(new Font("Arial", 12));
        return mineCountLabel;
    }

    

    private static Label makeStatusLabel(Minesweeper board){
        Label moveStatusLabel = makeLabel("In progress ", Color.YELLOW);
        moveStatusLabel.setMinSize(50*COLS+100,50);
        moveStatusLabel.setAlignment(Pos.CENTER);
        //moveStatusLabel.setBackground(new Background( new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        return moveStatusLabel;
    }

    public static Label makeLabel(String text, Color backgroundColor){
        Label label = new Label(text);
        label.setFont(new Font("Arial", 14));
        label.setBackground(new Background( new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
        
        return label;


    }

}

class CellHandler implements EventHandler<ActionEvent>{
    private Minesweeper board;
    private Location l;
    

    public CellHandler(Minesweeper board, Location l){
        this.board = board;
        this.l = l;
     
    }

    @Override
    public void handle(ActionEvent arg0) {
        // TODO Auto-generated method stub
            try{
                board.makeSelection(l);
                
            }
            catch(MinesweeperException e){
            }
    
    }

}

