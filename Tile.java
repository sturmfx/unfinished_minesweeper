
public class Tile
{
    public static int flags = 0;
    public static int mines = 0;
    public int x = 0;
    public int y = 0;
    public boolean is_visible = false;
    public boolean is_flag = false;
    public boolean is_mine = false;
    public boolean is_boom = false;
    public int mines_around = 0;

    public Tile(int x, int y, boolean is_visible, boolean is_flag, boolean is_mine, boolean is_boom)
    {
        this.x = x;
        this.y = y;
        this.is_visible = is_visible;
        this.is_flag = is_flag;
        this.is_mine = is_mine;
        this.is_boom = is_boom;
        this.mines_around = 0;
    }
}
