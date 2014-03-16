package Middleware.Exceptions;

public class MiddlewareNotStartedException extends Exception
{
	public MiddlewareNotStartedException() {}

    public MiddlewareNotStartedException(String message)
    {
       super(message);
    }
}

