import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDPPortScanner extends PortScanner implements Runnable
{
    public UDPPortScanner(InetAddress ipAddr, int lower, int upper)
    {
        super(ipAddr, lower, upper);
    }

    @Override
    public void run()
    {
        int openCounter = 0;
        for(int i = getLower(); i <= getUpper(); i++)
        {
            setProgess(i - getLower());
            if(isOpen(i))
                openCounter++;
        }
        setOpenCounter(openCounter);
        setProgess(getUpper());
        setIsComplete(true);
    }

    public boolean isOpen(int port)
    {
        try(DatagramSocket connection = new DatagramSocket(port, getInetAddress()))
        {
            addOpenPort(port);
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }
}
