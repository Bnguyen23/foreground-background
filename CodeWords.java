import java.util.*;
import java.util.ArrayList;

public class CodeWords {
    private ArrayList<Code> codeList;

    public CodeWords(){
        codeList = new ArrayList<>();
    }

    public void addCode(Code code){
        codeList.add(0,code); // add at the first position
    }

    public void removeCode(Code code){
        codeList.remove(code);
    }

    public List<Code> getCodeWords() {
        return codeList;
    }

    public int getSize() {
        return codeList.size();
    }
    
}
