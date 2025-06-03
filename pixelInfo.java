import java.util.*;
import java.util.ArrayList;

public class pixelInfo {
    private ArrayList<Pixel> pixelList;

    public pixelInfo(){
        pixelList = new ArrayList<>();
    }

    public void addPixel(Pixel pixel){
        pixelList.add(pixel);
    }

    public Pixel getPixel(int pixelNo){
        return pixelList.get(pixelNo);
    }

    public void removePixel(Pixel pixel){
        pixelList.remove(pixel);
    }
    public List<Pixel> getPixelLists() {
        return pixelList;
    }
    
    public int getSize() {
        return pixelList.size();
    }
}