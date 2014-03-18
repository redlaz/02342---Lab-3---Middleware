package Middleware.P2P.Controllers;

import java.io.IOException;
import java.net.InetAddress;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.P2P.Controllers.UDP.BootPeerFinder;
import Middleware.P2P.Controllers.UDP.BootPeerService;

public class Controller 
{
	public void startPeer()
	{
		BootPeerFinder bootPeerFinder = new BootPeerFinder();
		
		try 
		{
			InetAddress bootPeer = bootPeerFinder.go();
			
			if (bootPeer == null)
			{
				System.out.println("No boot peer");
				BootPeerService bootPeerService = new BootPeerService();
				bootPeerService.start();
			}
			
			else
			{
				System.out.println("Boot peer found at " + bootPeer.getHostName());
			}
		} 
		
		catch (MiddlewareIOException e) 
		{
			System.out.println(e.getMessage());
		}
	}
}
