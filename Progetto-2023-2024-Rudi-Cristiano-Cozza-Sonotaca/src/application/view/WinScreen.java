package application.view;
import java.awt.*;
import java.util.ArrayList;

public class WinScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] winScreenFunctions;
    public Function[] getScreenFunctions() {
        return winScreenFunctions;
    }

    public WinScreen(){
        getFunctions();
        this.winScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Menu Principale");
        functions.add("Livello Successivo");
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point(128,438));
        items[1] = new Function(func.getLast(), new Point(569,438));
        return items;
    }

    public Object select(Point mouseLocation,int liv) {
        if(liv!=3) {
            for (Function item : winScreenFunctions) {
                Dimension dimension = item.getDimension();
                Point location = item.getLocation();
                boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
                boolean inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
                if (inX && inY) {
                    return item.getObject();
                }
            }
        } else {
            Dimension dimension = winScreenFunctions[0].getDimension();
            Point location = winScreenFunctions[0].getLocation();
            boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
            boolean inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
            if (inX && inY) {
                return winScreenFunctions[0].getObject();
            }
        }
        return null;
    }
}
