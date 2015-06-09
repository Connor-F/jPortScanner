import java.net.*;
import java.util.ArrayList;

public abstract class PortScanner
{
    private InetAddress ipAddr;
    private int lower;
    private int upper;
    private int openCounter;
    private int progess;
    private boolean isComplete;
    private ArrayList<Integer> openPorts;

    public PortScanner(InetAddress ipAddr, int lower, int upper)
    {
        this.ipAddr = ipAddr;
        this.lower = lower;
        this.upper = upper;
        openCounter = 0;
        progess = 0;
        isComplete = false;
        openPorts = new ArrayList<>();
    }

    public abstract boolean isOpen(int port);

    public int getOpenCounter() { return openCounter; }
    public int getLower() { return lower; }
    public int getUpper() { return upper; }
    public boolean getIsComplete() { return isComplete; }
    public InetAddress getInetAddress() { return ipAddr; }
    public ArrayList<Integer> getOpenPorts() { return openPorts; }
    public int getProgess() { return progess; }

    public void setOpenCounter(int count) { openCounter = count; }
    public void setIsComplete(boolean complete) { isComplete = complete; }
    public void addOpenPort(int port) { openPorts.add(port);}
    public void setProgess(int currentPort) { progess = currentPort; }
}
