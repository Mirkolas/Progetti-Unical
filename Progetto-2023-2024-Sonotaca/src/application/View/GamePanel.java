package application.View;

import application.Block;
import application.Stato;
import application.Audio.Sound;
import application.Controller.Controller;
import application.Model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GamePanel extends JPanel {
    private int count=0;
    private  final ImmaginiGioco immaginiGioco;
    public static Font Stile1;
    private final Font Corsivo;
    public static Font Stile2;
    private MenuPrincipale Menu;
    private application.View.Vittoria Vittoria;
    private application.View.ScegliSkin ScegliSkin;
    private application.View.Pausa Pausa;
    private Aiuto Aiuto;
    private static SelezionaMappa SelezionaMappa;
    private GameOver GameOver;
    private Sviluppatore About;

    private final Map<Object, Stato> Map;
    private boolean drawedPlayer=false;
    private int ms,sec,min;
    private String minPlus,msPlus,secPlus;
    public static void setLock2(boolean lock2) {
        GamePanel.lock2 = lock2;
    }
    public static void setLock3(boolean lock3) {
        GamePanel.lock3 = lock3;
    }
    public static void setLockQuest(boolean lockQuest) {
        GamePanel.lockMonete1 = lockQuest;
    }
    public static boolean getLock2() {return lock2;}
    public static boolean getLock3() {return lock3;}
    private static boolean lock2=true;
    private static boolean lock3=true;
    private static boolean lockMonete1=true;
    private static boolean lockMonete2=true;
    private static boolean lockMonete3=true;
    private static boolean LockKill1=true;
    private static boolean LockKill2=true;
    private static boolean LockKill3=true;
    public static void setLockMonete1(boolean lockMonete1) {GamePanel.lockMonete1 = lockMonete1;}
    public static void setLockMonete2(boolean lockMonete2) { GamePanel.lockMonete2 = lockMonete2;}
    public static void setLockMonete3(boolean lockMonete3) {GamePanel.lockMonete3 = lockMonete3;}
    public static void setLockKill1(boolean lockKill1) {LockKill1 = lockKill1;}
    public static void setLockKill2(boolean lockKill2) {LockKill2 = lockKill2;}
    public static void setLockKill3(boolean lockKill3) {LockKill3 = lockKill3;}
    private MondoGioco mondoGioco;
    public MondoGioco getWorld() {return mondoGioco;}
    private static Sound soundtrack;
    private static String personaggio="Fire";

    public static String getPersonaggio() {
        return personaggio;
    }

    public void setController(Controller controllerPlayer) {this.addMouseListener(controllerPlayer);this.addKeyListener(controllerPlayer);}

    public GamePanel(ImmaginiGioco immaginigioco){
        this.immaginiGioco = immaginigioco;
        Map = new HashMap<>();
        StatoGiocoMap();
        Stile1 =loadFont("Stile1");
        Corsivo =loadFont("Corsivo");
        Stile2 =loadFont("Stile2");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Stile1);
        ge.registerFont(Corsivo);
        ge.registerFont(Stile2);
        inizializzaSchermi();
    }

    private void inizializzaSchermi(){
        Menu =new MenuPrincipale();
        Vittoria =new Vittoria();
        ScegliSkin =new ScegliSkin();
        Pausa =new Pausa();
        GameOver =new GameOver();
        Aiuto =new Aiuto();
        About = new Sviluppatore();

    }

    private void StatoGiocoMap() {
        Map.put("Gioca", Stato.MAPPE);
        Map.put("Indietro", Stato.MENU);
        Map.put("Scegli skin", Stato.CAMBIA_SKIN);
        Map.put("Aiuto", Stato.HELP);
        Map.put(ImmaginiGioco.getRiconoscimenti(), Stato.INFO);
        Map.put("Menu Principale", Stato.MENU);
        Map.put("Riprendi",Stato.IN_GIOCO);
        Map.put("Livello Successivo", Stato.IN_GIOCO);
        Map.put(ImmaginiGioco.getSkinFuoco(), Stato.FUOCO);
        Map.put(ImmaginiGioco.getSkinAcqua(), Stato.ACQUA);
        Map.put("Ricomincia", Stato.IN_GIOCO);
        Map.put("Reset  Progressi",Stato.MENU);
        Map.put(ImmaginiGioco.getMap1(false), Stato.IN_GIOCO);
        Map.put(ImmaginiGioco.getMap2(false), Stato.IN_GIOCO);
        Map.put(ImmaginiGioco.getMap3(false), Stato.IN_GIOCO);
        Map.put(ImmaginiGioco.getMap2(true), Stato.MAPPE);
        Map.put(ImmaginiGioco.getMap3(true), Stato.MAPPE);
        Map.put(ImmaginiGioco.getFreccia(0), Stato.HELP);
        Map.put(ImmaginiGioco.getFreccia(1), Stato.HELP);
    }

    private Font loadFont(String t){
        try {
            InputStream is = getClass().getResourceAsStream("/resources/font/Font-"+t+".ttf");
            switch (t) {
                case "Stile1" -> {
                    assert is != null;
                    return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
                }
                case "Stile2" -> {
                    assert is != null;
                    return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(14f);
                }
            }
            assert is != null;
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return switch (t) {
                case "Stile1" -> new Font("Verdana", Font.PLAIN, 18);
                case "Stile2"-> new Font("Arial", Font.PLAIN, 14);
                default -> new Font("Verdana", Font.PLAIN, 12);
            };
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Stato stato = Game.getStati();


        if(stato == Stato.MENU){
            drawMenuPrincipale(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("Menu.wav");
                for(int i = 0; i<5 ; i++) soundtrack.reduceVolume();
                soundtrack.loop();
            }
        }
        else if(stato == Stato.MAPPE){
            drawSelezionaMappa(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("Menu.wav");
                soundtrack.loop();
            }
        }
        else if(stato == Stato.INFO){
            drawAbout(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("Menu.wav");
                for(int i = 0; i<30 ; i++) soundtrack.incrementVolume();
                soundtrack.loop();
            }
        }

        else if(stato == Stato.CAMBIA_SKIN || stato == Stato.FUOCO || stato == Stato.ACQUA){
            drawScegliSkin(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("Menu.wav");
                soundtrack.loop();
            }

            if(stato == Stato.FUOCO || (stato == Stato.CAMBIA_SKIN && personaggio.equals("Fire"))) {
                personaggio="Fire";
                createImageHover(g2d, ScegliSkin.getScreenFunctions()[1].getLocation(), ScegliSkin.getScreenFunctions()[1].getDimension());
            }
            else if(stato == Stato.ACQUA ||  personaggio.equals("Water")) {
                personaggio="Water";
                createImageHover(g2d, ScegliSkin.getScreenFunctions()[2].getLocation(), ScegliSkin.getScreenFunctions()[2].getDimension());
            }
            if(personaggio.equals("Fire")){
                g2d.drawString("Skin Fuoco Selezionata",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) "Skin Fuoco Selezionata" ,GamePanel.Stile2))/2-70,130);
                g2d.drawImage(ImmaginiGioco.getFire_skin(),(ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getFire_skin().getWidth(null))/2,340, this);
                try {
                    immaginiGioco.setPersonaggio(personaggio);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                g2d.drawImage(ImmaginiGioco.getWater_skin(),(ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getWater_skin().getWidth(null))/2,340, this);
                g2d.drawString("Skin Acqua Selezionata", (ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) "Skin Acqua Selezionata" ,GamePanel.Stile2))/2-70,130);
                if (!personaggio.equals("Water")) personaggio = "Water";

                try {
                    immaginiGioco.setPersonaggio(personaggio);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(stato == Stato.HELP){
            drawHelp(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("Menu.wav");
                soundtrack.loop();
            }
        }

        else{
            drawMap(g2d);
            if(stato == Stato.IN_GIOCO && soundtrack==null) {
                if(mondoGioco.getLiv()==1) {
                    soundtrack = new Sound("Livello1.wav");
                    for(int i = 0; i<8 ; i++) soundtrack.reduceVolume();
                    soundtrack.loop();
                } else if(mondoGioco.getLiv()==2){
                    soundtrack = new Sound("Livello2.wav");
                    for(int i = 0; i<5 ; i++) soundtrack.reduceVolume();
                    soundtrack.loop();
                } else if(mondoGioco.getLiv()==3){
                    soundtrack = new Sound("Livello3.wav");
                    for(int i = 0; i<5 ; i++) soundtrack.reduceVolume();
                    soundtrack.loop();
                }
            }
            if(stato == Stato.PAUSA)
            {
                drawPause(g2d);
                mondoGioco.getLevel().FermaNemici();

            }
            else if(stato == Stato.VITTORIA){
                drawVittoria(g2d);
                if(soundtrack==null) {
                    if(mondoGioco.getLiv()!=3)
                    {
                        soundtrack = new Sound("FineLivello.wav");
                        soundtrack.play();
                    }
                    else soundtrack = new Sound("MorteFinale.wav"); soundtrack.play();
                }
            }
            else if(stato == Stato.GAME_OVER){
                drawGameOver(g2d);
                if(soundtrack!=null)
                {
                    soundtrack.pause();
                    soundtrack=null;
                }
                Controller.getPremuti().clear();
            }
        }
        g2d.dispose();
    }

    private void drawVittoria(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
        Font win = Stile2.deriveFont(50.0f);
        g2d.setFont(win);
        if(mondoGioco.getLiv()!=3) {
            if (mondoGioco.getLiv()==1) {
                g2d.setColor(Color.BLACK);
            }
            else g2d.setColor(Color.ORANGE);
            g2d.drawString("Level "+ mondoGioco.getLiv()+" completed",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String)"Level "+ mondoGioco.getLiv()+" completed",win))/2,300);
            g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-20,400,250,80,this);
            g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-35,500,280,80,this);
            for (Function item: Vittoria.getScreenFunctions()) {
                drawButtons(item, g2d, Stile2.deriveFont(20.0f));
            }
        }

        else
        {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("Congratulazioni",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String)"Congratulazioni",win))/2,200);
            g2d.drawString("Hai completato il gioco",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String)"Hai completato il gioco",win))/2,260);
            g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-20,400,250,80,this);
            drawButtons(Vittoria.getScreenFunctions()[0], g2d, Stile2.deriveFont(20.0f));
        }
    }
    private void drawPause(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-40,350,280,80,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-40,500,280,80,this);
        for(Function item : Pausa.getScreenFunctions()) {
            drawButtons(item,g2d, Stile2.deriveFont(20.0f));
        }
    }


    private void drawCompleted(Graphics2D g2d) {
        if (mondoGioco.getPersonaggio().getMoneta() >= 35) {
            if (mondoGioco.getLiv() == 1) {g2d.drawImage(immaginiGioco.getTrofeo(), 150, 8,  this);lockMonete1 = false;}
            else if (mondoGioco.getLiv()==2) {g2d.drawImage(immaginiGioco.getTrofeo(), 150, 8,  this);lockMonete2 = false;}
            else if(mondoGioco.getLiv()==3){ g2d.drawImage(immaginiGioco.getTrofeo(), 150, 8,  this);lockMonete3 = false;}
        }
        if (mondoGioco.getPersonaggio().getKill()>=7){
            g2d.drawImage(immaginiGioco.getTrofeoMob(mondoGioco.getLiv()),210,0,60, 60, this);
            if (mondoGioco.getLiv()==1){LockKill1=false;}
            else if(mondoGioco.getLiv()==2){LockKill2=false;}
            else if(mondoGioco.getLiv()==3){LockKill3=false;}
        }
    }

    private void drawScegliSkin(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getSkinScreen(),0,0,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-270,5,200,100,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-23,175,250,150,this);

        for(Function item : ScegliSkin.getScreenFunctions()) {
            drawButtons(item,g2d, Stile2.deriveFont(25.0f));
        }
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getGameOverSchermata(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getGameOverSchermata().getWidth(null))/2-98,60,300,300,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-20,400,250,80,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-20,500,250,80,this);
        g2d.drawImage(immaginiGioco.getStart(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getStart().getWidth(null))/2-200,810,this);
        for (Function item: GameOver.getScreenFunctions()) {
            drawButtons(item, g2d, Stile2.deriveFont(20.0f));
        }
    }


    private void drawAbout(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getHelpBackground(),0,0,this);
        g2d.drawImage(immaginiGioco.getTitolo(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getTitolo().getWidth(null))/2,100,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-270,5,200,100,this);
        g2d.setColor(Color.WHITE);
        Font dev = Corsivo.deriveFont(70.0f);
        setFont(dev);
        g2d.drawString("< Sviluppato Da >", (ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "< Sviluppato Da >",dev))/2, 300);
        g2d.drawString("> Mirko Sonotaca <", (ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "> Mirko Sonotaca <",dev))/2, 500);

        drawButtons(About.getScreenFunctions()[0],g2d, Stile2.deriveFont(25.0f));
    }



    private void drawHelp(Graphics2D g2d) {


        g2d.setColor(Color.BLACK);
        g2d.drawImage(immaginiGioco.getHelpBackground(),0,0,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-270,5,200,100,this);
        Font help = Corsivo.deriveFont(60.0f);
        g2d.setFont(help);
        g2d.drawString("Abilita",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Abilita",help))/2-200,200);
        g2d.drawString("Nemici",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Nemici",help))/2,200);
        g2d.drawString("Tasti",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Tasti",help))/2+220,200);
        g2d.drawImage(immaginiGioco.getVita(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getVita().getWidth(null))/2-300,250,this);
        g2d.drawImage(immaginiGioco.getMela(1),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getMela(1).getWidth(null))/2-300,290,this);
        g2d.drawImage(immaginiGioco.getMela(2),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getMela(2).getWidth(null))/2-300,330,this);
        g2d.drawImage(immaginiGioco.getMela(3),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getMela(3).getWidth(null))/2-300,370,this);
        g2d.drawImage(ImmaginiGioco.getSpento(true),(ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getSpento(true).getWidth(null))/2-300,410,this);
        g2d.drawImage(immaginiGioco.getSlime(1,1), (ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getSlime(1,1).getWidth(null))/2-50, 250, this);
        g2d.drawImage(immaginiGioco.getSlime(1,2), (ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getSlime(1,2).getWidth(null))/2-50, 300, this);
        g2d.drawImage(immaginiGioco.getSlime(1,3), (ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getSlime(1,3).getWidth(null))/2-50, 350, this);
        g2d.drawImage(immaginiGioco.getBall(1),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBall(1).getWidth(null))/2-300,450,this);
        help = Stile1.deriveFont(20.0f);
        g2d.setFont(help);
        g2d.drawString("Vita",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Vita",help))/2-247,270);
        g2d.drawString("Frutta Livello 1",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Frutta Livello 1",help))/2-200,310);
        g2d.drawString("Frutta Livello 2",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Frutta Livello 2",help))/2-200,350);
        g2d.drawString("Frutta Livello 3",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Frutta Livello 3",help))/2-200,390);
        g2d.drawString("Passaggio Segreto",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Passaggio Segreto",help))/2-187,430);
        g2d.drawString("Stella",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Stella",help))/2-241,470);
        help = Corsivo.deriveFont(40.0f);
        g2d.setFont(help);
        g2d.drawString("N",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "N",help))/2+200,280);
        g2d.drawString("Shift",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "SHIFT",help))/2+200,330);


        help = Stile1.deriveFont(20.0f);
        g2d.setFont(help);
        g2d.drawString("Apri Passaggi",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Apri Passaggi",help))/2+290,280);
        g2d.drawString("Spara",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Spara",help))/2+280,330);
        g2d.drawString("Slime Livello 1",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Slime Livello 1",help))/2+50,270);
        g2d.drawString("Slime Livello 2",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Slime Livello 2",help))/2+50,320);
        g2d.drawString("Slime Livello 3",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Slime Livello 3",help))/2+50,370);
        g2d.drawImage(ImmaginiGioco.getLavagna(),(ImpostazioniDim.Lunghezza_Finestra- ImmaginiGioco.getLavagna().getWidth(null))/2+100,430,this);
        g2d.setColor(Color.WHITE);
        Font co = Stile2.deriveFont(12.0f);
        g2d.setFont(co);
        if (getCount()==0) {

            g2d.drawImage(immaginiGioco.getVita(),(ImpostazioniDim.Lunghezza_Finestra- immaginiGioco.getVita().getWidth(null))/2-10,475,this);
            drawString(g2d,"Ottenuta questa Abilita ti aggiunge una \nvita al personaggio con un numero \nmassimo di vite pari a 4", 345, 520);

        }
        if (getCount()==1) {
            g2d.drawImage(immaginiGioco.getMela(1),(ImpostazioniDim.Lunghezza_Finestra- immaginiGioco.getMela(1).getWidth(null))/2-10,475,this);
            g2d.drawImage(immaginiGioco.getMela(2),(ImpostazioniDim.Lunghezza_Finestra- immaginiGioco.getMela(2).getWidth(null))/2+30,475,this);
            g2d.drawImage(immaginiGioco.getMela(3),(ImpostazioniDim.Lunghezza_Finestra- immaginiGioco.getMela(3).getWidth(null))/2+70,475,this);
            g2d.drawImage(immaginiGioco.getTrofeo(),(ImpostazioniDim.Lunghezza_Finestra- immaginiGioco.getTrofeo().getWidth(null))/2-10,635,this);
            drawString(g2d,"La frutta si puo ottenere nei vari\nlivelli e possono cambiare in base \nal livello.\nInoltre e' possibile ottenere il Trofeo\nCollectFruits accumulando 35 \npezzi di frutta.", 345, 520);
            drawString(g2d,"<<<<< CollectFruits", (ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "<<<<< CollectFruits",co))/2+100, 673);
        }


        if (getCount()==2) {
            g2d.drawImage(ImmaginiGioco.getSpento(true),(ImpostazioniDim.Lunghezza_Finestra- ImmaginiGioco.getSpento(true).getWidth(null))/2-10,475,this);
            g2d.drawImage(ImmaginiGioco.getSpento(false),(ImpostazioniDim.Lunghezza_Finestra- ImmaginiGioco.getSpento(false).getWidth(null))/2+40,475,this);
            drawString(g2d,"E' possibile trovare questo Slime \nSpeciale nella mappa,avvicinandosi e \ncliccando il tasto N si puo sbloccare un \npassaggio segreto.", 345, 520);

        }

        if (getCount()==3) {
            g2d.drawImage(immaginiGioco.getBall(1),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBall(1).getWidth(null))/2-10,472,30,30,this);
            drawString(g2d,"E' possibile uccidere gli Slime sparando\nuna stella tagliente,utilizzando il tasto\nSHIFT.", 345, 520);
        }
        if (getCount()==4) {

            g2d.drawImage(immaginiGioco.getSlime(1,1),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getSlime(1,1).getWidth(null))/2-10,475,this);
            g2d.drawImage(immaginiGioco.getSlime(1,2),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getSlime(1,1).getWidth(null))/2+30,475,this);
            g2d.drawImage(immaginiGioco.getSlime(1,3),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getSlime(1,1).getWidth(null))/2+70,475,this);
            g2d.drawImage(immaginiGioco.getTrofeoMob(1),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getTrofeoMob(1).getWidth(null))/2,620,this);
            g2d.drawImage(immaginiGioco.getTrofeoMob(2),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getTrofeoMob(2).getWidth(null))/2+70,620,this);
            g2d.drawImage(immaginiGioco.getTrofeoMob(3),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getTrofeoMob(3).getWidth(null))/2+140,627,this);
            drawString(g2d,"I nemici sono degli slime viscidi ti \ndaranno filo da torcere. \nE' possibile ucciderli\nsparandoli oppure saltando sotto\ndi essi.E' possibile ottenere i vari\ntrofei nei vari livelli uccidendo almeno\n7 nemici.", 345, 520);

        }

        for(Function item : Aiuto.getScreenFunctions()) {
            drawButtons(item,g2d, Stile2.deriveFont(25.0f));
        }
    }

    private void drawString(Graphics2D g2d, String text, int x, int y) {
        String[] lines = text.split("\n");
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        for (String line : lines) {
            g2d.drawString(line, x, y);
            y += lineHeight;
        }
    }

    private void drawSelezionaMappa(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawImage(immaginiGioco.getSkinScreen(),0,0,this);
        Font map = Corsivo.deriveFont(25.0f);
        g2d.setFont(map);
        map = Corsivo.deriveFont(60.0f);
        g2d.setFont(map);
        g2d.drawString("Seleziona Mappa",(ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) "Seleziona Mappa",map))/2,60);
        map = Stile1.deriveFont(45.0f);
        g2d.setFont(map);
        g2d.drawString("Livello 1",50,200);
        g2d.drawString("Livello 2",50,450);
        g2d.drawString("Livello 3",50,700);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2-270,5,200,100,this);

        for(Function item : SelezionaMappa.getScreenFunctions()) {
            drawButtons(item,g2d, Stile2.deriveFont(25.0f));
        }
        drawTime1(g2d,Game.getTempo());
        drawTime2(g2d,Game.getTempo2());
        drawTime3(g2d,Game.getTempo3());
        if(!lockMonete1 && !lock2) g2d.drawImage(immaginiGioco.getTrofeo(),460,100,this);
        if(!lockMonete2 && !lock3) g2d.drawImage(immaginiGioco.getTrofeo(),460,350,this);
        if(!lockMonete3) g2d.drawImage(immaginiGioco.getTrofeo(),460,600,this);
        if(!LockKill1 && !lock2) g2d.drawImage(immaginiGioco.getTeschio1(),462,160,this);
        if(!LockKill2 && !lock3) g2d.drawImage(immaginiGioco.getTeschio2(),462,420,this);
        if(!LockKill3 && !Game.getMondo().getLock4()) g2d.drawImage(immaginiGioco.getTeschio3(),472,663,this);

    }


    private void drawMenuPrincipale(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getMenuPrincipaleScreen(),0,0,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2,350,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2,400,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2,450,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2,500,this);
        g2d.drawImage(immaginiGioco.getBottoneMain(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getBottoneMain().getWidth(null))/2,550,this);
        g2d.drawImage(immaginiGioco.getTitolo(),(ImpostazioniDim.Lunghezza_Finestra-immaginiGioco.getTitolo().getWidth(null))/2,250,this);
        for(Function item : Menu.getScreenFunctions()) {
            drawButtons(item,g2d, Stile2);
        }
    }

    private void drawMap(Graphics2D g2d){
        mondoGioco = Game.getMondo().getWorld();
        g2d.drawImage(immaginiGioco.getBackgroundImage(mondoGioco.getLiv()),0,0,this);
        for(int i = 0; i < ImpostazioniDim.Righe; i++) {
            int y = i * ImpostazioniDim.Altezza_Cella;
            for(int j = 0; j < ImpostazioniDim.Colonne; j++) {
                int x = j * ImpostazioniDim.Lunghezza_Cella;
                if (mondoGioco.isPersonaggio(i, j )) {
                    if (!new Posizione(i, j ).equals(Personaggio.getCoordinatePersonaggio())) {mondoGioco.setMatrice(i, j, Block.NULLO);}
                }
                if (mondoGioco.isCoin(i, j)){g2d.drawImage(immaginiGioco.getMela(mondoGioco.getLiv()), x, y, this);}
                else if (mondoGioco.isVita(i, j )) {g2d.drawImage(immaginiGioco.getVita(), x, y, this);}
                else if (mondoGioco.isUsato(i, j )) g2d.drawImage(immaginiGioco.getBloccoUsato(), x, y, this);
                else if (mondoGioco.isWall(i, j )) g2d.drawImage(immaginiGioco.getBloccoMuro(mondoGioco.getLiv()), x, y, this);
                else if (mondoGioco.isMystery(i, j )) g2d.drawImage(immaginiGioco.getBloccoSpeciale(mondoGioco.getLiv()), x, y, this);
                else if (mondoGioco.isMorte(i, j )) g2d.drawImage(immaginiGioco.getMorte(mondoGioco.getLiv()), x, y, this);
                else if (mondoGioco.isPulsante(i, j )) g2d.drawImage(ImmaginiGioco.getSpento(mondoGioco.isRis()), x, y, this);
                else if (mondoGioco.isTrofeo(i, j)) g2d.drawImage(ImmaginiGioco.getFine(),x,y-25,this);
                else if (mondoGioco.isPersonaggio(i, j )) {
                    if (!drawedPlayer && Personaggio.getCoordinatePersonaggio().equals(new Posizione(i, j ))) {
                        if (Controller.getPremuti().contains(ImpostazioniDim.MUOVI_DESTRA)) g2d.drawImage(immaginiGioco.getPersonaggio(ImpostazioniDim.MUOVI_DESTRA), x, y - ImpostazioniDim.Altezza_Cella, this);
                        else if (Controller.getPremuti().contains(ImpostazioniDim.MUOVI_SINISTRA)) g2d.drawImage(immaginiGioco.getPersonaggio(ImpostazioniDim.MUOVI_SINISTRA), x, y - ImpostazioniDim.Altezza_Cella, this);
                        else g2d.drawImage(immaginiGioco.getPersonaggio(mondoGioco.getPersonaggio().getPreDirezione()), x, y - ImpostazioniDim.Altezza_Cella, this);
                        drawedPlayer = true;
                    }
                    if(mondoGioco.isPulsante(Personaggio.getCoordinatePersonaggio().i(),Personaggio.getCoordinatePersonaggio().j()+1) || mondoGioco.isPulsante(Personaggio.getCoordinatePersonaggio().i(),Personaggio.getCoordinatePersonaggio().j()-1)){
                        g2d.setColor(Color.WHITE);
                        Font premi = Stile1.deriveFont(30.0f);
                        g2d.setFont(premi);
                        g2d.drawString("Premi N per Sbloccare il passaggio",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) "Premi N per Sbloccare il passaggio",premi))/2,80);
                    }
                }


                if(mondoGioco.isNemico(i, j)){
                    for(Nemici o: mondoGioco.getNemici()){
                        if (o instanceof Mob) {
                            if (o.getCoordinate().i() == i && o.getCoordinate().j() == j) {g2d.drawImage(immaginiGioco.getSlime(o.getDirezione(), mondoGioco.getLiv()), x, y, this);break;}
                    }
                    }
                }
                if(mondoGioco.isBalls(i, j)) {
                    for (Ball t : mondoGioco.getPersonaggio().getBalls()) {
                        if (Objects.equals(t.getPos(), new Posizione(i, j))) {g2d.drawImage(immaginiGioco.getBall(t.getDir()), x, y, this);break;}
                    }
                }
                    }
                }
        drawedPlayer=false;
        drawTime(g2d,mondoGioco.getLevel().getTime().getTimer());
        drawCompleted(g2d);
        drawkill(g2d, mondoGioco.getPersonaggio().getKill());
        drawMonete(g2d, mondoGioco.getPersonaggio().getMoneta());
        drawVite(g2d, Personaggio.getLives());
        drawlivello(g2d, mondoGioco.getLiv());
    }
    private void drawlivello(Graphics2D g2d, int liv) {
        Stile1 = Stile1.deriveFont(25.0f);
        g2d.setColor(Color.WHITE);
        g2d.setFont(Stile1);
        g2d.drawString("Level "+liv, 25, 40);
        g2d.drawImage(immaginiGioco.getStart(),20,675,this);
    }
    private void drawVite(Graphics2D g2d, int lives) {
        for(int i=1;i<=lives;i++){
            g2d.drawImage(immaginiGioco.getVita(),i*35-10,50, this);
        }
    }
    private void drawkill(Graphics2D g2d, int kill) {
        Stile1 = Stile1.deriveFont(25.0f);
        g2d.setColor(Color.WHITE);
        g2d.setFont(Stile1);
        g2d.drawString(kill+"", 670, 80);
        g2d.drawImage(immaginiGioco.getKill(),683,43,60,50,this);
    }
    private void drawMonete(Graphics2D g2d, int coins) {
        Stile1 = Stile1.deriveFont(25.0f);
        g2d.setColor(Color.WHITE);
        g2d.setFont(Stile1);
        g2d.drawString(coins+"", 670, 37);
        g2d.drawImage(immaginiGioco.getMela(mondoGioco.getLiv()),690,0, 50, 50,this);
    }
    private void drawTime(Graphics2D g2d,int time){
        g2d.setFont(Stile1.deriveFont(35.0f));
        g2d.setColor(Color.WHITE);
        ms = time % 100;
        sec=(time/100)%60;
        min=(time/6000)%100;
        minPlus="";
        secPlus="";
        msPlus="";
        if(min<10) minPlus="0";
        if(sec<10) secPlus="0";
        if(ms<10) msPlus="0";
        g2d.drawString(minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",280,40);
    }
    private void drawTime1(Graphics2D g2d,int time){
        g2d.setFont(Stile1.deriveFont(35.0f));
        g2d.setColor(Color.WHITE);
        ms = time % 100;
        sec=(time/100)%60;
        min=(time/6000)%100;
        minPlus="";
        secPlus="";
        msPlus="";
        if(min<10) minPlus="0";
        if(sec<10) secPlus="0";
        if(ms<10) msPlus="0";
        g2d.drawString(minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",Stile1))/2+200,220);
    }
    private void drawTime2(Graphics2D g2d,int time){
        g2d.setFont(Stile1.deriveFont(35.0f));
        g2d.setColor(Color.WHITE);
        ms = time % 100;
        sec=(time/100)%60;
        min=(time/6000)%100;
        minPlus="";
        secPlus="";
        msPlus="";
        if(min<10) minPlus="0";
        if(sec<10) secPlus="0";
        if(ms<10) msPlus="0";
        g2d.drawString(minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",Stile1))/2+200,450);
    }
    private void drawTime3(Graphics2D g2d,int time){
        g2d.setFont(Stile1.deriveFont(35.0f));
        g2d.setColor(Color.WHITE);
        ms = time % 100;
        sec=(time/100)%60;
        min=(time/6000)%100;
        minPlus="";
        secPlus="";
        msPlus="";
        if(min<10) minPlus="0";
        if(sec<10) secPlus="0";
        if(ms<10) msPlus="0";
        g2d.drawString(minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",(ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) minPlus+min+":"+secPlus+sec+"."+msPlus+ms+"0",Stile1))/2+200,695);
    }




    public void LivelloSuperato() {this.repaint();}
    private Object selezionato;
    Sound click = new Sound("Click.wav");

    public void select() {
        if (Game.getStati().equals(Stato.MENU)){
            selezionato = Menu.select(getMousePosition());
        }
        else if (Game.getStati().equals(Stato.VITTORIA)) {
            selezionato = Vittoria.select(getMousePosition(), mondoGioco.getLiv());

            if (soundtrack != null && selezionato !=null) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getStati().equals(Stato.CAMBIA_SKIN) || Game.getStati().equals(Stato.FUOCO)||  Game.getStati().equals(Stato.ACQUA)) {
            selezionato = ScegliSkin.select(getMousePosition());
        }
        else if (Game.getStati().equals(Stato.PAUSA)){
            selezionato = Pausa.select(getMousePosition());
            if (soundtrack != null && selezionato !=null && Map.get(selezionato)!= Stato.IN_GIOCO ) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getStati().equals(Stato.GAME_OVER)){
            selezionato = GameOver.select(getMousePosition());
            if (mondoGioco.getPersonaggio().getMorte() != null && selezionato !=null) {
                mondoGioco.getPersonaggio().getMorte().pause();
            }

        }
        else if (Game.getStati().equals(Stato.MAPPE)){
            selezionato = SelezionaMappa.select(getMousePosition());
            if (soundtrack != null && selezionato !=null && Map.get(selezionato)!= Stato.MAPPE && Map.get(selezionato)!= Stato.MENU) {
                soundtrack.pause();
                soundtrack = null;
            }
        }

        else if (Game.getStati().equals(Stato.HELP)){
            selezionato = Aiuto.select(getMousePosition());
        }

        else if (Game.getStati().equals(Stato.INFO)){
            selezionato = About.select(getMousePosition());

        }


        if (selezionato != null) {
            click.play();
            if(selezionato =="Esci"){System.exit(1);}
            if (selezionato==ImmaginiGioco.getFreccia(1)) { count++;
                if (count==5){ count=0;}
            }
            if (selezionato==ImmaginiGioco.getFreccia(0)) { count--;
                if (count==-1){ count=4;}
            }
            else if (selezionato =="Gioca"){SelezionaMappa =new SelezionaMappa();}
            else if(selezionato =="Ricomincia"){Game.getMondo().setWorld(new MondoGioco(1, 3));}
            else if(selezionato =="Riprendi"){mondoGioco.getLevel().RiavviaNemici();}
            else if(selezionato =="Reset  Progressi"){
                setLock2(true);
                setLock3(true);
                setLockQuest(true);
                setLockKill1(true);
                setLockKill2(true);
                setLockKill3(true);
                setLockMonete1(true);
                setLockMonete2(true);
                setLockMonete3(true);
                Game.setTempo1(0);
                Game.setTempo2(0);
                Game.setTempo3(0);
            }
            if(selezionato ==ImmaginiGioco.getMap1(false)){Game.getMondo().setWorld(new MondoGioco(1,3));}
            else if(selezionato ==ImmaginiGioco.getMap2(false)){Game.getMondo().setWorld(new MondoGioco(2,3));}
            else if(selezionato ==ImmaginiGioco.getMap3(false)){Game.getMondo().setWorld(new MondoGioco(3,3));}
            Game.setGameStatus(Map.get(selezionato));
            selezionato =null;
        }
    }
    private void drawButtons(Function item, Graphics2D g2d, Font font) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        int width,height;
        if(item.getObject() instanceof String) {
            width = g2d.getFontMetrics().stringWidth((String) item.getObject());
            height = g2d.getFontMetrics().getHeight();
        }
        else{
            width = ((Image) item.getObject()).getWidth(this);
            height = ((Image) item.getObject()).getHeight(this);
        }
        item.setDimension(new Dimension(width, height));
        item.setLocation(new Point(item.getLocation().x, item.getLocation().y));

        if(item.getObject() instanceof String)
            g2d.drawString((String) item.getObject(), item.getLocation().x, item.getLocation().y);
        else g2d.drawImage((Image) item.getObject(), item.getLocation().x, item.getLocation().y,this);
        clickButton(item,g2d);
    }

    private void clickButton(Function item,Graphics2D g2d) {
        Dimension dimension = item.getDimension();
        Point location = item.getLocation();
        Point mousePosition=getMousePosition();
        if(mousePosition!=null) {
            boolean inX = location.x <= mousePosition.x && location.x + dimension.width >= mousePosition.x;
            boolean inY;
            if (item.getObject() instanceof String)
                inY = location.y >= mousePosition.y && location.y - dimension.height <= mousePosition.y;
            else inY = location.y <= mousePosition.y && location.y + dimension.height >= mousePosition.y;

            if (inX && inY) {
                if (item.getObject() instanceof Image) {
                    createImageHover(g2d, location, dimension);
                } else {
                    createTextHover(g2d, location, dimension);
                }
            }
        }
    }

    public int getCount() {
        return count;
    }
    public static int getStringWidth(String text, Font font) {
        JLabel label = new JLabel();
        FontMetrics fontMetrics = label.getFontMetrics(font);
        return fontMetrics.stringWidth(text);
    }

    private void createTextHover(Graphics2D g2d, Point l, Dimension d){g2d.drawRect(l.x,l.y-d.height,d.width,d.height);}
    private void createImageHover(Graphics2D g2d,Point l,Dimension d){g2d.drawRect(l.x,l.y,d.width,d.height);}
}



