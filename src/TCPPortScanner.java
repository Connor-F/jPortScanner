import java.io.IOException;
import java.net.*;

/**
 * TCP port scanner
 */
public class TCPPortScanner extends PortScanner implements Runnable
{
    /** connection timeout in ms */
    private static final int TIMEOUT = 5000;

    public TCPPortScanner(InetAddress ipAddr, int lower, int upper)
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
            if (isOpen(i))
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
        SocketAddress sockAddr = new InetSocketAddress(getInetAddress(), port); // this way allows us to set a timeout
        Socket socket = new Socket();
        try
        {
            socket.connect(sockAddr, TIMEOUT);
            addOpenPort(port);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}