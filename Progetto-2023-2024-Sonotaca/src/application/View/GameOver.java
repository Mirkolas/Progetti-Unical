package application.View;
import application.Model.ImpostazioniDim;

import java.awt.*;
import java.util.ArrayList;

public class GameOver {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] loseScreenFunctions;
    public Function[] getScreenFunctions() {
        return loseScreenFunctions;
    }

    public GameOver(){
        getFunctions();
        this.loseScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Menu Principale");
        functions.add("Ricomincia");
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.getFirst(),GamePanel.Stile2))/2-20,445));
        items[1] = new Function(func.getLast(), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(1),GamePanel.Stile2))/2-12,545));
        return items;
    }

    public Object select(Point mouseLocation) {
        for (Function item : loseScreenFunctions) {
            Dimension dimension = item.getDimension();
            Point location = item.getLocation();
            boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
            boolean inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
            if (inX && inY) {
                return item.getObject();
            }
        }
        return null;
    }
}

