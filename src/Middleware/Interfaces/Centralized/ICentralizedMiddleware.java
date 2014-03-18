package Middleware.Interfaces.Centralized;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;

public interface ICentralizedMiddleware 
{
	void subscribe(Class<?> classType) throws MiddlewareNotStartedException;
	void publish(Object object) throws MiddlewareNotStartedException;
	void startServer(IMiddleware caller) throws MiddlewareIOException;
	
	void stop();
	void activateDebugPrints(boolean flag) throws MiddlewareNotStartedException;
}
