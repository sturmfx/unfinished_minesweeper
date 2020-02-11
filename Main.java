
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application
{
    public static  int start_width = 400;
    public static int start_height = 200;
    public static int x = 10;
    public static int y = 10;
    public static int px = 50;
    public static int py = 50;
    public static int mines = 15;
    public static VBox root = new VBox();
    public static StartWindow sw;
    public static Stage stage;

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        init_1();

        stage.setTitle("MineSweeper");
        stage.setScene(new Scene(root, start_width, start_height));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void init() throws Exception
    {
        Platform.runLater(() ->
        {
            stage = new Stage();
        });
    }


    public void init_1()
    {
        sw = new StartWindow();
        root.getChildren().add(sw);
    }

    public static void start_game(int x, int y, int p, int mines)
    {
        Main.x = x;
        Main.y = y;
        Main.px = p;
        Main.py = p;
        Main.mines = mines;
        root.getChildren().clear();
        stage.setWidth(x * px);
        stage.setHeight(y * py);
        root.getChildren().add(GameWindow.restart());
        GameWindow.draw();
    }

    public static void end_game()
    {
        root.getChildren().clear();
        stage.setWidth(start_width);
        stage.setHeight(start_height);
        root.getChildren().add(sw);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
