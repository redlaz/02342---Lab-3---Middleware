package PublisherSimulation;

import Middleware.Middleware;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;
import Middleware.Interfaces.Broadcast.IBroadcastMiddleware;

public class Controller implements IMiddleware
{
	public void startTest() 
	{
		try 
		{
			IBroadcastMiddleware middleware = new Middleware();
			middleware.start(this);
			middleware.activateDebugPrints(false);
			
			middleware.subscribe(String.class);
			middleware.subscribe(Integer.class);
			middleware.subscribe(Test.class);
			
			middleware.publish(55);
			middleware.publish(new Test());
			
			try {Thread.sleep(100);} catch (InterruptedException e) {}
			System.out.println("stop");
			middleware.stop();
			
			Thread.interrupted();
		} 
		
		catch (MiddlewareIOException | MiddlewareNotStartedException e) 
		{
			System.out.println(e.getMessage());
		} 
	}

	@Override
	public void objectReceived(Object object) 
	{
		System.out.print("Event received ");
		
		if (object instanceof String)
			System.out.print("(String)" );
		
		else if (object instanceof Integer)
			System.out.print("(Integer)" );
		
		else if (object instanceof Test)
			System.out.print("(Test)");
		
		System.out.println(": " + object.toString());	
	}
}