import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramSocket;

/**
 * UDP port scanner
 */
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
        for(int i = getLower(); i <= getUpper(); i++) // loop through the range of ports provided
        {
            setProgress(i - getLower());
            if(isOpen(i))
                openCounter++;
        }
        setOpenCounter(openCounter);
        setProgress(getUpper());
        setIsComplete(true);
    }

    /**
     * checks if the provided port is open on the host
     * @param port the port to check
     * @return true if the port is open, false otherwise
     */
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
