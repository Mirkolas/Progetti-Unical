package application.View;

import application.Model.ImpostazioniDim;

import java.awt.*;
import java.util.ArrayList;

public class Pausa {
    private final ArrayList<Object> functions = new ArrayList<>();
    private final Function[] pauseScreenFunctions;

    public Function[] getScreenFunctions() {
        return pauseScreenFunctions;
    }

    public Pausa() {
        getFunctions();
        this.pauseScreenFunctions = createItems(this.functions);
    }

    private void getFunctions() {
        functions.add("Riprendi");
        functions.add("Menu Principale");

    }

    private Function[] createItems(ArrayList<Object> func) {
        if (func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.getFirst(),GamePanel.Stile2))/2-15,395));
        items[1] = new Function(func.getLast(), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(1),GamePanel.Stile2))/2-30,545));
        return items;
    }

    public Object select(Point mouseLocation) {
        for (Function item : pauseScreenFunctions) {
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

