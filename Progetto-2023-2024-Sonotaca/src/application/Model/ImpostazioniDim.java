package application.Model;

public class ImpostazioniDim {
    public final static int Righe = 32;
    public final static int Colonne = 30;
    public static final int Altezza_Finestra = Righe *25;
    public static final int Lunghezza_Finestra = Colonne *25;
    public final static int Altezza_Cella = 25;
    public final static int Lunghezza_Cella = 25;

    public final static int MUOVI_SINISTRA = -1;
    public final static int SALTA = 2;
    public final static int MUOVI_DESTRA = 1 ;
    public final static int PAUSA = 9;
    public static final int NON_IN_MOVIMENTO = 0 ;
    public static final int SHOOT = 3 ;
    public static final int PULSANTE=4;
}
