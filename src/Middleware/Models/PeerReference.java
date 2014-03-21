package Middleware.Models;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;

public class PeerReference implements Serializable
{
	private SocketAddress ipAndPort;
	private int guid;
	
	public PeerReference(SocketAddress ipAndPort, int guid) 
	{
		this.ipAndPort = ipAndPort;
		this.guid = guid;
	}

	
	public int getGuid() 
	{
		return guid;
	}
}
