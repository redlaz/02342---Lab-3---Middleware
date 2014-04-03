package Middleware.Network.TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpListener extends Thread
{
	private ServerSocket listener;
	private int port = 4446;
	private boolean isBootPeer = false;
	
	public void setIsBootPeer(boolean isBootPeer)
	{
		this.isBootPeer = isBootPeer;
	}
	
	@Override
	public void run()
	{		
		try 
		{
			// Boot node listens to port 4446
			if (this.isBootPeer)
				listener = new ServerSocket(port);

			// Regular node gets random tcp port
			else
				listener = new ServerSocket(0);

			while(true)
			{
				if (listener != null)
				{
					Socket newServer = listener.accept();
					TcpReceiver server = new TcpReceiver(newServer);
					server.start();
				}
			}
		} 
		
		catch (IOException e) 
		{
			System.out.println("Listener2: " + e.getMessage());
		}
	}
}
