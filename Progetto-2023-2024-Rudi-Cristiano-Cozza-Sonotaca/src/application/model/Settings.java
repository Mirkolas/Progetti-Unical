package application.model;

public class Settings {

    // serve per la view per far vedere solo il filtro
    public final static int Filtro_Size_Riga = 20;
    public final static int Filtro_Size_Colonna = 30;


    // grandezza Jframe da visualizzare
    public static final int WINDOW_SIZE_Y = Filtro_Size_Riga*30; //20*30=600
    public static final int WINDOW_SIZE_X = Filtro_Size_Colonna*30; //30*30= 900
    public final static int CELL_SIZE_RIGA = WINDOW_SIZE_Y /Filtro_Size_Riga;
    public final static int CELL_SIZE_COLONNA = WINDOW_SIZE_X /Filtro_Size_Colonna;

    //costanti che associano ad un valore il tipo di movimento del giocatore
    public final static int MOVE_LEFT = -1;
    public final static int JUMP = 2;
    public final static int MOVE_RIGHT = 1 ;
    public final static int PAUSE = 9;
    public static final int NOT_MOVING = 0 ;


}
