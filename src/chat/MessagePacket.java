package chat;

import java.io.Serializable;

public class MessagePacket implements Serializable{

	public static final int JOIN = 0;
	public static final int CHAT = 1;
	public static final int QUIT = 2;
	public static final int WHISPHER = 3;
	
	private int protocol;
	private String from;
	private String message;
	
	public MessagePacket(String name) {
		this.from = name;
	}

	public MessagePacket(int protocol, String name, String message) {
		this.protocol = protocol;
		this.from = name;
		this.message = message;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public String getName() {
		return from;
	}

	public void setName(String name) {
		this.from = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
