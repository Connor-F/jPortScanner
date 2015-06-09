import java.net.*;

public abstract class PortScanner
{
    private InetAddress ipAddr;
    private int lower;
    private int upper;
    private int openCounter;
    private boolean isComplete;

    public PortScanner(InetAddress ipAddr, int lower, int upper)
    {
        this.ipAddr = ipAddr;
        this.lower = lower;
        this.upper = upper;
        openCounter = 0;
        isComplete = false;
    }

    public abstract boolean isOpen(int port);

    public int getOpenCounter() { return openCounter; }
    public int getLower() { return lower; }
    public int getUpper() { return upper; }
    public boolean getIsComplete() { return isComplete; }
    public InetAddress getInetAddress() { return ipAddr; }

    public void setOpenCounter(int count) { openCounter = count; }
    public void setIsComplete(boolean complete) { isComplete = complete; }
}
