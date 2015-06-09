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
        for(int i = getLower(); i <= getUpper(); i++)
        {
            setProgess(i - getLower());
            if (isOpen(i))
                openCounter++;
        }
        setOpenCounter(openCounter);
        setProgess(getUpper());
        setIsComplete(true);
    }

    public boolean isOpen(int port)
    {
        try(Socket connection = new Socket(getInetAddress(), port))
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