import java.io.IOException;
import java.net.*;

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

    public PortScanner(InetAddress ipAddr, int lowest, int highest)
    {
        this.lowest = lowest;
        this.highest = highest;
        this.ipAddr = ipAddr;
        scanPorts();
    }

    // todo: range checks on port numbers
    // todo: provide host name & ip address at end of scan
    // todo: add map of ports to common services for a more informative output
    // todo: add threading for udp and tcp scanning
    // todo: improve % complete output
    private void scanPorts()
    {
        long start = System.currentTimeMillis();
        int tcp = 0; // number of accepted TCP connections
        int udp = 0; // number of accepted UDP connections

        for(int i = lowest; i <= highest; i++)
        {
            double onePercent = (highest - lowest) / 100.0;
            int percentComplete = (int)Math.floor(i / onePercent);
            System.out.println(percentComplete + "% complete");

            try(Socket connection = new Socket(ipAddr, i)) // TCP connection
            {
                System.out.println("[TCP] Accepted at port: " + i);
                tcp++;
            }
            catch(IOException ioe)
            {
                //System.out.println("[TCP] Refused at port: " + i);
                //
            }

            try(DatagramSocket connection = new DatagramSocket(i, ipAddr)) // UDP connection
            {
                System.out.println("[UDP] Accepted at port: " + i);
                udp++;
            }
            catch(SocketException se)
            {
                //System.out.println("[UDP] Refused at port: " + i);
            }
        }

        System.out.println("\n========================= SCAN COMPLETE =========================");
        System.out.println("Resolved host name: " + ipAddr.getHostName() + " (" + ipAddr.getHostAddress() + ")");
        System.out.println(tcp + " TCP port(s) open\n" + udp + " UDP port(s) open");
        System.out.println("Port scan took " + (System.currentTimeMillis() - start) / 1000.0 + "s");
    }
}
