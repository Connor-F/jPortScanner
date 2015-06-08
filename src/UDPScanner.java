import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDPScanner implements Runnable
{
    private InetAddress ipAddr;
    private int lower;
    private int upper;
    private int udpCounter;
    private boolean isComplete;

    public UDPScanner(InetAddress ipAddr, int lower, int upper)
    {
        this.ipAddr = ipAddr;
        this.lower = lower;
        this.upper = upper;
        udpCounter = 0;
        isComplete = false;
    }

    @Override
    public void run()
    {
        for(int i = lower; i <= upper; i++)
            if(isOpen(i))
                udpCounter++;
        isComplete = true;
    }

    public boolean isOpen(int port)
    {
        try(DatagramSocket connection = new DatagramSocket(port, ipAddr))
        {
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }

    public int getUdpCounter()
    {
        return udpCounter;
    }

    public boolean isComplete()
    {
        return isComplete;
    }
}
