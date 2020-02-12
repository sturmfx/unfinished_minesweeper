package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main extends Application
{
    public Random random = new Random();
    public int x = 10;
    public int y = 10;
    public int mine_amount = 25;

    public int pixel = 50;
    public int border = 2;

    public int flags_set = 25;
    public boolean first_click = false;
    public boolean boom = false;
    public boolean win = false;

    boolean[][] is_visible = new boolean[x][y];
    boolean[][] is_flag = new boolean[x][y];
    boolean[][] is_mine = new boolean[x][y];
    int[][] mines = new int[x][y];

    public VBox root = new VBox();
    public Button new_game = new Button("NEW GAME");
    public Label report = new Label();
    public Canvas can = new Canvas(pixel * x, pixel * y);
    public GraphicsContext gc = can.getGraphicsContext2D();;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        report.setMinWidth(pixel * x);
        new_game.setMinWidth(pixel * x);
        new_game.setOnAction(event ->
        {
            reset();
            draw();

        });

        can.setOnMouseClicked(event -> can(event));

        root.getChildren().addAll(new_game, report, can);

        reset();
        draw();
        primaryStage.setTitle("MineSweeper");
        primaryStage.setScene(new Scene(root, pixel * x, pixel * y + 50));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e ->
        {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }

    public void reset()
    {
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                is_visible[i][j] = false;
                is_flag[i][j] = false;
                is_mine[i][j] = false;
                mines[i][j] = 0;
            }
        }
        flags_set = 0;
        first_click = false;
        boom = false;
        win = false;

    }

    public void start(int fx, int fy)
    {
        int temp_mines = 0;

        while(temp_mines < mine_amount)
        {
            int tx = random.nextInt(x);
            int ty = random.nextInt(y);

            if(!is_mine[tx][ty]&&(tx != fx)&&(ty != fy))
            {
                is_mine[tx][ty] = true;
                temp_mines++;
            }
        }

        if(is_flag[fx][fy])
        {
            is_flag[fx][fy] = false;
            flags_set--;
        }

        first_click = true;
        draw();
    }

    public void draw()
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, pixel * x, pixel * y);
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(border);
                gc.strokeRect(pixel * i, pixel * j, pixel, pixel);

                if(is_visible[i][j])
                {
                    if(mines[i][j] == 0)
                    {
                        gc.setFill(Color.WHITE);
                        gc.fillRect(pixel * i + border, pixel * j + border, pixel - 2 * border, pixel - 2 * border);
                    }
                    else
                    {
                        gc.setFill(Color.WHITE);
                        gc.fillRect(pixel * i + border, pixel * j + border, pixel - 2 * border, pixel - 2 * border);

                        gc.setStroke(Color.GREEN);
                        gc.strokeText(String.valueOf(mines[i][j]), pixel * i + pixel * 0.5 - 3, pixel * j + pixel * 0.5);
                    }

                }
                else
                {
                    if(is_flag[i][j])
                    {
                        gc.setFill(Color.WHEAT);
                        gc.fillRect(pixel * i + border, pixel * j + border, pixel - 2 * border, pixel - 2 * border);

                        gc.setFill(Color.RED);
                        gc.fillPolygon(new double[]{pixel * i + pixel * 0.5 - 2, pixel * i + pixel * 0.5 + 2, pixel * (i + 1), pixel * i + pixel * 0.5 + 2, pixel * i + pixel * 0.5 + 2, pixel * i + pixel * 0.5 - 2},
                                       new double[]{pixel * j + 5, pixel * j + 5, pixel * j + (pixel - 10)*0.25 + 5, pixel * j + pixel * 0.5, pixel * (j + 1) - 5, pixel * (j + 1) - 5}, 6);
                    }
                    else
                    {
                        gc.setFill(Color.WHEAT);
                        gc.fillRect(pixel * i + border, pixel * j + border, pixel - 2 * border, pixel - 2 * border);
                    }
                }

                if(boom && is_mine[i][j])
                {
                    gc.setFill(Color.WHEAT);
                    gc.fillRect(pixel * i + border, pixel * j + border, pixel - 2 * border, pixel - 2 * border);
                    gc.setFill(Color.PURPLE);
                    gc.fillOval(pixel * i + 10, pixel * j + 10, pixel - 20, pixel - 20);
                }
            }
        }
        report.setText("FLAGS SET: " + flags_set);
        if(is_win())
        {
            gc.setFill(Color.BLUE);
            gc.fillPolygon(new double[]{0, 40, 80, 120, 160, 200, 240, 160, 120, 80},
                    new double[]{0, 0, 80, 40, 80, 0, 0, 120, 80, 120}, 10);

            gc.fillPolygon(new double[]{280, 320, 320, 280},
                    new double[]{0, 0, 20, 20}, 4);

            gc.fillPolygon(new double[]{280, 320, 320, 280},
                    new double[]{40, 40, 120, 120}, 4);

            gc.fillPolygon(new double[]{360, 400, 440, 440, 480, 480, 440, 400, 400, 360},
                    new double[]{0, 0, 80, 0, 0, 120, 120, 40, 120, 120}, 10);
        }
    }

    public void can(MouseEvent event)
    {
        int tx = (int) (event.getX() / pixel);
        int ty = (int) (event.getY() / pixel);

        if(!win)
        {
            if (!boom)
            {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    if (!first_click)
                    {
                        start(tx, ty);
                        calculate();
                        if (mines[tx][ty] == 0)
                        {
                            flood_fill(tx, ty);
                        }
                        else
                            {
                                if(is_flag[tx][ty])
                                {
                                    is_flag[tx][ty] = false;
                                    flags_set--;
                                }

                                is_visible[tx][ty] = true;
                            }

                    }
                    else
                        {
                        if (is_mine[tx][ty])
                        {
                            boom = true;
                        }
                        else
                            {
                            if (mines[tx][ty] == 0)
                            {
                                flood_fill(tx, ty);
                            }
                            else
                                {
                                    if(is_flag[tx][ty])
                                    {
                                        is_flag[tx][ty] = false;
                                        flags_set--;
                                    }

                                    is_visible[tx][ty] = true;
                                }
                        }
                    }

                } else
                    {
                    if (!is_visible[tx][ty])
                    {
                        if (!is_flag[tx][ty])
                        {
                            if (flags_set < mine_amount)
                            {
                                is_flag[tx][ty] = true;
                                flags_set++;
                            }
                        }
                        else
                            {
                                is_flag[tx][ty] = false;
                                flags_set--;
                            }
                    }
                }

                event.consume();
            }
            if(is_win())
            {
                win = true;
            }
            calc_flags();
            draw();
        }

    }

    public boolean is_win()
    {
        int temp_mines = 0;
        boolean result = true;
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                if(is_mine[i][j])
                {
                    temp_mines++;
                    result = result && is_flag[i][j];
                }
            }
        }
        if(temp_mines == 0)
        {
            return false;
        }
        else
        {
            return result;
        }

    }

    public void calc_flags()
    {
        int flags = 0;

        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                if(is_visible[i][j] && is_flag[i][j])
                {
                    is_flag[i][j] = false;
                }

                if(is_flag[i][j])
                {
                    flags++;
                }
            }
        }

        flags_set = flags;
    }

    public void flood_fill(int x, int y)
    {
        boolean[][] hits = new boolean[this.x][this.y];
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty())
        {
            Point p = queue.remove();

            if(flood_fill_1(hits,p.x,p.y))
            {
                queue.add(new Point(p.x,p.y - 1));
                queue.add(new Point(p.x,p.y + 1));
                queue.add(new Point(p.x - 1,p.y));
                queue.add(new Point(p.x + 1,p.y));
            }
        }
    }

    public boolean flood_fill_1(boolean[][] hits, int x, int y)
    {
        if (x < 0 || x >= this.x || y < 0 || y >= this.y) return false;
        if(hits[x][y])
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            return false;
        }
        if (mines[x][y] != 0)
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            return false;
        }

        if(is_flag[x][y])
        {
            is_flag[x][y] = false;
            flags_set--;
        }

        is_visible[x][y] = true;

        if((x > 0)&&(y > 0))
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x - 1][y - 1] = true;
        }

        if(y > 0)
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x][y - 1] = true;
        }

        if((x < this.x - 1)&&(y > 0))
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x + 1][y - 1] = true;
        }

        if(x > 0)
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x - 1][y] = true;
        }

        if(x < this.x - 1)
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x + 1][y] = true;
        }

        if((x > 0)&&(y < this.y - 1))
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x - 1][y + 1] = true;
        }

        if(y < this.y - 1)
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x][y + 1] = true;
        }

        if((x < this.x - 1)&&(y < this.y - 1))
        {
            if(is_flag[x][y])
            {
                is_flag[x][y] = false;
                flags_set--;
            }
            is_visible[x + 1][y + 1] = true;
        }

        hits[x][y] = true;
        return true;
    }

    public void calculate()
    {
        for(int i = 0; i < this.x; i++)
        {
            for(int j = 0; j < this.y; j++)
            {
                if(!is_mine[i][j])
                {
                    int mines_around = 0;

                    //1
                    if((i > 0)&&(j > 0))
                    {
                        if(is_mine[i - 1][j - 1])
                        {
                            mines_around++;
                        }
                    }

                    //2
                    if(j > 0)
                    {
                        if(is_mine[i][j - 1])
                        {
                            mines_around++;
                        }
                    }

                    //3
                    if((i < this.x - 1)&&(j > 0))
                    {
                        if(is_mine[i + 1][j - 1])
                        {
                            mines_around++;
                        }
                    }

                    //4
                    if(i > 0)
                    {
                        if(is_mine[i - 1][j])
                        {
                            mines_around++;
                        }
                    }

                    //6
                    if(i < this.x - 1)
                    {
                        if(is_mine[i + 1][j])
                        {
                            mines_around++;
                        }
                    }

                    //7
                    if((i > 0)&&(j < this.y - 1))
                    {
                        if(is_mine[i - 1][j + 1])
                        {
                            mines_around++;
                        }
                    }

                    //8
                    if(j < this.y - 1)
                    {
                        if(is_mine[i][j + 1])
                        {
                            mines_around++;
                        }
                    }

                    //9
                    if((i < this.x - 1)&&(j < this.y - 1))
                    {
                        if(is_mine[i + 1][j + 1])
                        {
                            mines_around++;
                        }
                    }
                    mines[i][j] = mines_around;
                }
            }
        }
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}

