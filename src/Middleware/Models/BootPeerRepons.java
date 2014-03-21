package Middleware.Models;

import java.net.InetAddress;

public class BootPeerRepons 
{
	private InetAddress ip;
	private long guid;
	
	public BootPeerRepons(InetAddress ip, long guid) 
	{
		this.ip = ip;
		this.guid = guid;
	}

	public InetAddress getIp() 
	{
		return ip;
	}
	
	public long getGuid() 
	{
		return guid;
	}
	
	
}
