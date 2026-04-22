package application.view;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public class CopyrightScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] copyrightScreenFunctions;
    public Function[] getScreenFunctions() {
        return copyrightScreenFunctions;
    }

    public CopyrightScreen(){
        getFunctions();
        this.copyrightScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Indietro");
        functions.add("https://creativecommons.org/licenses/by-nc/4.0/deed.it");
        functions.add("https://www.minecraft.net/it-it/usage-guidelines");

    }

    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point(47,62));
        items[1] = new Function(func.get(1) , new Point(190,500));
        items[2] = new Function(func.getLast() , new Point(235,540));

        return items;
    }


    public Object select(Point mouseLocation) {
        for (Function item : copyrightScreenFunctions){
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


    void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URI(urlString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

