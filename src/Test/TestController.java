package Test;

import Middleware.Middleware;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Interfaces.IMiddleware;
import Middleware.Interfaces.IP2P;

public class TestController implements IMiddleware
{
	public void start()
	{
		Middleware middleware = new Middleware();
		
		try 
		{
			middleware.start(this);
		} 
		
		catch (MiddlewareIOException e) 
		
		{
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void objectReceived(Object object) 
	{
	
	}
}
