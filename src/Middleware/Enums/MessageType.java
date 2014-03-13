package Middleware.Enums;

public enum MessageType 
{
	DISCOVER ((byte)0), PUBLISHMENT((byte)1),EVENT((byte)2),DISCOVERY_REPONSE((byte)3),NOTIFICATION((byte)4);
	
	private final byte value;

    private MessageType(byte value)
    {
        this.value = value;
    }

    
    public byte getByte()
    {
    	return value;
    }
    
    public static  MessageType fromByte(byte byteValue)
    {
   
    	switch (byteValue) 
    	{
			case 0:
				return MessageType.DISCOVER;
				
			case 1:
				return MessageType.PUBLISHMENT;
				
			case 2:
				return MessageType.EVENT;
				
			case 3:
				return MessageType.DISCOVERY_REPONSE;
				
			case 4:
				return MessageType.NOTIFICATION;
	
			default:
				return null;
		}
    }
}



    