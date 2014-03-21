package Middleware.Network.TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;


import Middleware.Enums.MessageType;
import Middleware.Models.Message;
import Middleware.Models.PeerReference;
import Middleware.Util.Serializer;


public class TcpResponder extends Thread
{
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private static int peerCount = 0;
	private Hashtable<Integer, PeerReference> routingTable;

	
	public TcpResponder(Socket server) throws IOException
	{
		this.socket = server;
		this.outputStream = socket.getOutputStream();
		this.inputStream = socket.getInputStream();	
		this.routingTable = new Hashtable<>();
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
		DataInputStream dIn = new DataInputStream(socket.getInputStream());
		int length = dIn.readInt();
		byte[] message = new byte[length];
		dIn.readFully(message, 0, message.length); // read the message
		return message;
		
		//byte[] inBytes = new byte[1024];
		//inputStream.read(inBytes);
		//return inBytes;
	}
	
	private void write(Object object) throws IOException
	{
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
		byte[] message = Serializer.now(object);
		System.out.println("Længde: " + message.length);
		dOut.writeInt(message.length); // write length of the message
		dOut.write(message);           // write the message
		
		//byte[] message = Serializer.now(object);
		//outputStream.write(message.length);
		//outputStream.write(message);
		//outputStream.flush();
		
	}
}