import java.io.*;

import java.net.*;
import java.util.*;
import java.lang.*;




public class SyncNode{
	public static int master=0;
	 public static void main(String args[]) throws Exception
    {
		
		 
	
    Client c=new Client();
	
    c.RecieveMsg_mastertime();
	Thread thread1 = new Thread()
		
		 {
         public void run()
		 {
			 try{
				 

                  c.RecieveMsg();	
			 }
			 catch (Exception e)
			 {
				System.out.println(e.getMessage()); 
			 }
         }
        };
		
		c.SendMsg();	
		/*Thread thread2 = new Thread()
		
		 {
         public void run()
		 {
			 try{
                  c.SendMsg();	
			 }
			 catch (Exception e)
			 {
				 System.out.println(e.getMessage());
			 }
         }
        };*/
		
	thread1.start();
	//thread2.start();

   
    

	}
	
	
	
	
}





class Client {
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;//To send messages;
	    final static int PORT1 = 6666;//To receive messages;
		int offset=0;

	int timer= new Random().nextInt(99);
	
	int process_id=new Random().nextInt(9999)*timer;
	
	int daemon_timer=0;
    

    public void RecieveMsg() throws Exception {
        // Get the address that we are going to connect to.
        InetAddress address = InetAddress.getByName(INET_ADDR);
        
        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];
        
        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(PORT1)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);
           
            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				
                clientSocket.receive(msgPacket);
				
                

                String msg = new String(buf, 0, buf.length);
                String search=""+process_id;
				String search1="master";
				String newtimer="";
				if(msg.toLowerCase().indexOf(search.toLowerCase()) != -1 && msg.toLowerCase().indexOf(search1.toLowerCase()) == -1)
				{
				 int i=0;
				 for (String retval: msg.split(":")) {
					 if(i==0)
                     {
                     newtimer=retval;
					 offset=Integer.parseInt(newtimer);
					 }
				      i++;
                    }
					
					System.out.println("The new offset for this node sent from master is  "+offset);
					timer=timer+offset;
                    System.out.println("The new timer value calculated from the offset sent from master is : "+timer);
				}
				
				else if (msg.toLowerCase().indexOf(search.toLowerCase()) == -1 && msg.toLowerCase().indexOf(search1.toLowerCase()) == -1) 
				{
				  int i=0;
				  for (String retval: msg.split(":")) {
					 if(i==1)
					 {
                     newtimer=retval;
					 offset=Integer.parseInt(newtimer);
					 }
				      i++;
                    }	
					System.out.println("The new offset for this node is "+offset);
					timer=timer+offset;
                    System.out.println("The new timer value calculated from the offset sent from master is : "+timer);
				}
                
				

            }
        } catch (Exception ex) {
            System.out.println("Error in receive"+ex.getMessage());
        }
	 }
		
		
		
		
		public  void SendMsg() throws Exception {
        // Get the address that we are going to connect to.
        InetAddress addr = InetAddress.getByName(INET_ADDR);
     
        // Open a new DatagramSocket, which will be used to send the data.
		
		
        try(DatagramSocket serverSocket = new DatagramSocket())  {
			
			
			
			//while(true){
				
                offset=timer-daemon_timer;
                String msg = offset+":"+process_id+":Client timer";
				

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                   msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);
     
                System.out.println("Node's timer value is : "+timer+" and The offset value sent to master for calculating average offset is  " + offset);
                
				
            
			
		 /* }*/
        } catch (Exception ex) {
          System.out.println(ex.getMessage());        }
    }
	
	//To receive the daemon's timer;
	public void RecieveMsg_mastertime() throws Exception {
        // Get the address that we are going to connect to.
        InetAddress address = InetAddress.getByName(INET_ADDR);
        
        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];
        
        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(PORT1)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);
           
            
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				
                clientSocket.receive(msgPacket);
				
                

                String msg = new String(buf, 0, buf.length);
                String search="master";
				
				if(msg.toLowerCase().indexOf(search.toLowerCase()) != -1)
				{
				 int i=0;
				 for (String retval: msg.split(":")) {
					 if(i==0)
                     {
                    
					 daemon_timer=Integer.parseInt(retval);
					 }
				      i++;
                    }
					
					System.out.println("The master's timer value is  "+daemon_timer);
				}
				
				
                
				

            
        } catch (Exception ex) {
            System.out.println("Error in receive"+ex.getMessage());
        }
	 }
	
	
	
	
    
}
