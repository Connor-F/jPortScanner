import java.net.*;
import java.util.ArrayList;

public abstract class PortScanner
{
    /** the InetAddress of the host we want to scan */
    private InetAddress ipAddr;
    /** the lowest port number provided */
    private int lower;
    /** the highest port number provided */
    private int upper;
    /** the number of open ports detected */
    private int openCounter;
    /** the number of ports scanned so far */
    private int progress;
    /** used to check if the scan has finished */
    private boolean isComplete;
    /** list of all the open ports */
    private ArrayList<Integer> openPorts;

    public PortScanner(InetAddress ipAddr, int lower, int upper)
    {
        this.ipAddr = ipAddr;
        this.lower = lower;
        this.upper = upper;
        openCounter = 0;
        progress = 0;
        isComplete = false;
        openPorts = new ArrayList<>();
    }

    public abstract boolean isOpen(int port);

    /* getters */
    public int getOpenCounter() { return openCounter; }
    public int getLower() { return lower; }
    public int getUpper() { return upper; }
    public boolean getIsComplete() { return isComplete; }
    public InetAddress getInetAddress() { return ipAddr; }
    public ArrayList<Integer> getOpenPorts() { return openPorts; }
    public int getProgress() { return progress; }

    /* setters */
    public void setOpenCounter(int count) { openCounter = count; }
    public void setIsComplete(boolean complete) { isComplete = complete; }
    public void addOpenPort(int port) { openPorts.add(port);}
    public void setProgress(int currentPort) { progress = currentPort; }
}
