import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;



public class Client extends Thread{

	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;

	Message receivedMessage;

	ArrayList<String> Users;
	HashMap<String, ArrayList<String>> chats;
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
		Users = new ArrayList<String>();
		chats = new HashMap<>();
	}
	
	public void run() {
		
		try{
			socketClient= new Socket("127.0.0.1",5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {

			try {
				Message data = new Message();
				data = (Message) in.readObject();
				receivedMessage = data;

				if (data.newUser){
					Users.clear();
                    Users.addAll(data.PrevUsers);
					callback.accept(data.message);
				}
				else if(data.userLeft){

					Users.remove(data.Username);
					chats.remove(data.Username);
					callback.accept(data.message);
				}
				else if(data.messagetoAll){
					callback.accept(data.message);
				}
				else if (data.personalMessage){
					if (chats.containsKey(data.Username)) {
						chats.get(data.Username).add(data.message);

					} else {
						ArrayList<String> newChat = new ArrayList<>();
						newChat.add(data.message);
						chats.put(data.Username, newChat);
					}

				}

			}
			catch(Exception e) {}
		}
	
	}

	public void send(Message data) {

		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
