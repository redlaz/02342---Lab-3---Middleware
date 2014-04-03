package Middleware.Network;


import java.net.InetAddress;
import java.util.Hashtable;

import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Models.BootNodeRepons;
import Middleware.Models.Message;
import Middleware.Models.PeerReference;
import Middleware.Network.TCP.TcpListener;
import Middleware.Network.TCP.TcpSender;
import Middleware.Network.UDP.UdpSend;
import Middleware.Network.UDP.UdpReceive;

public class Node 
{
	private boolean isBootPeer = false;
	private UdpSend udpSender;
	private InetAddress bootPeerAddress;
	private UdpReceive udpReceiver;
	private TcpListener tcpListener;
	private TcpSender requester;
	private Hashtable<Integer, PeerReference> localRoutingTable;
	
	public Node()
	{
		this.udpSender = new UdpSend();
		this.udpReceiver = new UdpReceive();
		this.tcpListener = new TcpListener();
		this.requester = new TcpSender();
	}
	
	public void joinChordRing() throws MiddlewareIOException
	{
			BootNodeRepons bootPeerRespons = udpSender.findBootNode();
			
			if (bootPeerRespons == null)
			{
				this.isBootPeer = true;
				System.out.println("No boot peer found.");
			}
			
			else
			{
				System.out.println("Boot peer found at " + bootPeerRespons.getIp().getHostName());
				System.out.println("GUID: " + bootPeerRespons.getGuid());
				udpReceiver.startBootNodeService();
			}
			
			tcpListener.setIsBootPeer(isBootPeer);
			tcpListener.start();
			
			if (!isBootPeer)
			{
				Hashtable<Integer, PeerReference> routingTable = requester.requestRoutingTable(bootPeerRespons.getIp());
				System.out.println(routingTable.size());
			}
		} 
	}