package Middleware.Controllers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.BlockingQueue;

import Logger.Log;
import Middleware.Models.Data;
import Middleware.Util.Serializer;

public class PacketReceiver extends Thread
{
	private MulticastSocket socket;
	private BlockingQueue<Data> ingoingPacketQueue;
	private int port = 4446;
	
	public PacketReceiver(BlockingQueue<Data> ingoingPacketQueue) throws IOException
	{
		this.ingoingPacketQueue = ingoingPacketQueue;
		socket = new MulticastSocket(port);
		socket.joinGroup(InetAddress.getByName("230.0.0.1"));		
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			try 
			{
				DatagramPacket inputPacket = receiveDatagramPacket();
				Data data = (Data) Serializer.back(inputPacket.getData());
				ingoingPacketQueue.add(data);	
				System.out.println("In packet queued");
			} 
			
			catch (IOException | ClassNotFoundException e) 
			{
				Log.Output(e.toString(), this);
			}
		}
	}
	
	private DatagramPacket receiveDatagramPacket() throws IOException
	{
		byte[] inputBuffer = new byte[1500];
		DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);	
		socket.receive(inputPacket);
		return inputPacket;
	}
}
