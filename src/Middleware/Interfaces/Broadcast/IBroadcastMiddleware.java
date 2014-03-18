package Middleware.Interfaces.Broadcast;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;

public interface IBroadcastMiddleware 
{
	void subscribe(Class<?> classType) throws MiddlewareNotStartedException;
	void publish(Object object) throws MiddlewareNotStartedException;
	void start(IMiddleware caller) throws MiddlewareIOException;
	void stop();
	void activateDebugPrints(boolean flag) throws MiddlewareNotStartedException;
}
