package Middleware.Network;


import java.net.InetAddress;
import java.util.Hashtable;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.Message;
import Middleware.Models.PeerReference;
import Middleware.Network.TCP.TcpListener;
import Middleware.Network.TCP.TcpRequester;
import Middleware.Network.UDP.UdpRequester;
import Middleware.Network.UDP.UdpResponder;

public class NetworkController 
{
	private boolean isBootPeer = false;
	private UdpRequester seacher;
	private InetAddress bootPeerAddress;
	private UdpResponder responder;
	private TcpListener listener;
	private TcpRequester requester;
	
	public NetworkController()
	{
		this.seacher = new UdpRequester();
		this.responder = new UdpResponder();
		this.listener = new TcpListener();
		this.requester = new TcpRequester();
	}
	
	public void startPeer()
	{
		try 
		{
			bootPeerAddress = seacher.join();
			
			if (bootPeerAddress == null)
			{
				isBootPeer = true;
				System.out.println("No boot peer found.");
			}
			
			else
				System.out.println("Boot peer found at " + bootPeerAddress.getHostName());

			System.out.println("Responder started");
			
			if (isBootPeer)
				responder.start();	

			
			listener.setIsBootPeer(isBootPeer);
			listener.start();
			
			if (!isBootPeer)
			{
				Hashtable<Integer, PeerReference> routingTable = requester.requestRoutingTable(bootPeerAddress);
				System.out.println(routingTable.size());
			}
		} 
		
		catch (MiddlewareIOException e) 
		{
			System.out.println("Controller: " + e.getMessage());
		}
	}
}
