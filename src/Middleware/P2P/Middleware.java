package Middleware.P2P;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;
import Middleware.P2P.Controllers.Controller;
import Middleware.P2P.Interfaces.IP2P;

public class Middleware implements IP2P
{
	private Controller controller;
	
	public Middleware ()
	{
		this.controller = new Controller();
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
		controller.startPeer();
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
