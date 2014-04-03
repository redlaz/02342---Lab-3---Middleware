package Middleware.Network;


import java.net.InetAddress;
import java.util.Hashtable;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.BootPeerRepons;
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
	private Hashtable<Integer, PeerReference> localRoutingTable;
	
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
			BootPeerRepons bootPeerRespons = seacher.join();
			
			if (bootPeerRespons == null)
			{
				isBootPeer = true;
				System.out.println("No boot peer found.");
			}
			
			else
			{
				System.out.println("Boot peer found at " + bootPeerRespons.getIp().getHostName());
				System.out.println("GUID: " + bootPeerRespons.getGuid());
			}


			if (isBootPeer)
				responder.start();	

			
			listener.setIsBootPeer(isBootPeer);
			listener.start();
			
			if (!isBootPeer)
			{
				Hashtable<Integer, PeerReference> routingTable = requester.requestRoutingTable(bootPeerRespons.getIp());
				System.out.println(routingTable.size());
			}
		} 
		
		catch (MiddlewareIOException e) 
		{
			System.out.println("Controller: " + e.getMessage());
		}
	}
}
