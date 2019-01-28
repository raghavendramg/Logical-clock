import java.io.*;

import java.net.*;
import java.util.*;
import java.lang.*;

public class SyncMaster {
    
    

    public static void main(String[] args) throws Exception
	{
      
		  Master m=new Master();
          Thread thread1 = new Thread()
		
		 {
         public void run()
		 {
			 try{
				 

                  m.RecieveMsg();	
			 }
			 catch (Exception e)
			 {
				System.out.println(e.getMessage()); 
			 }
         }
        };
		
			
		Thread thread2 = new Thread()
		
		 {
         public void run()
		 {
			 try{
                  m.SendMsg();	
			 }
			 catch (Exception e)
			 {
				 System.out.println(e.getMessage());
			 }
         }
        };
		
	thread1.start();
	thread2.start();





		  
	  
    }
	
}

class Master {
	
	final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888; //To receive messages;
	
	final static int PORT1 = 6666; //To multicast messages;
    
	int timer1= new Random().nextInt(99);
	int nodes=1;
	int offset=0;

	
	
	
	public void RecieveMsg() throws Exception {
		
		System.out.println("Master's current timer is : "+timer1);

		// Get the address that we are going to connect to.
        InetAddress address = InetAddress.getByName(INET_ADDR);
        
        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];
        
        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)){
            //Joint the Multicast group.
            clientSocket.joinGroup(address);
           
            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				
                clientSocket.receive(msgPacket);
                

                String msg = new String(msgPacket.getData());
				String clienttimer_offset="";
				int i=0;
				String process_id="";
				
				for (String retval: msg.split(":")) {
					 if(i==0)
                     clienttimer_offset=retval;
				     else if(i==1)
				     process_id=retval;
				     
					 i++;
                    }
				
			    

				//System.out.println(msg1);
				//System.out.println(msg);

				System.out.println(" A new node is added to the system, and it has sent the offset "+ clienttimer_offset);
				int value=Integer.parseInt(clienttimer_offset);
			    
				int current_client_offset=0;
                nodes++;

				
				offset=value/nodes; //offset to sent to master and existing nodes;
				
				current_client_offset=offset-value; //offset to be sent to the new node
				timer1=timer1+offset;
				Thread.sleep(1000);
				SendMsg(current_client_offset,offset,process_id);
				
                
                

            }
        } catch (Exception ex) {
            System.out.println("Error in receive"+ex.getMessage());
        }
	
	
	
	}
	
	public  void SendMsg(int avg_timer, int offset, String process_id) throws Exception {
        // Get the address that we are going to connect to.
        InetAddress addr = InetAddress.getByName(INET_ADDR);
     
        // Open a new DatagramSocket, which will be used to send the data.
		
		
        try(DatagramSocket serverSocket = new DatagramSocket())  {
			
			
			
			
				
                
                String msg = avg_timer+":"+offset+":"+process_id+":Synchronization";
				

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                   msg.getBytes().length, addr, PORT1);
                serverSocket.send(msgPacket);
     
                System.out.println("The corrected time is  " + timer1);
                
				
            
			
		 
        } catch (Exception ex) {
          System.out.println(ex.getMessage());        }
    }
	
	
	//To send the daemon's time to the nodes;
	public  void SendMsg() throws Exception {
        // Get the address that we are going to connect to.
        InetAddress addr = InetAddress.getByName(INET_ADDR);
     
        // Open a new DatagramSocket, which will be used to send the data.
		
		
        try(DatagramSocket serverSocket = new DatagramSocket())  {
			
			
			
			
				while(true){
                
                String msg = timer1+":master";
				

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                   msg.getBytes().length, addr, PORT1);
                serverSocket.send(msgPacket);
				Thread.sleep(1000);
     
               
                }
				
            
			
		 
        } catch (Exception ex) {
          System.out.println(ex.getMessage());        }
    }
	
	
	
	
	
	
}