package Middleware.Network.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;


import Middleware.Enums.MessageType;
import Middleware.Models.Message;
import Middleware.Models.PeerReference;
import Middleware.Util.Serializer;


public class TcpReceiver extends Thread
{
	private Socket socket;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private static int peerCount = 0;
	private static Hashtable<Integer, PeerReference> routingTable = new Hashtable<>();

	
	public TcpReceiver(Socket server) throws IOException
	{
		this.socket = server;
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		this.dataInputStream = new DataInputStream(socket.getInputStream());
	}
	
	@Override
	public void run()
	{
		System.out.println("TCP responser running at port: " + socket.getLocalPort());
		while(true)
		{
			try 
			{
				
				byte[] message = read(); 
				Object object  = Serializer.back(message);
				
				if (object instanceof Message)
				{
					System.out.println("Data objekt modtaget");
					Message incommingMessage = (Message)object;
					Message outgoingMessage = new Message();
					
					System.out.println(incommingMessage.getType());
					switch (incommingMessage.getType()) 
					{
						case GET_COPY_OF_ROUTING_TABLE:
							outgoingMessage.setMessageType(MessageType.ROUTING_TABLE);
							PeerReference peerReference = new PeerReference(socket.getRemoteSocketAddress(), peerCount);
							routingTable.put(peerCount, peerReference);
							System.out.println(peerCount);
							peerCount++;
							
							outgoingMessage.setPayload(routingTable);
							break;

						default:
							break;
					}
					
					write(outgoingMessage);
				}
				
			} 
			
			catch (IOException | ClassNotFoundException e) 
			{
				System.out.println("Responder: " + e.getMessage());
			}
		}
	}
	
	private byte[] read() throws IOException
	{
		int length = dataInputStream.readInt();
		byte[] message = new byte[length];
		dataInputStream.readFully(message, 0, message.length); 
		return message;
	}
	
	private void write(Object object) throws IOException
	{
		byte[] message = Serializer.now(object);
		dataOutputStream.writeInt(message.length);
		dataOutputStream.write(message);   
		dataOutputStream.flush();
	}
}