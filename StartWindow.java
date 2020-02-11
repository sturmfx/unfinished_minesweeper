
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class StartWindow extends GridPane
{
    public int min_x = 10;
    public int max_x = 30;
    public int min_y = 10;
    public int max_y = 20;
    public int min_tile_size = 50;
    public int max_tile_size = 100;
    public int min_mines = 5;
    public int max_mines = 25;
    public int max_width = 1500;
    public int max_height = 1000;

    public int default_x = 10;
    public int default_y = 10;
    public int default_tile_size = 50;
    public int default_mines = 15;

    TextField x_tiles = new TextField();
    Label x_tiles_l = new Label("NUMBER OF TILES (WIDTH)");
    TextField y_tiles = new TextField();
    Label y_tiles_l = new Label("NUMBER OF TILES (HEIGHT)");
    TextField tile_size = new TextField();
    Label tile_size_l = new Label("TILE SIZE IN PIXELS");
    TextField percent_of_mines = new TextField();
    Label percent_of_mines_l = new Label("PERCENT OF TILES THAT ARE MINES");
    Button start_game = new Button("START GAME");
    Label error = new Label();

    public StartWindow()
    {
        start_game.setOnAction(event -> start_game());
        addRow(0, x_tiles_l, x_tiles);
        addRow(1, y_tiles_l, y_tiles);
        addRow(2, tile_size_l, tile_size);
        addRow(3, percent_of_mines_l, percent_of_mines);
        addRow(4, start_game);
        addRow(5, error);

        x_tiles.setText(String.valueOf(default_x));
        y_tiles.setText(String.valueOf(default_y));
        tile_size.setText(String.valueOf(default_tile_size));
        percent_of_mines.setText(String.valueOf(default_mines));
    }

    public void start_game()
    {
        int temp_x = 0;
        int temp_y = 0;
        int temp_size = 0;
        int temp_mines = 0;

        try
        {
            temp_x = Integer.parseInt(x_tiles.getText());
        }
        catch(NumberFormatException e)
        {
            temp_x = default_x;
        }

        try
        {
            temp_y = Integer.parseInt(y_tiles.getText());
        }
        catch(NumberFormatException e)
        {
            temp_y = default_y;
        }

        try
        {
            temp_size = Integer.parseInt(tile_size.getText());
        }
        catch(NumberFormatException e)
        {
            temp_size = default_tile_size;
        }

        try
        {
            temp_mines = Integer.parseInt(percent_of_mines.getText());
        }
        catch(NumberFormatException e)
        {
            temp_mines = default_mines;
        }



        if((temp_x >= min_x)&&(temp_x <= max_x)&&(temp_y >= min_y)&&(temp_y <= max_y)&&(temp_size >= min_tile_size)&&(temp_size <= max_tile_size)&&(temp_mines >= min_mines)&&(temp_mines <= max_mines)&&(temp_x * temp_size <= max_width)&&(temp_y * temp_size <= max_height))
        {

            Main.start_game(temp_x, temp_y,temp_size, temp_mines);
        }

        x_tiles.setText(String.valueOf(default_x));
        y_tiles.setText(String.valueOf(default_y));
        tile_size.setText(String.valueOf(default_tile_size));
        percent_of_mines.setText(String.valueOf(default_mines));

    }
}
