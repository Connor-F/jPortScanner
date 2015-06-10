import java.net.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import java.io.*;

/**
 * simple multi threaded TCP and UDP port scanner
 */
public class PortScannerTool
{
    /** port to service name mappings for UDP services */
    private Map<Integer, String> commonUDPServices;
    /** port to service name mappings for TCP services */
    private Map<Integer, String> commonTCPServices;
    /** the TCP port scanner itself */
    private TCPPortScanner tcpScanner;
    /** the UDP port scanner itself */
    private UDPPortScanner udpScanner;
    /** stores the time the scan started (currentTimeMillis()) */
    private long start;

    public PortScannerTool(InetAddress ipAddr, int lowest, int highest) throws PortRangeException, FileNotFoundException
    {
        if(lowest > highest)
            throw new PortRangeException("The lower bound must be less than or equal to the upper bound");
        if(lowest > 65535 || highest > 65535 || lowest < 0 || highest < 0)
            throw new PortRangeException("Port numbers must be between 0 and 65535 (inclusive)");

        commonUDPServices = readCommonUDPServices();
        commonTCPServices = readCommonTCPServices();
        scanPorts(ipAddr, lowest, highest);
        outputResults(ipAddr);
    }

    /**
     * reads in the UDP_common_services file and populates a hashmap containing all the port to service mappings
     * from the file
     * @return hashmap will all the defined port to services mappings for UDP
     * @throws FileNotFoundException when UDP_common_services file cannot be found
     */
    private HashMap<Integer, String> readCommonUDPServices() throws FileNotFoundException
    {
        HashMap<Integer, String> services = new HashMap<>();
        Scanner udpFile = new Scanner(new File("src/UDP_common_services"));
        while(udpFile.hasNext())
            services.put(udpFile.nextInt(), udpFile.next());
        return services;
    }

    /**
     * reads in the TCP_common_services file and populates a hashmap containing all the port to service mappings
     * from the file
     * @return hashmap will all the defined port to services mappings for TCP
     * @throws FileNotFoundException when TCP_common_services file cannot be found
     */
    private HashMap<Integer, String> readCommonTCPServices() throws FileNotFoundException
    {
        HashMap<Integer, String> services = new HashMap<>();
        Scanner tcpFile = new Scanner(new File("src/TCP_common_services"));
        while(tcpFile.hasNext())
            services.put(tcpFile.nextInt(), tcpFile.next());
        return services;
    }

    /**
     * starts each port scanner running and blocks until they both complete
     * @param ipAddr the InetAddress of the host we want to scan
     * @param lowest the smallest port number provided
     * @param highest the largest port number provided
     */
    private void scanPorts(InetAddress ipAddr, int lowest, int highest)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("Scan started at: " + dateFormat.format(new Date()));
        start = System.currentTimeMillis();

        tcpScanner = new TCPPortScanner(ipAddr, lowest, highest);
        udpScanner = new UDPPortScanner(ipAddr, lowest, highest);
        new Thread(tcpScanner).start();
        new Thread(udpScanner).start();

        int dotCounter = 0; // allows for simple progress monitor
        while(!tcpScanner.getIsComplete() || !udpScanner.getIsComplete()) // block until both scanners have finished scanning
        {
            try
            {
                Thread.sleep(150);
                if(dotCounter++ % 10 == 0) // just for simple progress indicator
                    System.out.print("\rProgress: TCP[" + tcpScanner.getProgress() + "/" + highest + "] UDP[" + udpScanner.getProgress() + "/" + highest + "]");
                System.out.print(".");
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread sleep failed: " + e.getMessage());
            }
        }
        System.out.print("\rProgress: TCP[" + tcpScanner.getProgress() + "/" + highest + "] UDP[" + udpScanner.getProgress() + "/" + highest + "]");
    }

    /**
     * prints out the results of the scan. Including: the time taken, resolved host name, the number of TCP and UDP ports
     * open, a list of the open ports and the services that run on these ports
     * @param ipAddr the InetAddress of the host we scanned
     */
    private void outputResults(InetAddress ipAddr)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("\n========================= SCAN COMPLETE =========================");
        System.out.println("- Scan finished at: " + dateFormat.format(new Date()));
        System.out.println("- Time taken: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
        System.out.println("- Resolved host name: " + ipAddr.getHostName() + " (" + ipAddr.getHostAddress() + ")");
        System.out.println(tcpScanner.getOpenCounter() + " TCP port(s) open\n" + udpScanner.getOpenCounter() + " UDP port(s) open");

        ArrayList<Integer> openUDPPorts = udpScanner.getOpenPorts();
        ArrayList<Integer> openTCPPorts = tcpScanner.getOpenPorts();
        if(udpScanner.getOpenCounter() > 0) // no need to search through map if there was no open ports
        {
            System.out.println("======================\nOpen UDP ports summary\n======================");
            for(Integer i : openUDPPorts)
            {
                if (commonUDPServices.get(i) != null)
                    System.out.println(i + ": " + commonUDPServices.get(i));
                else
                    System.out.println(i + ": <unknown>");
            }
        }

        if(tcpScanner.getOpenCounter() > 0)
        {
            System.out.println("======================\nOpen TCP ports summary\n======================");
            for (Integer i : openTCPPorts)
            {
                if (commonTCPServices.get(i) != null)
                    System.out.println(i + ": " + commonTCPServices.get(i));
                else
                    System.out.println(i + ": <unknown>");
            }
        }
    }

    /**
     * allows a String IP address to be given and then converted into a InetAddress (which the Tool uses)
     * @param in the IP address of the host we want to scan
     * @return the InetAddress of the host we want to scan
     * @throws IOException thrown if something goes wrong when creating the InetAddress (string provided invalid)
     */
    public static InetAddress getInetAddress(String in) throws IOException
    {
        String[] octets = in.split("\\."); // octets needed so a new InetAddress object can be made for the IP addr
        byte[] addrBytes = new byte[4]; // max 4 bytes in ipv4 address
        for(int i = 0; i < 4; i++)
            addrBytes[i] = (byte) Integer.parseInt(octets[i]);
        return InetAddress.getByAddress(addrBytes);
    }
}
