import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

/**
 * simple multi threaded TCP and UDP port scanner
 */
public class PortScanner
{
    /** lowest port number provided */
    private int lowest;
    /** highest port number provided */
    private int highest;
    /** InetAddress of the host we want to port scan */
    private InetAddress ipAddr;

    public PortScanner(InetAddress ipAddr, int lowest, int highest) throws PortRangeException
    {
        if(lowest > highest)
            throw new PortRangeException("The lower bound must be less than or equal to the upper bound");
        if(lowest > 65535 || highest > 65535 || lowest < 0 || highest < 0)
            throw new PortRangeException("Port numbers must be between 0 and 65535 (inclusive)");

        this.lowest = lowest;
        this.highest = highest;
        this.ipAddr = ipAddr;
        scanPorts();
    }

    // todo: add map of ports to common services for a more informative output
    private void scanPorts()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.print("Scan started at: " + dateFormat.format(new Date()));

        long start = System.currentTimeMillis();

        TCPScanner tcpScanner = new TCPScanner(ipAddr, lowest, highest);
        UDPScanner udpScanner = new UDPScanner(ipAddr, lowest, highest);
        new Thread(tcpScanner).start();
        new Thread(udpScanner).start();

        int dotCounter = 0; // allows for simple progress monitor
        while(!tcpScanner.isComplete() || !udpScanner.isComplete()) // block until both scanners have finished scanning
        {
            try
            {
                Thread.sleep(300);
                if(dotCounter++ % 40 == 0)
                    System.out.println();
                System.out.print(".");
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }


        System.out.println("\n========================= SCAN COMPLETE =========================");
        System.out.println("Scan finished at: " + dateFormat.format(new Date()));
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
        System.out.println("Resolved host name: " + ipAddr.getHostName() + " (" + ipAddr.getHostAddress() + ")");
        System.out.println(tcpScanner.getTcpCounter() + " TCP port(s) open\n" + udpScanner.getUdpCounter() + " UDP port(s) open");
    }
}
