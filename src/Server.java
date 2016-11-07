import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server
{
	
	private static final int PORT = 7788;
		
	public static void main(String args[])
	{
		DatagramSocket sock = null;
		
		//useful to save before ctrl + c
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				try
				{
					System.out.println("Shutting down ...");
					GPIOUtil.out(1, 0);
					Thread.sleep(200);
					echo("Server Closed.");
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});
		try
		{
			//GPIOUtil.setMode(1, "out");
			//Creating a server socket, parameter is local port number
			sock = new DatagramSocket(PORT);
			//buffer to receive incoming data
			byte[] buffer = new byte[65536];
			DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

			echo("Server socket created. Waiting for incoming data...");
			
			
			
			//communication loop. Loop can be broken using <CTRL> + <C>, or from a client entering "/killserver"
			while (true)
			{
				// Will wait for the UDP socket to get a signal, then receive the data.
				// Hopefully they don't happen at same time? Make sync
				sock.receive(incoming);
				byte[] data = incoming.getData();
				
				// Save data as String.
				String s = new String(data, 0, incoming.getLength());
				echo(s);

				if(s.equals("true"))
				{
					GPIOUtil.out(1, 1);
					String sendBack = "The LED was set ON";
					DatagramPacket dp = new DatagramPacket(sendBack.getBytes(), sendBack.getBytes().length,
							incoming.getAddress(), incoming.getPort());

					sock.send(dp);
				}else if(s.equals("false"))
				{
					GPIOUtil.out(1, 0);
					String sendBack = "The LED was set to OFF";
					DatagramPacket dp = new DatagramPacket(sendBack.getBytes(), sendBack.getBytes().length,
							incoming.getAddress(), incoming.getPort());

					sock.send(dp);
				}
				
			} // End communication loop
		}

		catch (IOException e)
		{
			System.err.println("IOException " + e);
		}

	}

	// Shorter version of Java's Println.
	public static void echo(String msg)
	{
		System.out.println(msg);
	}
}