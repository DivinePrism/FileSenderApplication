import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainThreadedServer {
private static ServerSocket serverSocket;
public static void main(String[] args) {

try {
serverSocket = new ServerSocket(6000);

while(true) {
new Thread(new ClientWorker(serverSocket.accept())).start();
}
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
}
class ClientWorker implements Runnable{
private static ServerSocket serverSocket;
private static Socket clientSocket;
private static InputStream inputStream;
private static FileOutputStream fileOutputStream;
private static BufferedOutputStream bufferedOutputStream;
private static int filesize = 10000000;
private static int bytesRead;
private static int current = 0;
static int counter = 1;
static int counterA = 0;
public ClientWorker (Socket serverSocket) {
try {
clientSocket = serverSocket;
}catch(Exception e) {
e.printStackTrace();
}
}

@Override
public void run() {
try {
long start = System.currentTimeMillis();

byte[] mybytearray = new byte[filesize];   

inputStream = clientSocket.getInputStream();

fileOutputStream = new FileOutputStream("/home/ning/Desktop/output.txt");

bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

System.out.println("Receiving...");

bytesRead = inputStream.read(mybytearray, 0, mybytearray.length);
current = bytesRead;

do {
bytesRead = inputStream.read(mybytearray, current, (mybytearray.length - current));

if (bytesRead >= 0) {
current += bytesRead;

}
} while (bytesRead > -1);


bufferedOutputStream.write(mybytearray, 0, current);
long end = System.currentTimeMillis();
bufferedOutputStream.flush();

long totalTime = end-start;
String num2 = String.valueOf(totalTime);
FileWriter info = null;
try {
info = new FileWriter("/home/ning/Desktop/data.txt",true);
} catch (IOException e1) {
// TODO Auto-generated catch block
e1.printStackTrace();
}

info.write(num2 + "\n");
counterA++;

if(counterA == 7){

info.write("-------------------------- \n");
counterA = 0;

}

info.flush();
info.close();

bufferedOutputStream.close();
inputStream.close();
clientSocket.close();

System.out.println("Sever recieved the file");
}catch(Exception e){
e.printStackTrace();;
}

}
}
