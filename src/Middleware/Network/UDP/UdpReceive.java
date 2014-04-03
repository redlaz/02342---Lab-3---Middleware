package Middleware.Network.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Middleware.Enums.MessageType;
import Middleware.Util.Serializer;



public class UdpReceive 
{
	private int port = 4446;
	private BlockingQueue<DatagramPacket> inQueue;
	private BlockingQueue<DatagramPacket> outQueue;
	
	public UdpReceive()
	{
		this.inQueue = new ArrayBlockingQueue<DatagramPacket>(10000);
		this.outQueue = new ArrayBlockingQueue<DatagramPacket>(10000);
	}
	
	public void startBootNodeService()
	{
		new receive(inQueue).start();
		new inHandler(inQueue, outQueue).start();
		new send(outQueue).start();
		System.out.println("Boot peer service started");
	}
	
	private class send extends Thread 
	{
		private BlockingQueue<DatagramPacket> outQueue;
		
		public send(BlockingQueue<DatagramPacket> outQueue)
		{
			this.outQueue = outQueue;
		}
		
		@Override
		public void run()
		{
			try 
			{
				DatagramSocket sendMulticastSocket = new DatagramSocket();
				while (true) 
				{
					DatagramPacket outPacket = this.outQueue.take();
					sendMulticastSocket.send(outPacket);
				}
			} 
			
			catch (InterruptedException | IOException e) 
			{
				System.out.println("send " + e.getMessage());
			}
		}
	}
	
	private class receive extends Thread 
	{
		private BlockingQueue<DatagramPacket> inQueue;
		private DatagramSocket receiveMulticastSocket;
		
		public receive(BlockingQueue<DatagramPacket> inQueue)
		{
			try 
			{
				this.inQueue = inQueue;
				receiveMulticastSocket = new DatagramSocket(null);
				receiveMulticastSocket.setBroadcast(true);
				receiveMulticastSocket.setReuseAddress(true);
				receiveMulticastSocket.bind(new InetSocketAddress(port));
			} 
			catch (IOException e) 
			{
				System.out.println("receive " + e.getMessage());
			}
		}
		
		@Override
		public void run()
		{
			
			while(true)
			{	
				try 
				{
					byte[] inBuffer = new byte[1500];
					DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
				
					receiveMulticastSocket.receive(inPacket);
					MessageType messageType = (MessageType)Serializer.back(inPacket.getData());

					if (messageType.equals(MessageType.JOIN))
					{
						byte[] outBuffer = Serializer.now(MessageType.GUID);
						DatagramPacket outPacket = new DatagramPacket(outBuffer, outBuffer.length, InetAddress.getByName("192.168.1.198"), inPacket.getPort());
						this.inQueue.add(outPacket);
					}
				} 
				
				catch (IOException | ClassNotFoundException e) 
				{
					System.out.println("receive run" + e.getMessage());
				}
			}
		}
	}
	
	private class inHandler extends Thread 
	{
		private BlockingQueue<DatagramPacket> inQueue;
		private BlockingQueue<DatagramPacket> outQueue;

		public inHandler(BlockingQueue<DatagramPacket> inQueue, BlockingQueue<DatagramPacket> outQueue)
		{
			this.inQueue = inQueue;
			this.outQueue = outQueue;
		}
		
		@Override
		public void run()
		{
			while(true)
			{
				try 
				{
					DatagramPacket inPacket = this.inQueue.take();
					byte[] dataBytes = Serializer.now(MessageType.GUID);
					DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length,inPacket.getAddress(),inPacket.getPort());
					this.outQueue.add(packet);
					System.out.println("Peer "+packet.getSocketAddress()+" requests JOIN");
				} 
				
				catch (InterruptedException  e) 
				{
					System.out.println(e.getMessage());
				}
				
			}
		}
	}
}