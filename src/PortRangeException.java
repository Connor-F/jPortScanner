public class PortRangeException extends Exception
{
    private String msg;

    public PortRangeException(String msg)
    {
        this.msg = msg;
    }

    @Override
    public String getMessage()
    {
        return this.msg;
    }
}
