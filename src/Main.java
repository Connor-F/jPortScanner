import java.io.IOException;
import java.net.InetAddress;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new PortScannerTool(getInetAddress("127.0.0.1"), 1, 10000);
        }
        catch (IOException | PortRangeException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private static InetAddress getInetAddress(String in) throws IOException
    {
        String[] octets = in.split("\\."); // octets needed so a new InetAddress object can be made for the IP addr
        byte[] addrBytes = new byte[4]; // max 4 bytes in ipv4 address
        for(int i = 0; i < 4; i++)
            addrBytes[i] = (byte) Integer.parseInt(octets[i]);
        return InetAddress.getByAddress(addrBytes);
    }
}
