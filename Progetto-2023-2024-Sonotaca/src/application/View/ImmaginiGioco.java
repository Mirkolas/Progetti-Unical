package application.View;
import application.Model.ImpostazioniDim;
import application.Resources.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ImmaginiGioco {
    private Image personaggioDes;
    private Image personaggioSin;
    private Image SfondoLivello1;
    private Image SfondoLivello2;
    private Image SfondoLivello3;
    private Image SabbiaLivello1;
    private Image SlimeRosso;
    private Image BloccoMystery;
    private final Image SlimeRossoSX;
    private Image SlimeGrey;
    private final Image SlimeGreySX;
    private Image Teschio1;
    private Image Teschio2;
    private Image Teschio3;
    private Image Kill;
    private static Image Fine;
    private Image Acqua;
    private Image BloccoLivello2;
    private Image BloccoLivello3;
    private Image Trofeo;
    private Image Lava;
    private Image Mela;
    private Image Ciliegia;
    private Image Fragola;
    private Image MenuPrincipaleScreen;
    private Image SkinScreen;
    private Image BloccoUsato;
    private Image Vita;
    private Image Start;
    private Image Ball;
    private static Image Spento;
    private static Image Acceso;
    private static Image Riconoscimenti;
    private Image Titolo;
    private static Image Fire_icon;
    private static Image Water_icon;
    private static Image Fire_skin;
    private static Image Water_skin;
    private Image SlimeBlu;
    private Image BottoneMain;
    private Image GameOverSchermata;
    private final Image SlimeBluSX;
    private static Image Livello1;
    private static Image Livello2;
    private static Image Livello3;
    private static Image Livello1Lock;
    private static Image Livello2Lock;
    private static Image Livello3Lock;
    private Image HelpBackground;
    private static Image Freccia;
    private static Image Lavagna;
    private  static Image Freccia1;


    public ImmaginiGioco() throws IOException {
        SfondoLivello1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/SfondoLivello1.png")));
        SfondoLivello1 = SfondoLivello1.getScaledInstance(6240, ImpostazioniDim.Altezza_Finestra,Image.SCALE_SMOOTH);

        SfondoLivello2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/SfondoLivello2.png")));
        SfondoLivello2 = SfondoLivello2.getScaledInstance(1830, ImpostazioniDim.Altezza_Finestra,Image.SCALE_SMOOTH);

        SfondoLivello3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/SfondoLivello3.png")));
        SfondoLivello3 = SfondoLivello3.getScaledInstance(3120, ImpostazioniDim.Altezza_Finestra,Image.SCALE_SMOOTH);

        personaggioDes= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Personaggio/Fire.png")));
        personaggioDes = personaggioDes.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);
        personaggioSin= ImageUtil.flipImageHorizontally(personaggioDes);

        SabbiaLivello1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Sabbia.png")));
        SabbiaLivello1 = SabbiaLivello1.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        BloccoMystery = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/BloccoSpeciale.png")));
        BloccoMystery = BloccoMystery.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        Acceso = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Acceso.png")));
        Acceso = Acceso.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        Spento = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Spento.png")));
        Spento = Spento.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        Ball = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Abilita/Ball.png")));
        Ball = Ball.getScaledInstance(ImpostazioniDim.Altezza_Cella -5, ImpostazioniDim.Lunghezza_Cella -4,Image.SCALE_SMOOTH);

        Fine = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Fine.png")));
        Fine = Fine.getScaledInstance(ImpostazioniDim.Altezza_Cella*2 , ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);

        Acqua = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Acqua.jpeg")));
        Acqua = Acqua.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        BloccoLivello2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/BloccoMuro2.png")));
        BloccoLivello2 = BloccoLivello2.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        BloccoLivello3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/BloccoMuro3.png")));
        BloccoLivello3 = BloccoLivello3.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);


        Lava = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Lava.jpg")));
        Lava = Lava.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        Mela = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Apple.png")));
        Mela = Mela.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        Ciliegia = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Cherries.png")));
        Ciliegia = Ciliegia.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        Fragola = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Strawberry.png")));
        Fragola = Fragola.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        MenuPrincipaleScreen = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/MenuPrincipale/MenuPrincipaleScreen.png")));
        MenuPrincipaleScreen = MenuPrincipaleScreen.getScaledInstance(ImpostazioniDim.Lunghezza_Finestra, ImpostazioniDim.Altezza_Finestra,Image.SCALE_SMOOTH);

        BloccoUsato = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/BloccoUsato.png")));
        BloccoUsato = BloccoUsato.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);

        BottoneMain = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/MenuPrincipale/BottoneMainScreen.png")));
        BottoneMain = BottoneMain.getScaledInstance(200,50,Image.SCALE_SMOOTH);


        Vita = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Abilita/Vita.png")));
        Vita = Vita.getScaledInstance(ImpostazioniDim.Altezza_Cella /2+8, ImpostazioniDim.Lunghezza_Cella /2+8,Image.SCALE_SMOOTH);

        Riconoscimenti = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/MenuPrincipale/Riconoscimenti.png")));
        Riconoscimenti = Riconoscimenti.getScaledInstance(ImpostazioniDim.Altezza_Cella*3, ImpostazioniDim.Lunghezza_Cella*3,Image.SCALE_SMOOTH);

        Titolo = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/MenuPrincipale/Titolo.png")));
        Titolo = Titolo.getScaledInstance(ImpostazioniDim.Altezza_Cella *18, ImpostazioniDim.Lunghezza_Cella *4,Image.SCALE_SMOOTH);

        SkinScreen = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/SkinBackground.png")));
        SkinScreen = SkinScreen.getScaledInstance(ImpostazioniDim.Lunghezza_Finestra, ImpostazioniDim.Altezza_Finestra,Image.SCALE_SMOOTH);

        Fire_icon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/ScegliSkin/Fire_icon.png")));
        Fire_icon = Fire_icon.getScaledInstance(ImpostazioniDim.Altezza_Cella *4, ImpostazioniDim.Lunghezza_Cella *4,Image.SCALE_SMOOTH);
        Water_icon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/ScegliSkin/Water_Icon.png")));
        Water_icon = Water_icon.getScaledInstance(ImpostazioniDim.Altezza_Cella *4, ImpostazioniDim.Lunghezza_Cella *4,Image.SCALE_SMOOTH);

        Fire_skin = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/ScegliSkin/Fire_skin.png")));
        Fire_skin = Fire_skin.getScaledInstance(ImpostazioniDim.Altezza_Cella *5, ImpostazioniDim.Lunghezza_Cella *10,Image.SCALE_SMOOTH);
        Water_skin = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/ScegliSkin/Water_Skin.png")));
        Water_skin = Water_skin.getScaledInstance(ImpostazioniDim.Altezza_Cella *5, ImpostazioniDim.Lunghezza_Cella *10,Image.SCALE_SMOOTH);


        SlimeBlu = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Nemici/SlimeBlu.png")));
        SlimeBlu = SlimeBlu.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);
        SlimeBluSX =ImageUtil.flipImageHorizontally(SlimeBlu);

        SlimeRosso = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Nemici/SLimeRosso.png")));
        SlimeRosso = SlimeRosso.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);
        SlimeRossoSX =ImageUtil.flipImageHorizontally(SlimeRosso);

        SlimeGrey = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Nemici/SlimeGrey.png")));
        SlimeGrey = SlimeGrey.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella,Image.SCALE_SMOOTH);
        SlimeGreySX =ImageUtil.flipImageHorizontally(SlimeGrey);


        Freccia = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/MenuPrincipale/Freccia.jpg")));
        Freccia = Freccia.getScaledInstance(ImpostazioniDim.Altezza_Cella *2, ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);
        Freccia1 =ImageUtil.flipImageHorizontally(Freccia);


        Lavagna = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/MenuPrincipale/Lavagna.png")));
        Lavagna = Lavagna.getScaledInstance(ImpostazioniDim.Altezza_Cella *15, ImpostazioniDim.Lunghezza_Cella *15,Image.SCALE_SMOOTH);


        Start = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Blocchi/Start.png")));
        Start = Start.getScaledInstance(ImpostazioniDim.Altezza_Cella *3, ImpostazioniDim.Lunghezza_Cella *3,Image.SCALE_SMOOTH);

        GameOverSchermata = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/GameOver/GameOver.png")));
        GameOverSchermata = GameOverSchermata.getScaledInstance(ImpostazioniDim.Altezza_Cella *4, ImpostazioniDim.Lunghezza_Cella *4,Image.SCALE_SMOOTH);

        Trofeo = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SelezioneMappe/Trofeo.png")));
        Trofeo = Trofeo.getScaledInstance(ImpostazioniDim.Altezza_Cella *2+10, ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);

        Teschio1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SelezioneMappe/Teschio1.png")));
        Teschio1 = Teschio1.getScaledInstance(ImpostazioniDim.Altezza_Cella *2+10, ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);

        Teschio2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SelezioneMappe/Teschio2.png")));
        Teschio2 = Teschio2.getScaledInstance(ImpostazioniDim.Altezza_Cella *2+10, ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);

        Teschio3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SelezioneMappe/Teschio3.png")));
        Teschio3 = Teschio3.getScaledInstance(ImpostazioniDim.Altezza_Cella *2-10, ImpostazioniDim.Lunghezza_Cella *2-10,Image.SCALE_SMOOTH);

        Kill = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SelezioneMappe/TrofeoMob.png")));
        Kill = Kill.getScaledInstance(ImpostazioniDim.Altezza_Cella *2+20, ImpostazioniDim.Lunghezza_Cella *2+15,Image.SCALE_SMOOTH);

        Livello1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/Livello1.png")));
        Livello1 = Livello1.getScaledInstance(ImpostazioniDim.Altezza_Cella *7, ImpostazioniDim.Lunghezza_Cella *7,Image.SCALE_SMOOTH);

        Livello2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/Livello2.png")));
        Livello2 = Livello2.getScaledInstance(ImpostazioniDim.Altezza_Cella *7, ImpostazioniDim.Lunghezza_Cella *7,Image.SCALE_SMOOTH);

        Livello3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/Livello3.png")));
        Livello3 = Livello3.getScaledInstance(ImpostazioniDim.Altezza_Cella *7, ImpostazioniDim.Lunghezza_Cella *7,Image.SCALE_SMOOTH);

        Livello1Lock = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/Livello1Lock.png")));
        Livello1Lock = Livello1Lock.getScaledInstance(ImpostazioniDim.Altezza_Cella *7, ImpostazioniDim.Lunghezza_Cella *7,Image.SCALE_SMOOTH);

        Livello2Lock = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/Livello2Lock.png")));
        Livello2Lock = Livello2Lock.getScaledInstance(ImpostazioniDim.Altezza_Cella *7, ImpostazioniDim.Lunghezza_Cella *7,Image.SCALE_SMOOTH);

        Livello3Lock = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/Livello3Lock.png")));
        Livello3Lock = Livello3Lock.getScaledInstance(ImpostazioniDim.Altezza_Cella *7, ImpostazioniDim.Lunghezza_Cella *7,Image.SCALE_SMOOTH);

        HelpBackground = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/SfondiBackground/HelpBackground.jpg")));
        HelpBackground = HelpBackground.getScaledInstance(ImpostazioniDim.Lunghezza_Finestra, ImpostazioniDim.Altezza_Finestra,Image.SCALE_SMOOTH);





    }

    public static Image getFine() {
        return Fine;
    }
    public Image getTrofeo() {
        return Trofeo;
    }
    public Image getStart() {
        return Start;
    }
    public Image getHelpBackground() {
        return HelpBackground;
    }

    public static Image getMap1(boolean lock) {
        if(lock) return Livello1Lock;
        return Livello1;
    }
    public static Image getMap2(boolean lock) {
        if(lock) return Livello2Lock;
        return Livello2;
    }
    public static Image getMap3(boolean lock) {
        if(lock) return Livello3Lock;
        return Livello3;
    }
    public Image getGameOverSchermata() {
        return GameOverSchermata;
    }
    public Image getPersonaggio(int direzione) {
        if (direzione == ImpostazioniDim.MUOVI_DESTRA) return personaggioDes;
        else return personaggioSin;
    }
    public static Image getFire_skin() {
        return Fire_skin;
    }
    public static Image getWater_skin() {
        return Water_skin;
    }
    public static Image getSkinFuoco() {
        return Fire_icon;
    }
    public static Image getSkinAcqua() {
        return Water_icon;
    }
    public void setPersonaggio(String p) throws IOException {
        personaggioDes= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/application/Resources/Personaggio/" +p+".png")));
        personaggioDes = personaggioDes.getScaledInstance(ImpostazioniDim.Altezza_Cella, ImpostazioniDim.Lunghezza_Cella *2,Image.SCALE_SMOOTH);
        personaggioSin=ImageUtil.flipImageHorizontally(personaggioDes);
    }
    public Image getSkinScreen() {
        return SkinScreen;
    }
    public Image getTitolo() {
        return Titolo;
    }
    public static Image getRiconoscimenti() {
        return Riconoscimenti;
    }

    public Image getBackgroundImage(int liv) {
        if(liv==1) return SfondoLivello1;
        else if(liv==2) return SfondoLivello2;
        return SfondoLivello3;
    }
    public Image getMela(int liv) {
        if(liv==1) return Mela;
        else if(liv==2) return Ciliegia;
        return Fragola;
    }
    public Image getBloccoMuro(int liv) {
        if(liv==1) return SabbiaLivello1;
        else if(liv==2) return BloccoLivello2;
        return BloccoLivello3;
    }
    public Image getBloccoSpeciale(int liv) {return BloccoMystery;}
    public Image getBall(int liv) {return Ball;}
    public Image getBottoneMain() {
        return BottoneMain;
    }
    public Image getSlime(int direzione, int liv) {
        if (liv == 1) {
            if (direzione == 1) {return SlimeBlu;}
            return SlimeBluSX;
        }
        else if (liv == 2) {
            if (direzione == 1) {return SlimeRosso;}
            return SlimeRossoSX;
        }
        else if (liv == 3) {
            if (direzione == 1) {return SlimeGrey;}
            return SlimeGreySX;
        }
        return null;
    }
    public Image getKill() {
        return Kill;
    }
    public Image getMorte(int liv) {
        if(Objects.equals(GamePanel.getPersonaggio(), "Fire")) return Acqua;
        return Lava;
    }
    public Image getTrofeoMob(int liv){
        if (liv==1){return Teschio1;}
        else if (liv==2){return Teschio2;}
        return Teschio3;
    }

    public static Image getLavagna(){return Lavagna;}
    public static Image getFreccia(int dir) {
        if (dir == 1){
            return Freccia;
        }
        return Freccia1;

    }
    public Image getTeschio1() {
        return Teschio1;
    }
    public Image getTeschio2() {
        return Teschio2;
    }
    public Image getTeschio3() {
        return Teschio3;
    }

    public Image getBloccoUsato() {
        return BloccoUsato;
    }
    public Image getMenuPrincipaleScreen() {
        return MenuPrincipaleScreen;
    }
    public Image getVita(){
        return Vita;
    }

    public static Image getSpento(boolean ris){
        if (ris){return Acceso;}
        else if (!ris){return Spento;}
        return null;
    }
}
