package application.view;
import java.awt.*;
import java.util.ArrayList;

public class LoseScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] loseScreenFunctions;
    public Function[] getScreenFunctions() {
        return loseScreenFunctions;
    }

    public LoseScreen(){
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
        items[0] = new Function(func.getFirst(), new Point(128,438));
        items[1] = new Function(func.getLast(), new Point(622,438));
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

