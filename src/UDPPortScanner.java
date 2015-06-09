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
        for(int i = super.getLower(); i <= super.getUpper(); i++)
            if(isOpen(i))
                openCounter++;
        super.setOpenCounter(openCounter);
        super.setIsComplete(true);
    }

    public boolean isOpen(int port)
    {
        try(DatagramSocket connection = new DatagramSocket(port, super.getInetAddress()))
        {
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }
}
