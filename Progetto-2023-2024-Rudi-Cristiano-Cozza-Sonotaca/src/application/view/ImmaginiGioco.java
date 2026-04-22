package application.view;

import application.model.Settings;
import application.resources.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImmaginiGioco {
    private Image personaggioDes;
    private Image personaggioSin;
    private Image backgroundImage;
    private Image backgroundImage2;
    private Image backgroundImage3;
    private Image bloccoTerra;
    private Image bloccoErba;
    private Image bloccoMuro;
    private Image bloccoSpeciale;

    private Image barile;
    private Image fine;
    private Image fine3;
    private Image portale;
    private Image portale_end;
    private Image acqua;
    private Image ponte2;
    private Image ponte3;
    private Image teletrasporto;
    private Image bloccoTerra2;
    private Image bloccoTerra3;
    private Image bloccoMuro2;
    private Image bloccoMuro3;
    private Image bloccoSpeciale2;


    private Image oro;
    private Image lava;
    private Image ender_dragon;
    private Image end_frame;
    private Image ender_egg;
    private Image moneta;
    private Image moneta2;
    private Image moneta3;
    private Image start_screen;
    private Image skin_screen;
    private Image usato;
    private Image vita;

    private static Image riconoscimenti;
    private static Image copyright;
    private Image titolo;
    private static Image pulsante;
    private Image skin_button;
    private static Image steve;
    private static Image alex;
    private static Image steve_completo;
    private static Image alex_completo;
    private Image pausa;
    private Image minizombieDX;
    private Image creeperDX;

    private Image skeletonDX;
    private final Image skeletonSX;
    private Image frecciaDX;
    private final Image frecciaSX;
    private Image creeperActDX;
    private final Image creeperActSX;
    private Image lose_screen;
    private Image skull;
    private final Image minizombieSX;
    private final Image creeperSX;
    private Image velocita;
    private Image lentezza;
    private Image scudo;
    private Image tartaruga;
    private Image flash;

    //TUBI
    private Image tubo3_sopSX;
    private Image tubo1_sopSX;
    private Image tubo2_sopSX;
    private Image tubo3_sotSX;
    private Image tubo1_sotSX;
    private Image tubo2_sotSX;

    private Image tubo1_sopDX;
    private Image tubo2_sopDX;
    private Image tubo3_sopDX;
    private Image tubo1_sotDX;
    private Image tubo2_sotDX;
    private Image tubo3_sotDX;




    private static Image map1;
    private static Image map2;
    private static Image map3;
    private static Image map1no;
    private static Image map2no;
    private static Image map3no;
    private Image mapBG;
    private Image lock;
    private static Image WASD;
    private static Image freccette;
    private Image esc;

    private Image scudoSteveDX;
    private final Image scudoSteveSX;
    private Image scudoAlexDX;
    private final Image scudoAlexSX;
    private Image spazio;
    private Image board;
    private Image helpBG;

    private Image fireball;

    private Image copBackground;

    private Image creativeCommons;

    private Image devBackground;

    public ImmaginiGioco() throws IOException {
        backgroundImage = ImageIO.read(new File("src/application/resources/BackgroundImage/sfondo_liv1.png"));
        backgroundImage = backgroundImage.getScaledInstance(6240,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        backgroundImage2 = ImageIO.read(new File("src/application/resources/BackgroundImage/sfondo_liv2.png"));
        backgroundImage2 = backgroundImage2.getScaledInstance(1830,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        backgroundImage3 = ImageIO.read(new File("src/application/resources/BackgroundImage/sfondo_liv3.png"));
        backgroundImage3 = backgroundImage3.getScaledInstance(3120,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        personaggioDes= ImageIO.read(new File("src/application/resources/Player/steve.png"));
        personaggioDes = personaggioDes.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        personaggioSin= ImageUtil.flipImageHorizontally(personaggioDes);
        bloccoErba = ImageIO.read(new File("src/application/resources/blocks/bloccoErba.png"));
        bloccoErba = bloccoErba.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoTerra = ImageIO.read(new File("src/application/resources/blocks/bloccoTerra.png"));
        bloccoTerra = bloccoTerra.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoMuro = ImageIO.read(new File("src/application/resources/blocks/bloccoMuro.png"));
        bloccoMuro = bloccoMuro.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoSpeciale = ImageIO.read(new File("src/application/resources/blocks/bloccoSpeciale.png"));
        bloccoSpeciale = bloccoSpeciale.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo1_sopSX = ImageIO.read(new File("src/application/resources/blocks/tubo1_sopSX.png"));
        tubo1_sopSX = tubo1_sopSX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo2_sopSX = ImageIO.read(new File("src/application/resources/blocks/tubo2_sopSX.png"));
        tubo2_sopSX = tubo2_sopSX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo3_sopSX = ImageIO.read(new File("src/application/resources/blocks/tubo3_sopSX.png"));
        tubo3_sopSX = tubo3_sopSX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo3_sotSX = ImageIO.read(new File("src/application/resources/blocks/tubo3_sotSX.png"));
        tubo3_sotSX = tubo3_sotSX.getScaledInstance(Settings.CELL_SIZE_RIGA-5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo2_sotSX = ImageIO.read(new File("src/application/resources/blocks/tubo2_sotSX.png"));
        tubo2_sotSX = tubo2_sotSX.getScaledInstance(Settings.CELL_SIZE_RIGA-5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo1_sotSX = ImageIO.read(new File("src/application/resources/blocks/tubo1_sotSX.png"));
        tubo1_sotSX = tubo1_sotSX.getScaledInstance(Settings.CELL_SIZE_RIGA-5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo1_sopDX = ImageIO.read(new File("src/application/resources/blocks/tubo1_sopDX.png"));
        tubo1_sopDX = tubo1_sopDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo2_sopDX = ImageIO.read(new File("src/application/resources/blocks/tubo2_sopDX.png"));
        tubo2_sopDX = tubo2_sopDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo3_sopDX = ImageIO.read(new File("src/application/resources/blocks/tubo3_sopDX.png"));
        tubo3_sopDX = tubo3_sopDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo3_sotDX = ImageIO.read(new File("src/application/resources/blocks/tubo3_sotDX.png"));
        tubo3_sotDX = tubo3_sotDX.getScaledInstance(Settings.CELL_SIZE_RIGA-5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo2_sotDX = ImageIO.read(new File("src/application/resources/blocks/tubo2_sotDX.png"));
        tubo2_sotDX = tubo2_sotDX.getScaledInstance(Settings.CELL_SIZE_RIGA-5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tubo1_sotDX = ImageIO.read(new File("src/application/resources/blocks/tubo1_sotDX.png"));
        tubo1_sotDX = tubo1_sotDX.getScaledInstance(Settings.CELL_SIZE_RIGA-5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        barile = ImageIO.read(new File("src/application/resources/blocks/barile.png"));
        barile = barile.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        fine = ImageIO.read(new File("src/application/resources/blocks/fine.png"));
        fine = fine.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*9,Image.SCALE_SMOOTH);
        fine3 = ImageIO.read(new File("src/application/resources/blocks/fine3.gif"));
        fine3 = fine3.getScaledInstance(Settings.CELL_SIZE_RIGA*3,Settings.CELL_SIZE_COLONNA*3,Image.SCALE_SMOOTH);
        portale = ImageIO.read(new File("src/application/resources/blocks/portale.png"));
        portale = portale.getScaledInstance(Settings.CELL_SIZE_RIGA*4,Settings.CELL_SIZE_COLONNA*5,Image.SCALE_SMOOTH);
        portale_end = ImageIO.read(new File("src/application/resources/blocks/portale_end.png"));
        portale_end = portale_end.getScaledInstance(Settings.CELL_SIZE_RIGA*5,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        ponte2 = ImageIO.read(new File("src/application/resources/blocks/ponte2.png"));
        ponte2 = ponte2.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        ponte3 = ImageIO.read(new File("src/application/resources/blocks/ponte3.png"));
        ponte3 = ponte3.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA/2,Image.SCALE_SMOOTH);
        acqua = ImageIO.read(new File("src/application/resources/blocks/acqua.jpeg"));
        acqua = acqua.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        teletrasporto = ImageIO.read(new File("src/application/resources/blocks/teletrasporto.gif"));
        teletrasporto = teletrasporto.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        ender_dragon = ImageIO.read(new File("src/application/resources/blocks/ender_dragon.png"));
        ender_dragon = ender_dragon.getScaledInstance(Settings.CELL_SIZE_RIGA*8,Settings.CELL_SIZE_COLONNA*4,Image.SCALE_SMOOTH);
        bloccoTerra2 = ImageIO.read(new File("src/application/resources/blocks/bloccoTerra2.png"));
        bloccoTerra2 = bloccoTerra2.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoTerra3 = ImageIO.read(new File("src/application/resources/blocks/bloccoTerra3.png"));
        bloccoTerra3 = bloccoTerra3.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoMuro2 = ImageIO.read(new File("src/application/resources/blocks/bloccoMuro2.png"));
        bloccoMuro2 = bloccoMuro2.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoMuro3 = ImageIO.read(new File("src/application/resources/blocks/bloccoMuro3.png"));
        bloccoMuro3 = bloccoMuro3.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        bloccoSpeciale2 = ImageIO.read(new File("src/application/resources/blocks/bloccoSpeciale2.png"));
        bloccoSpeciale2 = bloccoSpeciale2.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        oro = ImageIO.read(new File("src/application/resources/blocks/oro.png"));
        oro = oro.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        lava = ImageIO.read(new File("src/application/resources/blocks/lava.jpg"));
        lava = lava.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        end_frame = ImageIO.read(new File("src/application/resources/blocks/end_frame.png"));
        end_frame = end_frame.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        ender_egg = ImageIO.read(new File("src/application/resources/blocks/ender_egg.png"));
        ender_egg = ender_egg.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        moneta = ImageIO.read(new File("src/application/resources/blocks/coin.png"));
        moneta = moneta.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        moneta2 = ImageIO.read(new File("src/application/resources/blocks/coin2.png"));
        moneta2 = moneta2.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        moneta3 = ImageIO.read(new File("src/application/resources/blocks/coin3.png"));
        moneta3 = moneta3.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        start_screen = ImageIO.read(new File("src/application/resources/mainScreen/main_screen_image.png"));
        start_screen = start_screen.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        usato = ImageIO.read(new File("src/application/resources/blocks/usato.png"));
        usato = usato.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        vita = ImageIO.read(new File("src/application/resources/abilità/vita.png"));
        vita = vita.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        riconoscimenti = ImageIO.read(new File("src/application/resources/mainScreen/riconoscimenti.png"));
        riconoscimenti = riconoscimenti.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        copyright = ImageIO.read(new File("src/application/resources/mainScreen/copyright.png"));
        copyright = copyright.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        titolo = ImageIO.read(new File("src/application/resources/mainScreen/titolo.png"));
        titolo = titolo.getScaledInstance(Settings.CELL_SIZE_RIGA*18,Settings.CELL_SIZE_COLONNA*4,Image.SCALE_SMOOTH);
        pulsante = ImageIO.read(new File("src/application/resources/mainScreen/pulsante.png"));
        pulsante = pulsante.getScaledInstance(Settings.CELL_SIZE_RIGA*8,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        creativeCommons = ImageIO.read(new File("src/application/resources/copyrightScreen/creativeCommons.png"));
        creativeCommons = creativeCommons.getScaledInstance(Settings.CELL_SIZE_RIGA*8,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);


        // IMMAGINI SCHERMATA SKIN
        skin_screen = ImageIO.read(new File("src/application/resources/BackgroundImage/skin_background.png"));
        skin_screen = skin_screen.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        skin_button = ImageIO.read(new File("src/application/resources/mainScreen/pulsante.png"));
        skin_button = skin_button.getScaledInstance(Settings.CELL_SIZE_RIGA*6,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        steve = ImageIO.read(new File("src/application/resources/skinScreen/steve_i.png"));
        steve = steve.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        alex = ImageIO.read(new File("src/application/resources/skinScreen/alex_i.png"));
        alex = alex.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        steve_completo = ImageIO.read(new File("src/application/resources/skinScreen/steve_completo.png"));
        steve_completo = steve_completo.getScaledInstance(Settings.CELL_SIZE_RIGA*5,Settings.CELL_SIZE_COLONNA*10,Image.SCALE_SMOOTH);
        alex_completo = ImageIO.read(new File("src/application/resources/skinScreen/alex_completo.png"));
        alex_completo = alex_completo.getScaledInstance(Settings.CELL_SIZE_RIGA*5,Settings.CELL_SIZE_COLONNA*10,Image.SCALE_SMOOTH);



        pausa = ImageIO.read(new File("src/application/resources/pauseScreen/pausa.png"));
        pausa = pausa.getScaledInstance(Settings.CELL_SIZE_RIGA*4,Settings.CELL_SIZE_COLONNA*4,Image.SCALE_SMOOTH);
        minizombieDX = ImageIO.read(new File("src/application/resources/nemici/minizombie.gif"));
        minizombieDX = minizombieDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        minizombieSX=ImageUtil.flipImageHorizontally(minizombieDX);
        creeperDX = ImageIO.read(new File("src/application/resources/nemici/creeper.png"));
        creeperDX = creeperDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        creeperSX=ImageUtil.flipImageHorizontally(creeperDX);

        creeperActDX = ImageIO.read(new File("src/application/resources/nemici/creeperAct.png"));
        creeperActDX = creeperActDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        creeperActSX=ImageUtil.flipImageHorizontally(creeperActDX);
        velocita = ImageIO.read(new File("src/application/resources/abilità/velocita.gif"));
        velocita = velocita.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        lentezza = ImageIO.read(new File("src/application/resources/abilità/lentezza.png"));
        lentezza = lentezza.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        scudo = ImageIO.read(new File("src/application/resources/abilità/scudo.png"));
        scudo = scudo.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        tartaruga = ImageIO.read(new File("src/application/resources/icone/tartaruga.png"));
        tartaruga = tartaruga.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        flash = ImageIO.read(new File("src/application/resources/icone/flash.png"));
        flash = flash.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);

        // NEMICI
        skeletonDX = ImageIO.read(new File("src/application/resources/nemici/skeleton.png"));
        skeletonDX = skeletonDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        skeletonSX=ImageUtil.flipImageHorizontally(skeletonDX);
        frecciaDX = ImageIO.read(new File("src/application/resources/nemici/freccia.png"));
        frecciaDX = frecciaDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);
        frecciaSX=ImageUtil.flipImageHorizontally(frecciaDX);
        lose_screen = ImageIO.read(new File("src/application/resources/gameOverScreen/blood.png"));
        lose_screen = lose_screen.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        skull = ImageIO.read(new File("src/application/resources/gameOverScreen/skull.png"));
        skull = skull.getScaledInstance(Settings.CELL_SIZE_RIGA*5,Settings.CELL_SIZE_COLONNA*5,Image.SCALE_SMOOTH);

        fireball = ImageIO.read(new File("src/application/resources/nemici/fireball.png"));
        fireball = fireball.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA,Image.SCALE_SMOOTH);

        map1 = ImageIO.read(new File("src/application/resources/BackgroundImage/liv1.png"));
        map1 = map1.getScaledInstance(Settings.CELL_SIZE_RIGA*10,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        map2 = ImageIO.read(new File("src/application/resources/BackgroundImage/liv2.png"));
        map2 = map2.getScaledInstance(Settings.CELL_SIZE_RIGA*10,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        map3 = ImageIO.read(new File("src/application/resources/BackgroundImage/liv3.png"));
        map3 = map3.getScaledInstance(Settings.CELL_SIZE_RIGA*10,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        map1no = ImageIO.read(new File("src/application/resources/BackgroundImage/liv1no.png"));
        map1no = map1no.getScaledInstance(Settings.CELL_SIZE_RIGA*10,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        map2no = ImageIO.read(new File("src/application/resources/BackgroundImage/liv2no.png"));
        map2no = map2no.getScaledInstance(Settings.CELL_SIZE_RIGA*10,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        map3no = ImageIO.read(new File("src/application/resources/BackgroundImage/liv3no.png"));
        map3no = map3no.getScaledInstance(Settings.CELL_SIZE_RIGA*10,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        mapBG = ImageIO.read(new File("src/application/resources/BackgroundImage/mapBG.png"));
        mapBG = mapBG.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        lock = ImageIO.read(new File("src/application/resources/mapScreen/lock.png"));
        lock = lock.getScaledInstance(Settings.CELL_SIZE_RIGA*3,Settings.CELL_SIZE_COLONNA*3,Image.SCALE_SMOOTH);
        WASD = ImageIO.read(new File("src/application/resources/commandScreen/wasd.png"));
        WASD = WASD.getScaledInstance(Settings.CELL_SIZE_RIGA*7,Settings.CELL_SIZE_COLONNA*7,Image.SCALE_SMOOTH);
        freccette = ImageIO.read(new File("src/application/resources/commandScreen/freccette.png"));
        freccette = freccette.getScaledInstance(Settings.CELL_SIZE_RIGA*7,Settings.CELL_SIZE_COLONNA*7,Image.SCALE_SMOOTH);
        spazio = ImageIO.read(new File("src/application/resources/commandScreen/spazio.png"));
        spazio = spazio.getScaledInstance(Settings.CELL_SIZE_RIGA*6,Settings.CELL_SIZE_COLONNA*6,Image.SCALE_SMOOTH);
        esc = ImageIO.read(new File("src/application/resources/commandScreen/esc.png"));
        esc = esc.getScaledInstance(Settings.CELL_SIZE_RIGA*2,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        board = ImageIO.read(new File("src/application/resources/BackgroundImage/board_help.png"));
        board = board.getScaledInstance(Settings.WINDOW_SIZE_X-45,Settings.WINDOW_SIZE_Y-30,Image.SCALE_SMOOTH);
        helpBG = ImageIO.read(new File("src/application/resources/BackgroundImage/helpBG.jpg"));
        helpBG = helpBG.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);

        copBackground = ImageIO.read(new File("src/application/resources/BackgroundImage/black_screen.jpg"));
        copBackground = copBackground.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);
        devBackground = ImageIO.read(new File("src/application/resources/BackgroundImage/dev_background.png"));
        devBackground = devBackground.getScaledInstance(Settings.WINDOW_SIZE_X,Settings.WINDOW_SIZE_Y,Image.SCALE_SMOOTH);


        scudoSteveDX = ImageIO.read(new File("src/application/resources/Player/steveScudo.png"));
        scudoSteveDX = scudoSteveDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        scudoSteveSX=ImageUtil.flipImageHorizontally(scudoSteveDX);
        scudoAlexDX = ImageIO.read(new File("src/application/resources/Player/alexScudo.png"));
        scudoAlexDX = scudoAlexDX.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        scudoAlexSX=ImageUtil.flipImageHorizontally(scudoAlexDX);
    }

    public Image getFireball() {
        return fireball;
    }

    public Image getDevBackground() {
        return devBackground;
    }

    public Image getCopBackground() {
        return copBackground;
    }

    public Image getBoard() {
        return board;
    }

    public Image getHelpBG() {
        return helpBG;
    }

    public Image getEsc() {
        return esc;
    }

    public Image getSpazio() {
        return spazio;
    }

    public static Image getWASD() {
        return WASD;
    }

    public Image getCreativeCommons() {
        return creativeCommons;
    }

    public static Image getFreccette() {
        return freccette;
    }

    public Image getLock() {
        return lock;
    }

    public Image getMapBG() {
        return mapBG;
    }

    public static Image getMap1(boolean lock) {
        if(lock) return map1no;
        return map1;
    }

    public static Image getMap2(boolean lock) {
        if(lock) return map2no;
        return map2;
    }

    public static Image getMap3(boolean lock) {
        if(lock) return map3no;
        return map3;
    }

    public Image getTartaruga() {
        return tartaruga;
    }

    public Image getFlash() {
        return flash;
    }

    public Image getScudoIcon(){
        return scudo;
    }
    public Image getScudo(int direction, String p) {
        if(direction==Settings.MOVE_RIGHT) {
            if(p.equals("steve"))
                return scudoSteveDX;
            return scudoAlexDX;
        }
        if(p.equals("steve"))
            return scudoSteveSX;
        return scudoAlexSX;
    }

    public Image getLentezza() {
        return lentezza;
    }

    public Image getVelocita() {
        return velocita;
    }

    public Image getMiniZombie(int direction){
        if(direction==1){
            return minizombieDX;
        }
        return minizombieSX;
    }
    public Image getCreeper(int direction, int esplosione){
        if(direction==1) {
            if(esplosione>0) return creeperActDX;
            return creeperDX;
        }

        if(esplosione>0) return creeperActSX;
        return creeperSX;
    }

    public Image getSkull() {
        return skull;
    }

    public Image getLose_screen() {
        return lose_screen;
    }

    public Image getFreccia(int direction){
        if(direction==1){
            return frecciaDX;
        }
        return frecciaSX;
    }
    public Image getSkeleton(int direction){
        if(direction==1){
            return skeletonDX;
        }
        return skeletonSX;
    }


    public Image getPausa() {return pausa;}
    public static Image getPulsante() {
        return pulsante;
    }

    public Image getPersonaggio(int direction) {
        if (direction == Settings.MOVE_RIGHT) return personaggioDes;
        else return personaggioSin;
    }

    public static Image getSteve_completo() {
        return steve_completo;
    }
    public static Image getAlex_completo() {
        return alex_completo;
    }

    public static Image getSteve() {
        return steve;
    }

    public static Image getAlex() {
        return alex;
    }

    public void setPersonaggio(String p) throws IOException {
        personaggioDes= ImageIO.read(new File("src/application/resources/Player/"+p+".png"));
        personaggioDes = personaggioDes.getScaledInstance(Settings.CELL_SIZE_RIGA,Settings.CELL_SIZE_COLONNA*2,Image.SCALE_SMOOTH);
        personaggioSin=ImageUtil.flipImageHorizontally(personaggioDes);
    }

    public Image getSkin_screen() {
        return skin_screen;
    }

    public Image getSkin_button(){return skin_button;}

    public Image getTitolo() {
        return titolo;
    }

    public static Image getRiconoscimenti() {
        return riconoscimenti;
    }

    public static Image getCopyright() {
        return copyright;
    }

    public Image getBackgroundImage(int liv) {
        if(liv==1) return backgroundImage;
        else if(liv==2) return backgroundImage2;
        return backgroundImage3;
    }

    public Image getMoneta(int liv) {
        if(liv==1) return moneta;
        else if(liv==2) return moneta2;
        return moneta3;
    }

    public Image getBloccoTerra(int liv) {
        if(liv==1) return bloccoTerra;
        else if(liv==2) return bloccoTerra2;
        return bloccoTerra3;
    }

    public Image getBloccoErba() {
        return bloccoErba;
    }

    public Image getBloccoMuro(int liv) {
        if(liv==1) return bloccoMuro;
        else if(liv==2) return bloccoMuro2;
        return bloccoMuro3;
    }

    public Image getBloccoSpeciale(int liv) {
        if(liv==1) return bloccoSpeciale;
        return bloccoSpeciale2;
    }


    public Image getTubo_sopra(int liv, int dir) {
        if(liv==1){
            if(dir==-1) return tubo1_sopSX;
            return tubo1_sopDX;
        }
        else if(liv==2){
            if(dir==-1) return tubo2_sopSX;
            return tubo2_sopDX;
        }
        if(dir==-1) return tubo3_sopSX;
        return tubo3_sopDX;
    }

    public Image getTubo_sotto(int liv,int dir) {
        if(liv==1){
            if(dir==-1) return tubo1_sotSX;
            return tubo1_sotDX;
        }
        else if(liv==2){
            if(dir==-1) return tubo2_sotSX;
            return tubo2_sotDX;
        }
        if(dir==-1) return tubo3_sotSX;
        return tubo3_sotDX;
    }


    public Image getBarile(int liv) {
        if(liv==1) return barile;
        else if(liv==2) return oro;
        return end_frame;
    }

    public Image getFine(int liv) {
        if(liv==1 || liv==2) return fine;
        return fine3;
    }

    public Image getPortale(int liv) {
        if(liv==1) return portale;
        else if(liv==2) return portale_end;
        return ender_egg;
    }

    public Image getMorte(int liv) {
        if(liv==1) return acqua;
        return lava;
    }
    public Image getPonte(int liv) {
        if(liv==2) return ponte2;
        return ponte3;
    }
    public Image getTeletrasporto() {
        return teletrasporto;

    }

    public Image getEnder_dragon() {
        return ender_dragon;
    }

    public Image getUsato() {
        return usato;
    }

    public Image getStart_screen() {
        return start_screen;
    }
    public Image getVita(){
        return vita;
    }
}
