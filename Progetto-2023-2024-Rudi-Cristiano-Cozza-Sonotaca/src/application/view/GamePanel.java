package application.view;

import application.Block;
import application.GameStatus;
import application.audio.Sound;
import application.controller.Controller;
import application.model.*;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GamePanel extends JPanel {

    private final ImmaginiGioco immaginiGioco;
    private Font Alt;
    private final Font Reg;
    private final Font Start;

    private final Font Playground;

    public void setController(Controller controllerPlayer) {
        this.addMouseListener(controllerPlayer);
        this.addKeyListener(controllerPlayer);
    }
    private MainScreen startScreen;
    private WinScreen winScreen;
    private SkinScreen skinScreen;
    private PauseScreen pauseScreen;

    private HelpScreen helpScreen;
    private ComScreen comScreen;

    private MapScreen mapScreen;
    private LoseScreen loseScreen;

    private AboutScreen aboutScreen;
    private CopyrightScreen copyrightScreen;
    private final Map<Object, GameStatus> contenutoMap;
    public GamePanel(ImmaginiGioco immaginigioco, LevelProgress progress){
        this.immaginiGioco = immaginigioco;
        this.progress=progress;
        contenutoMap = new HashMap<>();
        addInMap();
        inizializzaSchermi();
        Alt=loadFont("Alt");
        Reg=loadFont("Reg");
        Start=loadFont("Start");
        Playground=loadFont("Playground");

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Alt);
        ge.registerFont(Reg);
        ge.registerFont(Start);
        ge.registerFont(Playground);
    }

    private void inizializzaSchermi(){
        startScreen=new MainScreen();
        winScreen=new WinScreen();
        skinScreen=new SkinScreen();
        pauseScreen=new PauseScreen();
        loseScreen=new LoseScreen();
        comScreen=new ComScreen();
        helpScreen=new HelpScreen();
        aboutScreen = new AboutScreen();
        copyrightScreen = new CopyrightScreen();
    }

    private void addInMap() {
        contenutoMap.put("Gioca",GameStatus.MAP_SELECTION);
        contenutoMap.put("Indietro",GameStatus.START_SCREEN);
        contenutoMap.put("Scegli skin", GameStatus.CHOOSE_SKIN);
        contenutoMap.put("Aiuto", GameStatus.HELP);
        contenutoMap.put("Comandi", GameStatus.COMMANDS_SCREEN);
        contenutoMap.put(ImmaginiGioco.getCopyright(), GameStatus.COPYRIGHT);
        contenutoMap.put(ImmaginiGioco.getRiconoscimenti(), GameStatus.ABOUT_SCREEN);
        contenutoMap.put("Menu Principale", GameStatus.START_SCREEN);
        contenutoMap.put("Livello Successivo", GameStatus.IN_GAME);
        contenutoMap.put(ImmaginiGioco.getSteve(), GameStatus.STEVE);
        contenutoMap.put(ImmaginiGioco.getAlex(), GameStatus.ALEX);
        contenutoMap.put("Ricomincia", GameStatus.IN_GAME);
        contenutoMap.put(ImmaginiGioco.getMap1(false),GameStatus.IN_GAME);
        contenutoMap.put(ImmaginiGioco.getMap2(false),GameStatus.IN_GAME);
        contenutoMap.put(ImmaginiGioco.getMap3(false),GameStatus.IN_GAME);
        contenutoMap.put(ImmaginiGioco.getMap2(true),GameStatus.MAP_SELECTION);
        contenutoMap.put(ImmaginiGioco.getMap3(true),GameStatus.MAP_SELECTION);
    }

    private Font loadFont(String t){
        try {
            InputStream is = getClass().getResourceAsStream("/resources/font/Minecrafter-"+t+".ttf");
            switch (t) {
                case "Alt" -> {
                    assert is != null;
                    return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f);
                }
                case "Start", "Playground" -> {
                    assert is != null;
                    return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(14f);
                }
            }
            assert is != null;
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return switch (t) {
                case "Alt" -> new Font("Verdana", Font.PLAIN, 18);
                case "Start", "Playground" -> new Font("Arial", Font.PLAIN, 14);
                default -> new Font("Verdana", Font.PLAIN, 12);
            };
        }
    }


    private World world;

    public World getWorld() {
        return world;
    }

    private final LevelProgress progress;

    private static Sound soundtrack;
    private String personaggio="steve";

    public static Sound getSoundtrack() {
        return soundtrack;
    }

    public static void setSoundtrack(Sound soundtrack) {
        GamePanel.soundtrack = soundtrack;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GameStatus gameStatus= Game.getGameStatus();

        if(gameStatus == GameStatus.START_SCREEN){
            drawStartScreen(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("pigstep.wav");
                for(int i = 0; i<5 ; i++) soundtrack.reduceVolume();
                soundtrack.loop();
            }
        }
        else if(gameStatus == GameStatus.MAP_SELECTION){
            drawMapSelection(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("stal.wav");
                soundtrack.loop();
            }
        }
        else if(gameStatus == GameStatus.ABOUT_SCREEN){
            drawAbout(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("menu3.wav");
                for(int i = 0; i<30 ; i++) soundtrack.incrementVolume();
                soundtrack.loop();
            }
        }
        else if(gameStatus == GameStatus.COMMANDS_SCREEN){
            drawCommands(g2d);
        }
        else if(gameStatus == GameStatus.CHOOSE_SKIN  || gameStatus == GameStatus.STEVE || gameStatus == GameStatus.ALEX){
            drawChooseSkin(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("skin.wav");
                soundtrack.loop();
            }
            if(gameStatus == GameStatus.STEVE || (gameStatus == GameStatus.CHOOSE_SKIN && personaggio.equals("steve"))) {
                personaggio="steve";
                createImageHover(g2d,skinScreen.getScreenFunctions()[1].getLocation(),skinScreen.getScreenFunctions()[1].getDimension());
            }
            else if(gameStatus == GameStatus.ALEX ||  personaggio.equals("alex")) {
                personaggio="alex";
                createImageHover(g2d,skinScreen.getScreenFunctions()[2].getLocation(),skinScreen.getScreenFunctions()[2].getDimension());
            }
            if(personaggio.equals("steve")){
                drawSkin(ImmaginiGioco.getSteve_completo(), g2d);
                try {
                    immaginiGioco.setPersonaggio(personaggio);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                drawSkin(ImmaginiGioco.getAlex_completo(),g2d);
                if(!personaggio.equals("alex")) personaggio="alex";
                try {
                    immaginiGioco.setPersonaggio(personaggio);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(gameStatus == GameStatus.HELP){
            drawHelp(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("menu2.wav");
                soundtrack.loop();
            }
        }
        else if(gameStatus == GameStatus.COPYRIGHT){
            drawCopyright(g2d);
            if(soundtrack==null) {
                soundtrack = new Sound("menu4.wav");
                for(int i = 0; i<10 ; i++) soundtrack.incrementVolume();
                soundtrack.loop();
            }
        }
        else{
            drawMap(g2d);
            if(gameStatus == GameStatus.IN_GAME && soundtrack==null) {
                if(world.getLiv()==1) {
                    soundtrack = new Sound("blocks.wav");
                    for(int i = 0; i<8 ; i++) soundtrack.reduceVolume();
                    soundtrack.loop();
                } else if(world.getLiv()==2){
                    soundtrack = new Sound("nether1.wav");
                    for(int i = 0; i<5 ; i++) soundtrack.reduceVolume();
                    soundtrack.loop();
                } else if(world.getLiv()==3){
                    soundtrack = new Sound("so_below.wav");
                    for(int i = 0; i<5 ; i++) soundtrack.reduceVolume();
                    soundtrack.loop();
                }
            }
            if(gameStatus == GameStatus.PAUSE)
            {
                drawPause(g2d);
                world.getLevel().pauseAllEnemies();

            }
            else if(gameStatus == GameStatus.WIN){
                drawWin(g2d);
                if(soundtrack==null) {

                    if(world.getLiv()!=3)
                    {
                        soundtrack = new Sound("endLevel.wav");
                        soundtrack.play();
                    }
                    else
                    {
                        soundtrack = new Sound("eDied.wav");
                        soundtrack.play();
                    }

                }
            }
            else if(gameStatus == GameStatus.GAME_OVER){
                drawGameOver(g2d);
                if(soundtrack!=null)
                {
                    soundtrack.pause();
                    soundtrack=null;
                }
                Controller.getPressed().clear();
            }
        }
        g2d.dispose();
    }

    private void drawSkin(Image i,Graphics2D g2d){
        g2d.drawImage(i,370,125,this);
    }
    private void drawWin(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        Font win = Alt.deriveFont(50.0f);
        g2d.setFont(win);




        g2d.drawImage(ImmaginiGioco.getPulsante(),100,400,this);
        if(world.getLiv()!=3) {
            g2d.drawString("Level "+world.getLiv()+" completed",170,200);
            g2d.drawImage(ImmaginiGioco.getPulsante(),560,400,this);
            for (Function item: winScreen.getScreenFunctions()) {
                drawButtons(item, g2d,Start.deriveFont(20.0f));
            }
        }

        else
        {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Congratulazioni",200,180);
            g2d.drawString("Hai completato il gioco",100,240);
            drawButtons(winScreen.getScreenFunctions()[0], g2d,Start.deriveFont(20.0f));
        }
    }

    private void drawPause(Graphics2D g2d) {
        g2d.drawImage(ImmaginiGioco.getPulsante(),(Settings.WINDOW_SIZE_X- ImmaginiGioco.getPulsante().getWidth(this))/2,400,this);
        g2d.drawImage(immaginiGioco.getPausa(),(Settings.WINDOW_SIZE_X-immaginiGioco.getPausa().getWidth(this))/2,(Settings.WINDOW_SIZE_Y-immaginiGioco.getPausa().getHeight(this))/2,this);
        for(Function item : pauseScreen.getScreenFunctions()) {
            drawButtons(item,g2d,Start.deriveFont(20.0f));
        }
    }
    private void drawChooseSkin(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getSkin_screen(),0,0,this);
        g2d.drawImage(immaginiGioco.getSkin_button(),20,20,this);
        for(Function item : skinScreen.getScreenFunctions()) {
            drawButtons(item,g2d,Start.deriveFont(25.0f));
        }
    }

    private void drawGameOver(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getLose_screen(),0,0,this);
        g2d.drawImage(immaginiGioco.getSkull(),(Settings.WINDOW_SIZE_X-183)/2,175,this);
        g2d.drawImage(ImmaginiGioco.getPulsante(),100,400,this);
        g2d.drawImage(ImmaginiGioco.getPulsante(),560,400,this);
        for (Function item: loseScreen.getScreenFunctions()) {
            drawButtons(item, g2d,Start.deriveFont(20.0f));
        }
    }

    private void drawCommands(Graphics2D g2d) {

        g2d.drawImage(immaginiGioco.getMapBG(),0,0,this);
        g2d.drawImage(immaginiGioco.getSkin_button(),20,20,this);
        g2d.setColor(Color.BLACK);
        Font com = Alt.deriveFont(35.0f);
        g2d.setFont(com);
        g2d.drawString("Scelta",(Settings.WINDOW_SIZE_X-140)/2,75);
        g2d.drawString("Configurazione Tasti",(Settings.WINDOW_SIZE_X-445)/2,130);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Scelta",(Settings.WINDOW_SIZE_X-150)/2,70);
        g2d.drawString("Configurazione Tasti",(Settings.WINDOW_SIZE_X-455)/2,125);
        com =Reg.deriveFont(30.0f);
        g2d.setFont(com);
        g2d.drawString("Muovi",(Settings.WINDOW_SIZE_X-97)/2,250);
        g2d.drawString("Salta",570,422);
        g2d.drawString("Pausa",510,490);
        g2d.drawImage(immaginiGioco.getSpazio(),(Settings.WINDOW_SIZE_X-immaginiGioco.getSpazio().getWidth(this))/2,320,this);
        g2d.drawImage(immaginiGioco.getEsc(),(Settings.WINDOW_SIZE_X-immaginiGioco.getEsc().getWidth(this))/2,450,this);
        com =Start.deriveFont(20.0f);
        g2d.setFont(com);
        if(Controller.getTipo()==0) g2d.drawString("Stai usando la configurazione Freccette",(Settings.WINDOW_SIZE_X-480)/2,550);
        else g2d.drawString("Stai usando la configurazione WASD",(Settings.WINDOW_SIZE_X-423)/2,550);
        for (Function item: comScreen.getScreenFunctions()) {
            drawButtons(item, g2d,Start.deriveFont(25.0f));
        }

    }

    private void drawAbout(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getDevBackground(),0,0,this);
        g2d.drawImage(immaginiGioco.getSkin_button(),20,20,this);
        g2d.setColor(Color.BLACK);
        Font dev = Alt.deriveFont(55.0f);
        g2d.setFont(dev);
        g2d.drawString("sviluppato da", 230, 170); //OMBRA
        dev =Alt.deriveFont(35.0f);
        g2d.setFont(dev);
        g2d.drawString("per user interface design", 178,458); //OMBRA
        g2d.setColor(Color.WHITE);
        dev =Alt.deriveFont(55.0f);
        g2d.setFont(dev);
        g2d.drawString("sviluppato da", 220, 160);
        dev =Alt.deriveFont(35.0f);
        g2d.setFont(dev);
        g2d.drawString("per user interface design", 168,450);
        dev =Playground.deriveFont(40.0f);
        g2d.setFont(dev);
        g2d.drawString("Francesco Cristiano", 240, 230);
        g2d.drawString("Giuseppe Rudi", 305, 280);
        g2d.drawString("Simone Cozza", 310, 330);
        g2d.drawString("Mirko Sonotaca", 280, 380);
        drawButtons(aboutScreen.getScreenFunctions()[0],g2d,Start.deriveFont(25.0f));
    }
    private void drawCopyright(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getCopBackground(),0,0,this); //cambiare sfondo
        g2d.drawImage(immaginiGioco.getSkin_button(),20,20,this);
        g2d.drawImage(immaginiGioco.getCreativeCommons(),(Settings.WINDOW_SIZE_X-immaginiGioco.getCreativeCommons().getWidth(this))/2,370,this);
        Font cop = Alt.deriveFont(55.0f);
        g2d.setFont(cop);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Copyright", 280, 100);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Copyright",270,90);
        cop =Alt.deriveFont(30.0f);
        g2d.setFont(cop);
        g2d.drawString("font", 180, 200);
        g2d.drawString("immagini", 180, 260);
        g2d.drawString("suoni", 180, 320);
        Font site=new Font("font", Font.BOLD,30);
        g2d.setFont(site);
        g2d.drawString("dafont", 355, 195);
        g2d.drawString("minecraft wiki - shutterstock", 355, 255);
        g2d.drawString("mojang studios - freesound", 355, 315);
        drawButtons(copyrightScreen.getScreenFunctions()[0],g2d,Start.deriveFont(25.0f));
        drawButtons(copyrightScreen.getScreenFunctions()[1],g2d,Start.deriveFont(15.0f));
        drawButtons(copyrightScreen.getScreenFunctions()[2],g2d,Start.deriveFont(15.0f));
    }
    private void drawHelp(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawImage(immaginiGioco.getHelpBG(),0,0,this);
        g2d.drawImage(immaginiGioco.getBoard(),20,20,this);
        Font help = Reg.deriveFont(30.0f);
        g2d.setFont(help);
        g2d.drawImage(immaginiGioco.getSkin_button(),20,20,this);
        g2d.drawString("Abilita",250,100);
        g2d.drawString("Nemici",550,100);
        g2d.drawImage(immaginiGioco.getVita(),230,170,this);
        g2d.drawImage(immaginiGioco.getScudoIcon(),230,270,this);
        g2d.drawImage(immaginiGioco.getVelocita(),230,370,this);
        g2d.drawImage(immaginiGioco.getLentezza(),230,470,this);

        g2d.drawImage(immaginiGioco.getCreeper(1,0),520,170,this);
        g2d.drawImage(immaginiGioco.getCreeper(1,1),580,170,this);
        g2d.drawImage(immaginiGioco.getSkeleton(1),520,320,this);
        g2d.drawImage(immaginiGioco.getFreccia(1),580,320,this);
        g2d.drawImage(immaginiGioco.getMiniZombie(1),520,470,this);
        help =Start.deriveFont(17.0f);
        g2d.setFont(help);
        g2d.drawString("Vita",300,190);
        g2d.drawString("Scudo",300,290);
        g2d.drawString("Velocita",300,390);
        g2d.drawString("Lentezza",300,490);

        g2d.drawString("Creeper",650,210);
        g2d.drawString("Scheletro",650,360);
        g2d.drawString("Mini Zombie",590,490);
        for(Function item : helpScreen.getScreenFunctions()) {
            drawButtons(item,g2d,Start.deriveFont(25.0f));
        }

    }
    private void drawMapSelection(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawImage(immaginiGioco.getMapBG(),0,0,this);
        g2d.drawImage(immaginiGioco.getSkin_button(),20,20,this);
        Font map = Reg.deriveFont(25.0f);
        g2d.setFont(map);
        g2d.drawString("Livello 1",100+(300-130)/2,325);
        g2d.drawString("Livello 2",500+(300-130)/2,325);
        g2d.drawString("Livello 3",((Settings.WINDOW_SIZE_X-ImmaginiGioco.getMap3(true).getWidth(this))/2)+(300-130)/2,573);
        map =Reg.deriveFont(50.0f);
        g2d.setFont(map);
        g2d.drawString("Mappe",(Settings.WINDOW_SIZE_X-170)/2,75);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Mappe",(Settings.WINDOW_SIZE_X-185)/2,68);
        map =Alt.deriveFont(25.0f);
        g2d.setFont(map);
        g2d.drawString("Livello 1",100+(300-140)/2,320);
        g2d.drawString("Livello 2",500+(300-140)/2,320);
        g2d.drawString("Livello 3",((Settings.WINDOW_SIZE_X-ImmaginiGioco.getMap3(true).getWidth(this))/2)+(300-140)/2,568);
        for(Function item : mapScreen.getScreenFunctions()) {
            drawButtons(item,g2d,Start.deriveFont(25.0f));
        }
        if(lock2) g2d.drawImage(immaginiGioco.getLock(),500+(300-90)/2,100+(180-90)/2,this);
        if(lock3) g2d.drawImage(immaginiGioco.getLock(),((Settings.WINDOW_SIZE_X-ImmaginiGioco.getMap3(true).getWidth(this))/2)+(300-90)/2,350+(180-90)/2,this);
    }

    private void drawStartScreen(Graphics2D g2d) {
        g2d.drawImage(immaginiGioco.getStart_screen(),0,0,this);
        g2d.drawImage(immaginiGioco.getTitolo(),180,60,this);
        for(Function item : startScreen.getScreenFunctions()) {
            drawButtons(item,g2d,Start);
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

    private void createTextHover(Graphics2D g2d, Point l, Dimension d){
        g2d.fillRect(l.x, l.y, d.width, 2);
        g2d.fillRect(l.x-2, l.y- d.height+2, 2, d.height);
        g2d.fillRect(l.x, l.y- d.height+2, d.width, 2);
        g2d.fillRect(l.x+ d.width, l.y- d.height+2, 2, d.height);
    }

    private void createImageHover(Graphics2D g2d,Point l,Dimension d){
        g2d.fillRect(l.x, l.y, d.width, 2); //Sopra
        g2d.fillRect(l.x, l.y, 2, d.height); //Sinistra
        g2d.fillRect(l.x + d.width - 2, l.y, 2, d.height); //Destra
        g2d.fillRect(l.x, l.y + d.height - 2, d.width, 2); //Sotto
    }

    private boolean drawedPlayer=false;
    private boolean drawedBoss=false;

    private void drawMap(Graphics2D g2d){



        world = Game.getInstance().getWorld();

        if(progress.getMaximum()!=world.getViewPort().getFirst().length()-Settings.Filtro_Size_Colonna) progress.setRange(0,world.getViewPort().getFirst().length()-Settings.Filtro_Size_Colonna);
        progress.updateProgress(world.getPlayer().getProgresso());
        g2d.drawImage(immaginiGioco.getBackgroundImage(world.getLiv()),-(world.getPlayer().getProgresso()*5),0,this);
        for(int i = 0; i < Settings.Filtro_Size_Riga; i++) {
            int y = i * Settings.CELL_SIZE_RIGA; //coordinate x sulla view
            for(int j = 0; j < Settings.Filtro_Size_Colonna; j++) {
                int x = j * Settings.CELL_SIZE_COLONNA; //coordinate y sulla view
                if(world.isPlayer(i,j+world.getPlayer().getProgresso()) )
                {
                    if(world.isPlayer(i+1,j+world.getPlayer().getProgresso()))
                    {
                        if(!new Position(i, j + world.getPlayer().getProgresso()).equals(world.getPlayer().getCoordinatePlayer().getFirst()) && !new Position(i+1, j + world.getPlayer().getProgresso()).equals(world.getPlayer().getCoordinatePlayer().getLast()))
                        {
                            world.setMatrice_Principale(i,j+world.getPlayer().getProgresso(), Block.VUOTO);
                            world.setMatrice_Principale(i+1,j+world.getPlayer().getProgresso(), Block.VUOTO);
                        }
                    }
                }

                if (world.getViewPort().get(i).charAt(j + world.getPlayer().getProgresso()) == 'j') {
                    g2d.drawImage(immaginiGioco.getFine(world.getLiv()), x + 8, y, this);
                }
                if (world.getViewPort().get(i).charAt(j + world.getPlayer().getProgresso()) == 'k') {
                    g2d.drawImage(immaginiGioco.getPortale(world.getLiv()), x, y, this);
                }
                if (world.getViewPort().get(i).charAt(j + world.getPlayer().getProgresso()) == 'n') {
                    g2d.drawImage(immaginiGioco.getTeletrasporto(), x, y, this);
                }
                if (world.isCoin(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getMoneta(world.getLiv()), x, y, this);
                } else if (world.isVelocita(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getVelocita(), x, y, this);
                } else if (world.isScudo(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getScudoIcon(), x, y, this);
                } else if (world.isLentezza(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getLentezza(), x, y, this);
                } else if (world.isVita(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getVita(), x, y, this);
                } else if (world.isUsato(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getUsato(), x, y, this);
                } else if (world.isWall(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getBloccoMuro(world.getLiv()), x, y, this);
                } else if (world.isTerra(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getBloccoTerra(world.getLiv()), x, y, this);
                } else if (world.isErba(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getBloccoErba(), x, y, this);
                } else if (world.isSpeciale(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getBloccoSpeciale(world.getLiv()), x, y, this);
                } else if (world.isFireball(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getFireball(), x, y, this);
                } else if(world.isTubo(i,j + world.getPlayer().getProgresso())){

                    if(!world.isTubo(i-1,j + world.getPlayer().getProgresso())){
                        if(!world.isTubo(i,j - 1 + world.getPlayer().getProgresso()))
                            g2d.drawImage(immaginiGioco.getTubo_sopra(world.getLiv(),-1), x, y, this);
                        else g2d.drawImage(immaginiGioco.getTubo_sopra(world.getLiv(),1), x, y, this);
                    }
                    else if(world.isTubo(i-1,j + world.getPlayer().getProgresso()))
                        if(!world.isTubo(i,j - 1 + world.getPlayer().getProgresso()))
                            g2d.drawImage(immaginiGioco.getTubo_sotto(world.getLiv(),-1), x+5, y, this);
                        else g2d.drawImage(immaginiGioco.getTubo_sotto(world.getLiv(),1), x, y, this);
                }
                else if (world.isBossFinale(i,j+world.getPlayer().getProgresso()))
                {
                    if (!drawedBoss)
                    {
                        g2d.drawImage(immaginiGioco.getEnder_dragon(),x,y,this);
                        drawedBoss= true;
                    }

                }
                 else if (world.isBarile(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getBarile(world.getLiv()), x, y, this);
                } else if (world.isMorte(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getMorte(world.getLiv()), x, y, this);
                } else if (world.isPonte(i, j + world.getPlayer().getProgresso())) {
                    g2d.drawImage(immaginiGioco.getPonte(world.getLiv()), x, y, this);}
                else if(world.isPlayer(i, j+world.getPlayer().getProgresso())) {
                    if (world.isPlayer(i-1, j+world.getPlayer().getProgresso()) && !drawedPlayer && world.getPlayer().getCoordinatePlayer().getFirst().equals(new Position(i-1, j + world.getPlayer().getProgresso())) && world.getPlayer().getCoordinatePlayer().getLast().equals(new Position(i, j + world.getPlayer().getProgresso()))) {
                        if(Controller.getPressed().contains(Settings.MOVE_RIGHT))
                            if(world.getPlayer().getScudo()){
                                g2d.drawImage(immaginiGioco.getScudo(Settings.MOVE_RIGHT,personaggio),x,y-Settings.CELL_SIZE_RIGA,this);
                            }
                            else g2d.drawImage(immaginiGioco.getPersonaggio(Settings.MOVE_RIGHT),x,y-Settings.CELL_SIZE_RIGA,this);
                        else if(Controller.getPressed().contains(Settings.MOVE_LEFT))
                            if(world.getPlayer().getScudo()){
                                g2d.drawImage(immaginiGioco.getScudo(Settings.MOVE_LEFT,personaggio),x,y-Settings.CELL_SIZE_RIGA,this);
                            }
                            else g2d.drawImage(immaginiGioco.getPersonaggio(Settings.MOVE_LEFT),x,y-Settings.CELL_SIZE_RIGA,this);
                        else {
                            if(world.getPlayer().getScudo()){
                                g2d.drawImage(immaginiGioco.getScudo(world.getPlayer().getPreDirection(),personaggio),x,y-Settings.CELL_SIZE_RIGA,this);
                            }
                            else g2d.drawImage(immaginiGioco.getPersonaggio(world.getPlayer().getPreDirection()), x, y - Settings.CELL_SIZE_RIGA, this);
                        }
                        drawedPlayer=true;
                    }
                }

                else if(world.isNemico(i, j+world.getPlayer().getProgresso())){

                    for(Enemy o: world.getNemici()){
                        if(o instanceof Skeleton) {

                            if (o.getCoordinate().getLast().i() == i && o.getCoordinate().getLast().j() == j + world.getPlayer().getProgresso()) {
                                g2d.drawImage(immaginiGioco.getSkeleton(o.getDirection()), x, y, this);

                                break;
                            }
                        }

                        else if(o instanceof Creeper)
                        {
                            if (o.getCoordinate().getLast().i()==i && o.getCoordinate().getLast().j()==j+world.getPlayer().getProgresso()){
                                g2d.drawImage(immaginiGioco.getCreeper(o.getDirection(),((Creeper) o).getEsplosione()),x,y,this);

                                break;

                            }
                        }
                        else if(world.isMiniZombie(i, j+world.getPlayer().getProgresso())){

                            if(o instanceof MiniZombie){
                                if (o.getCoordinate().getFirst().i()==i && o.getCoordinate().getFirst().j()==j+world.getPlayer().getProgresso()){
                                    g2d.drawImage(immaginiGioco.getMiniZombie(o.getDirection()),x,y,this);
                                    break;
                                }
                            }

                }
                    }
                }
                if(world.isFreccia(i, j+world.getPlayer().getProgresso())){
                    for(Enemy o: world.getNemici()){
                        if(o instanceof Skeleton){
                            for(Arrow a: ((Skeleton) o).getArrows()){
                                if(Objects.equals(a.getPos(), new Position(i, j + world.getPlayer().getProgresso()))){
                                    g2d.drawImage(immaginiGioco.getFreccia(a.getDir()),x,y,this);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }
        drawedPlayer=false;
        drawedBoss= false;
        drawAbilityIcons(g2d);
        drawProgress(g2d,world.getPlayer().getProgresso());
        drawCoins(g2d,world.getPlayer().getCoins());
        drawLives(g2d,world.getPlayer().getLives());
        drawLevel(g2d,world.getLiv());
    }

    private void drawAbilityIcons(Graphics2D g2d){
        if(world.getPlayer().getVelocita()){
            g2d.drawImage(immaginiGioco.getFlash(),15,80,this);
            Alt=Alt.deriveFont(25.0f);
            g2d.setFont(Alt);
            g2d.setColor(Color.WHITE);
            g2d.drawString(world.getPlayer().getVelC()*66/100+"", 80, 120);
        }
        else if(world.getPlayer().getLentezza()){
            g2d.drawImage(immaginiGioco.getTartaruga(),15,80,this);
            Alt=Alt.deriveFont(25.0f);
            g2d.setFont(Alt);
            g2d.setColor(Color.WHITE);
            g2d.drawString(world.getPlayer().getLenC()*66/100+"", 80, 120);
        }
    }
    private void drawLevel(Graphics2D g2d,int liv) {
        g2d.setFont(Alt);
        g2d.drawString("Level "+liv, 40, 560);
    }
    private void drawLives(Graphics2D g2d,int lives) {
        for(int i=1;i<=lives;i++){
            g2d.drawImage(immaginiGioco.getVita(),i*30,18, this);
        }
    }
    private void drawCoins(Graphics2D g2d,int coins) {
        g2d.setFont(Alt);
        g2d.drawString(coins+"", 830, 40);
        g2d.drawImage(immaginiGioco.getMoneta(world.getLiv()),850,18, this);
    }

    private void drawProgress(Graphics2D g2d,int progresso) {
        int x=Settings.WINDOW_SIZE_X/2-progress.getMaximum();
        int y=30;
        for(int i=1;i<=progresso;i++){
            g2d.setColor(Color.WHITE);
            g2d.fillRect((i*2)+x,y,2,2);
        }
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x+progress.getMaximum()*2+5,y-3,2,8);
        g2d.fillRect(x-3,y-3,2,8);
        g2d.fillRect(x-1,y-3,progress.getMaximum()*2+6,2);
        g2d.fillRect(x-1,y+3,progress.getMaximum()*2+6,2);
        String completion = String.format("%.0f", progress.getPercentComplete()*100);
        g2d.setFont(Reg);
        g2d.drawString(completion, x-33, y+5);
    }


    public static void setLock2(boolean lock2) {
        GamePanel.lock2 = lock2;
    }

    public static void setLock3(boolean lock3) {
        GamePanel.lock3 = lock3;
    }

    public static boolean getLock2() {
        return lock2;
    }

    public static boolean getLock3() {
        return lock3;
    }
    private static boolean lock2=false;
    private static boolean lock3=false;


    public void update() {this.repaint();}
    private Object path;
    Sound click = new Sound("click.wav");
    public void select() {
        if (Game.getGameStatus().equals(GameStatus.START_SCREEN)){
            path = startScreen.select(getMousePosition());
            if (soundtrack != null && path!=null) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.WIN)) {
            path = winScreen.select(getMousePosition(), world.getLiv());
            if (soundtrack != null && path!=null) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.CHOOSE_SKIN) || Game.getGameStatus().equals(GameStatus.STEVE)||  Game.getGameStatus().equals(GameStatus.ALEX)) {
            path = skinScreen.select(getMousePosition());
            if (soundtrack != null && path=="Indietro") {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.PAUSE)){
            path = pauseScreen.select(getMousePosition());
            if (soundtrack != null && path!=null) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.GAME_OVER)){
            path = loseScreen.select(getMousePosition());
            if (world.getPlayer().getMorte() != null && path!=null) {
                world.getPlayer().getMorte().pause();
            }

        }
        else if (Game.getGameStatus().equals(GameStatus.MAP_SELECTION)){
            path = mapScreen.select(getMousePosition());
            if (soundtrack != null && path !=null && contenutoMap.get(path)!=GameStatus.MAP_SELECTION) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.COMMANDS_SCREEN)){
            path = comScreen.select(getMousePosition());
        }
        else if (Game.getGameStatus().equals(GameStatus.HELP)){
            path = helpScreen.select(getMousePosition());
            if (soundtrack != null && path!=null) {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.COPYRIGHT)){
            path = copyrightScreen.select(getMousePosition());
            if (soundtrack != null && path !=null && path!= "https://creativecommons.org/licenses/by-nc/4.0/deed.it" && path!="https://www.minecraft.net/it-it/usage-guidelines") {
                soundtrack.pause();
                soundtrack = null;
            }
        }
        else if (Game.getGameStatus().equals(GameStatus.ABOUT_SCREEN)){
            path = aboutScreen.select(getMousePosition());
            if (soundtrack != null && path!=null) {
                soundtrack.pause();
                soundtrack = null;
            }
        }

        if (path != null) {
            click.play();
            if(path=="Esci"){
                System.exit(1);
            }
            else if(path=="Ricomincia"){
                Game.getInstance().setWorld(new World(1, 3));
                if(!lock2) lock2=true;
                if(!lock3) lock3=true;
            }
            else if(path=="Menu Principale" && Game.getGameStatus()==GameStatus.GAME_OVER){
                if(!lock2) lock2=true;
                if(!lock3) lock3=true;
            }
            else if (path=="Gioca"){
                mapScreen=new MapScreen();
            }
            else if(path== "https://creativecommons.org/licenses/by-nc/4.0/deed.it" || path=="https://www.minecraft.net/it-it/usage-guidelines")
            {
                copyrightScreen.openWebpage((String) path);
            }
            if(path==ImmaginiGioco.getMap1(false)){
                Game.getInstance().setWorld(new World(1,3));
            }
            else if(path==ImmaginiGioco.getMap2(false)){
                Game.getInstance().setWorld(new World(2,3));
            }
            else if(path==ImmaginiGioco.getMap3(false)){
                Game.getInstance().setWorld(new World(3,3));
            }
            else if(path==ImmaginiGioco.getWASD()){
                if(Controller.getTipo()==0) Controller.setTipo(1);
            }
            else if(path==ImmaginiGioco.getFreccette()){
                if(Controller.getTipo()==1) Controller.setTipo(0);
            }
            if(path!=ImmaginiGioco.getWASD() && path!=ImmaginiGioco.getFreccette() && path!="https://creativecommons.org/licenses/by-nc/4.0/deed.it" && path!="https://www.minecraft.net/it-it/usage-guidelines") Game.setGameStatus(contenutoMap.get(path));
            path=null;
        }
    }


}



