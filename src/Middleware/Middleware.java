package Middleware;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;
import Middleware.Interfaces.IP2P;
import Middleware.Network.Node;

public class Middleware implements IP2P
{
	private Node node;
	
	public Middleware ()
	{
		this.node = new Node();
	}
	
	@Override
	public void subscribe(Class<?> classType)throws MiddlewareNotStartedException 
	{

	}

	@Override
	public void publish(Object object) throws MiddlewareNotStartedException 
	{

	}

	@Override
	public void start(IMiddleware caller) throws MiddlewareIOException 
	{
		node.joinChordRing();
	}

	@Override
	public void stop() 
	{
		
	}

	@Override
	public void activateDebugPrints(boolean flag) throws MiddlewareNotStartedException 
	{

	}
}
