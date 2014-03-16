package Middleware.Exceptions;

public class MiddlewareIOException extends Exception
{
    public MiddlewareIOException() {}

    public MiddlewareIOException(String message)
    {
       super(message);
    }
}
