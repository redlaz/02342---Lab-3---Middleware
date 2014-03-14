package PublisherSimulation;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import Logger.Log;
import Middleware.Middleware;
import Middleware.Interfaces.IMiddleware;


public class Controller implements IMiddleware
{

	public void startTest() 
	{
	
		Middleware middleware = new Middleware();
		
		try 
		{
			middleware.start(this);

			middleware.publishEvent("mit event");
		} 
		
		catch (SocketException e) 
		{
			Log.Output(e.toString(), this);
		} 
		
		catch (UnknownHostException e) 
		{
			Log.Output(e.toString(), this);
		} 
		
		catch (IOException e) 
		{
			Log.Output(e.toString(), this);
		}
	}

	@Override
	public void handleEvent() 
	{
		System.out.println("EVENT ANKOMMET");
	}
}