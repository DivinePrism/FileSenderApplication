import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TheServer {


	public static void main(String[] args) {
		try {
			//change to your own port
			ServerSocket server_socket = new ServerSocket(6000);

			while(true) {
				new Thread(new ClientWorker(server_socket.accept())).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class ClientWorker implements Runnable{
	private Socket target_socket;
	private DataInputStream din;
	private DataOutputStream dout;
	static int counter = 1;
	public ClientWorker (Socket recv_socket) {
		try {
			target_socket = recv_socket;
			din = new DataInputStream(target_socket.getInputStream());
			dout = new DataOutputStream(target_socket.getOutputStream());

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			byte[] initilize = new byte[1];
			try {
				FileWriter info = null;
				//change to own desktop
				info = new FileWriter("/home/ning/Desktop/info.txt",true);
				din.read(initilize, 0, initilize.length);
				try {				
					String msg = new String(ReadStream(info));
					info.write(msg);
					info.flush();
				}catch(Exception e) {
					e.printStackTrace();
				}

				if(initilize[0] == 2){
					System.out.println(new String(ReadStream(info)));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	private byte[] ReadStream(FileWriter info) {
		
		long start = System.nanoTime();
		long start2 = System.currentTimeMillis();
		byte[] data_buff=null;

		try {
			int b = 0;
			String length_buff="";
			while((b = din.read()) != 4) {
				length_buff +=(char)b;
			}
			int data_length = Integer.parseInt(length_buff);

			data_buff = new byte[Integer.parseInt(length_buff)];
			int byte_read=0;
			int byte_offset=0;
			while(byte_offset < data_length) {
				byte_read = din.read(data_buff, byte_offset, data_length-byte_offset);
				byte_offset +=  byte_read;
			}

		}catch(Exception e) {
			e.printStackTrace();
		}

		long end = System.nanoTime();
		long end2 = System.currentTimeMillis();
		long total = end - start;
		long total2 = end2 - start2;
		String num = String.valueOf(total);
		String count = String.valueOf(counter);
		String num2 = String.valueOf(total2);
		try {
			info.write("\n");
			info.write("\n");
			info.write( num + "," + " nanosec" + "\n");
			info.write( num2 + "," + " millsec" + "\n");
			info.write( "-------------" + "\n");
			info.write(count);
			info.write( "\n");
			info.write("\n");
			if(counter == 15) {
				counter = 0;
			}
			counter++;

			
			
			
			info.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}



		return data_buff;


	}
	private byte[] CreateDataPacket(byte[] data){
		byte[] packet=null;
		try{
			byte[] initialize = new byte[1];
			initialize[0]=2;
			byte[] separator = new byte[1];
			separator[0] = 4;
			byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
			packet=new byte[initialize.length+separator.length+data_length.length+data.length];
			System.arraycopy(initialize,0,packet,0,initialize.length);
			System.arraycopy(data_length,0,packet,initialize.length,data_length.length);
			System.arraycopy(separator,0,packet,initialize.length+data_length.length,separator.length);
			System.arraycopy(data,0,packet,initialize.length+data_length.length+separator.length,data.length);

		}catch(Exception e){
			e.printStackTrace();
		}
		return packet;
	}

}
