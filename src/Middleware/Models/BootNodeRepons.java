package Middleware.Models;

import java.net.InetAddress;

public class BootNodeRepons 
{
	private InetAddress ip;
	private long guid;
	
	public BootNodeRepons(InetAddress ip, long guid) 
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
