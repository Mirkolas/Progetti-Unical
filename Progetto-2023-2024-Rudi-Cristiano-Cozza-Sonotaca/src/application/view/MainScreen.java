package application.view;

import java.awt.*;
import java.util.ArrayList;

public class MainScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] mainScreenFunctions;

    public Function[] getScreenFunctions() {
        return mainScreenFunctions;
    }

    public MainScreen(){
        getFunctions();
        this.mainScreenFunctions=createItems(this.functions);
    }
    private void getFunctions(){
        functions.add("Gioca");
        functions.add("Scegli skin");
        functions.add("Aiuto");
        functions.add("Comandi");
        functions.add("Esci");
        functions.add(ImmaginiGioco.getCopyright());
        functions.add(ImmaginiGioco.getRiconoscimenti());
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point(427, 253));
        items[1] = new Function(func.get(1), new Point(407,293));
        items[2] = new Function(func.get(2), new Point(430,333));
        items[3] = new Function(func.get(3), new Point(333,393));
        items[4] = new Function(func.get(4), new Point(518,393));
        items[5] = new Function(func.get(5), new Point(244,373));
        items[6] = new Function(func.get(6), new Point(625,373));

        return items;
    }

    public Object select(Point mouseLocation) {
        for(Function item : mainScreenFunctions) {
            Dimension dimension = item.getDimension();
            Point location = item.getLocation();
            boolean inX= location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
            boolean inY;
            if(item.getObject() instanceof String) inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
            else inY = location.y <= mouseLocation.y && location.y + dimension.height >= mouseLocation.y;
            if(inX && inY){
                return item.getObject();
            }
        }
        return null;
    }
}