import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPScanner implements Runnable
{
    private InetAddress ipAddr;
    private int lower;
    private int upper;
    private int tcpCounter;
    private boolean isComplete;

    public TCPScanner(InetAddress ipAddr, int lower, int upper)
    {
        this.ipAddr = ipAddr;
        this.lower = lower;
        this.upper = upper;
        tcpCounter = 0;
        isComplete = false;
    }

    @Override
    public void run()
    {
        for(int i = lower; i <= upper; i++)
            if(isOpen(i))
                tcpCounter++;
        isComplete = true;
    }

    public boolean isOpen(int port)
    {
        try(Socket connection = new Socket(ipAddr, port))
        {
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }

    public int getTcpCounter()
    {
        return tcpCounter;
    }

    public boolean isComplete()
    {
        return isComplete;
    }
}