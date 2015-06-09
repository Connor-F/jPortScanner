import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCPPortScanner extends PortScanner implements Runnable
{
    public TCPPortScanner(InetAddress ipAddr, int lower, int upper)
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
        try(Socket connection = new Socket(super.getInetAddress(), port))
        {
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }
}