import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;


import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

	int count = 1;

	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	ArrayList<String> Usernames = new ArrayList<>();
	HashMap<String, ClientThread> clientmap;

	TheServer server;
	private Consumer<Serializable> callback;

	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		clientmap = new HashMap<>();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);)
			{
		    	System.out.println("Server is waiting for a client!");

				while(true) {

					ClientThread c = new ClientThread(mysocket.accept(), count);
					c.start();
					clients.add(c);
					count++;
				}
			}
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}
	}

	class ClientThread extends Thread{


		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;

		String name;
		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;
		}

		public void updateClients(Message data) {
            for (ClientThread t : clients) {
                try {

					t.out.writeObject(data);

                } catch (Exception e) {
                }
            }
		}

		public boolean checkduplicateUsername(String name){

            for (String Username : Usernames) {
                if (Objects.equals(Username, name)) {
                    return false;
                }
            }
			return true;

		}

		public void run(){

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			 while(true) {
				try {

					Message data = new Message();
					data = (Message) in.readObject();

					if (data.login) {
						if (checkduplicateUsername(data.message)) {
							Usernames.add(data.message);

							data.validName = true;
							data.newUser = true;
							data.Username = data.message;

							name = data.message;
							data.message = "New member joined: " + data.Username;

							data.PrevUsers = new ArrayList<>(Usernames);

							clientmap.put(name, this);
							updateClients(data);
							callback.accept("client #" + count + " " + data.message + " connected");
						}
						else{
							data.validName = false;
							data.newUser = false;
							out.writeObject(data);
						}

					} else if (data.messagetoAll) {
						callback.accept("client#" + count + " " + data.Username + " sent: " + data.message);
						String messageUpdate = data.Username + ": " + data.message;
						data.message = messageUpdate;
						updateClients(data);
					}
					else if(data.personalMessage){

						ClientThread t = clientmap.get(data.sendPerson);
						if (t != null) {
							t.out.writeObject(data);
						}
						callback.accept(data.Username + " Sending Private Chat to " + data.sendPerson);
					}
				}
				catch(Exception e) {

					callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					Message data = new Message();
					data.Username = name;
					data.message = name + " has left the Chat";
					data.newUser = false;
					data.userLeft = true;
					clients.remove(this);
					data.PrevUsers = new ArrayList<>(Usernames);

					updateClients(data);

					Usernames.remove(name);
					clientmap.remove(name, this);

					break;
				}
			 }
		}
	}
}


	
	

	
