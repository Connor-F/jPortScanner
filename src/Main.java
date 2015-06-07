import java.io.IOException;
import java.net.InetAddress;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new PortScanner(getInetAddress("213.206.113.225"), 1, 1000);
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
