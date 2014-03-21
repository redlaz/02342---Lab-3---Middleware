package Middleware;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;
import Middleware.Interfaces.IP2P;
import Middleware.Network.NetworkController;

public class Middleware implements IP2P
{
	private NetworkController networkController;
	
	public Middleware ()
	{
		this.networkController = new NetworkController();
	}
	
	@Override
	public void subscribe(Class<?> classType)
			throws MiddlewareNotStartedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publish(Object object) throws MiddlewareNotStartedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(IMiddleware caller) throws MiddlewareIOException 
	{
		networkController.startPeer();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateDebugPrints(boolean flag)
			throws MiddlewareNotStartedException {
		// TODO Auto-generated method stub
		
	}

}
