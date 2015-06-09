import java.io.FileNotFoundException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import java.io.File;

/**
 * simple multi threaded TCP and UDP port scanner
 */
public class PortScannerTool
{
    private Map<Integer, String> commonUDPServices;
    private Map<Integer, String> commonTCPServices;
    private TCPPortScanner tcpScanner;
    private UDPPortScanner udpScanner;
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

    private HashMap<Integer, String> readCommonUDPServices() throws FileNotFoundException
    {
        HashMap<Integer, String> services = new HashMap<>();
        Scanner udpFile = new Scanner(new File("src/UDP_common_services"));
        while(udpFile.hasNext())
            services.put(udpFile.nextInt(), udpFile.next());
        return services;
    }

    private HashMap<Integer, String> readCommonTCPServices() throws FileNotFoundException
    {
        HashMap<Integer, String> services = new HashMap<>();
        Scanner tcpFile = new Scanner(new File("src/TCP_common_services"));
        while(tcpFile.hasNext())
            services.put(tcpFile.nextInt(), tcpFile.next());
        return services;
    }

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
                if(dotCounter++ % 10 == 0)
                    System.out.print("\rTCP[" + tcpScanner.getProgess() + "/" + highest + "] UDP[" + udpScanner.getProgess() + "/" + highest + "]");
                System.out.print(".");
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread sleep failed: " + e.getMessage());
            }
        }
    }

    private void outputResults(InetAddress ipAddr)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("\n========================= SCAN COMPLETE =========================");
        System.out.println("Scan finished at: " + dateFormat.format(new Date()));
        System.out.println("Time taken: " + (System.currentTimeMillis() - start) / 1000.0 + "s");
        System.out.println("Resolved host name: " + ipAddr.getHostName() + " (" + ipAddr.getHostAddress() + ")");
        System.out.println(tcpScanner.getOpenCounter() + " TCP port(s) open\n" + udpScanner.getOpenCounter() + " UDP port(s) open");

        ArrayList<Integer> openUDPPorts = udpScanner.getOpenPorts();
        ArrayList<Integer> openTCPPorts = tcpScanner.getOpenPorts();
        if(udpScanner.getOpenCounter() > 0) // no need to search through map if there was no open ports
        {
            System.out.println("======================\nOpen UDP ports summary\n======================");
            for(Integer i : openUDPPorts)
                if(commonUDPServices.get(i) != null)
                    System.out.println(i + ": " + commonUDPServices.get(i));
        }

        if(tcpScanner.getOpenCounter() > 0)
        {
            System.out.println("======================\nOpen TCP ports summary\n======================");
            for (Integer i : openTCPPorts)
                if (commonTCPServices.get(i) != null)
                    System.out.println(i + ": " + commonTCPServices.get(i));
        }
    }
}
