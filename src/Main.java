import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            new PortScannerTool(PortScannerTool.getInetAddress("127.0.0.1"), 1, 1000);
        }
        catch (IOException | PortRangeException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
}
