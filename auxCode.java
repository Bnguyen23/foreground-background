public class auxCode {

    public double brightMin;
    public double brightMax;
    public int frequency;
    public int runLength;
    public int firstAccess;
    public int lastAccess;
    
    public auxCode(double brightMin, double brightMax,
        int frequency, int runLength, int firstAccess, int lastAccess){
        this.brightMin = brightMin;
        this.brightMax = brightMax;
        this.frequency = frequency;
        this.runLength = runLength;
        this.firstAccess = firstAccess;
        this.lastAccess = lastAccess;
    }  
}
