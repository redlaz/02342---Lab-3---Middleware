package PublisherSimulation;

import Logger.Log;
import Middleware.Middleware;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddlewareEvent;
import Middleware.Models.Test;


public class Controller implements IMiddlewareEvent
{
	public void startTest() 
	{
	
		Middleware middleware = new Middleware();
		
		try 
		{
			middleware.start(this);
		
			middleware.subscribe(String.class);
			middleware.subscribe(Integer.class);
			middleware.subscribe(Test.class);
			
			middleware.publish(55);
			middleware.publish(new Test());
		} 
		
		catch (MiddlewareIOException | MiddlewareNotStartedException e) 
		{
			Log.Output(e.toString(), this);
		} 
	}

	@Override
	public void objectReceivedFromMiddleware(Object object) 
	{
		System.out.println("EVENT ANKOMMET: " + object.toString());
	}
}