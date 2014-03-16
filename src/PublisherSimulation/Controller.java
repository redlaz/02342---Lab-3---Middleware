package PublisherSimulation;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import Logger.Log;
import Middleware.Middleware;
import Middleware.Enums.EventType;
import Middleware.Interfaces.IMiddleware;
import Middleware.Models.Test;


public class Controller implements IMiddleware
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
			
			middleware.publishEvent(55);
			middleware.publishEvent(new Test());
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
	public void handleEvent(Object object) 
	{
		System.out.println("EVENT ANKOMMET: " + object.toString());
	}
}