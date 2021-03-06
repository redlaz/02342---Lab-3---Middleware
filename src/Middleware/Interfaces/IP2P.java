package Middleware.Interfaces;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;

public interface IP2P 
{
	void subscribe(Class<?> classType) throws MiddlewareNotStartedException;
	void publish(Object object) throws MiddlewareNotStartedException;
	void start(IMiddleware caller) throws MiddlewareIOException;
	void stop();
	void activateDebugPrints(boolean flag) throws MiddlewareNotStartedException;
}
