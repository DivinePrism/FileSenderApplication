package com.example.wang.finalattempt;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.net.UnknownHostException;
import androidx.fragment.app.FragmentActivity;

public  class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    //buttons
    private Button packetAutomaticWifi;
    private  Button fileCreation;

    private TextView textView;
    //String path for the creation and creating a file
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FilesForData";
    private File file = new File(path);

    //handle packet auto stuff
    private Handler wifiHandler;

    //handles automatic wifi stuff
      String stringBuilderGps = "";
    static int changer = 0;
    int dataCounter = 0;
    static int total = 0;
    
    private ProgressBar  simpleProgressBar;
    private Handler mHandler = new Handler();
    private int intie = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //assigning buttons
        packetAutomaticWifi = findViewById(R.id.packetAutomaticWifi);
        fileCreation = findViewById(R.id.fileCreate);
        textView = findViewById(R.id.FileProgressTextView);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);

        simpleProgressBar.setMax(100); // 100 maximum value for the progress value

        // file directory make up
        file.mkdirs();
    
        fileCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makes string files
               fileCreation();

            }
        });
        //(handles wifi auto loop)
        packetAutomaticWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiHandler = new Handler();
                wifi_Runnable.run();
            }
        });

    }// end of oncreate
     @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //rowan
        LatLng rowan = new LatLng(39.7107068, -75.1203863);
        mMap.addMarker(new MarkerOptions().position(rowan).title("Marker at Rowan University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rowan));
    }

/*
TODO need to make into loop statement, loop makes app crash. Fast fix to get data
*/
    private final Runnable wifi_Runnable;//runnable
    {
        wifi_Runnable = new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, "Auto Update! ", Toast.LENGTH_SHORT).show();
                boolean isRunnning = true;
                while(isRunnning) {
                    if(changer == 0) {
                        sendDataTest1k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "1k", Toast.LENGTH_LONG).show()
                        break;
                    }else if(changer==1){
                       sendDataTest2k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "2k", Toast.LENGTH_LONG).show()
                        break;
                    }else if(changer==2 ){
                        sendDataTest3k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "3k", Toast.LENGTH_LONG).show()
                        break;
                    }else if(changer==3){
                        sendDataTest5k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "5k", Toast.LENGTH_LONG).show()
                        break;
                    }else if(changer==4){
                        sendDataTest10k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "10k", Toast.LENGTH_LONG).show()
                        break;
                    }else if(changer==5 ){
                        sendDataTest100k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "100k", Toast.LENGTH_LONG).show()
                        break;
                    }else if(changer==6 ){
                        sendDataTest500k();
                        changer++;
                           Toast.makeText(getApplicationContext(), "1mb", Toast.LENGTH_LONG).show()
                        break;
                    }
                    else{
                        changer = 0;
                    }
                }// end of for loop
                MainActivity.this.wifiHandler.postDelayed(wifi_Runnable, 20000);
            }
        };
    }
/*
create a file with random data inside to be sent out
*/
    public void fileCreation(){
        try {
            // catches IOException below
            File file = new File(path + "/1mb.txt");
            String build = "";
            for(int i =0;i<=500000;i++){
                build += "a";
            }
            String mes = build;
            String dataPacket = new String(mes);
            FileOutputStream fOut = openFileOutput("500k.txt",
                    MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // Write the string to the file
            osw.write(dataPacket);
            /* ensure that everything is
             * really written out and close */
            osw.flush();
            osw.close();
            //Reading the file back.
            FileInputStream fIn = openFileInput("500k.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            /* Prepare a char-Array that will
             * hold the chars we read back in. */
            char[] inputBuffer = new char[dataPacket.length()];
            // Fill the Buffer with data from the file
            isr.read(inputBuffer);
            // Transform the chars to a String
            String readString = new String(inputBuffer);
            // Check if we read back the same chars that we had written out
            boolean isTheSame = dataPacket.equals(readString);
            String[] saveText;
            saveText = dataPacket.split(System.getProperty("line.separator"));
            Save2(file, saveText);
            Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_LONG).show();
        } catch (IOException ioe)
        {ioe.printStackTrace();}
    }

    public void sendDataTest1k (){
        MessageSender  mt = new MessageSender();
        mt.execute("1k.txt");
    }
    public void sendDataTest2k (){
        MessageSender  mt = new MessageSender();
        mt.execute("2k.txt");
    }
    public void sendDataTest3k(){
        MessageSender  mt = new MessageSender();
        mt.execute("3k.txt");
    }
    public void sendDataTest5k(){
        MessageSender  mt = new MessageSender();
        mt.execute("5k.txt");
    }
    public void sendDataTest10k (){
        MessageSender  mt = new MessageSender();
        mt.execute("10k.txt");
    }

    public void sendDataTest100k (){
        MessageSender  mt = new MessageSender();
        mt.execute("100k.txt");
    }

    public void sendDataTest500k(){
        MessageSender  mt = new MessageSender();
        mt.execute("500k.txt");
    }
    /*
      to save to a textfile with appending feature
   */
    public void Save2(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            //append to a end of a textfile
            fos = new FileOutputStream(file, true);
            try {

                for (int i = 0; i < data.length; i++) {
                    fos.write(data[i].getBytes());

                    if (i < data.length - 1) {
                        fos.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }//end of Save2 method

    private class MyTaskResult {
        String textView1 = "test";

    }
    /*
    Sending the data through a socket connection
    */
    public class MessageSender extends AsyncTask<Object, Void, MyTaskResult> {
       //enter ip to server
        String dstAddress = "73.xxx.xx.xxx";
        //enter port, might need to port forward
        int dstPort = 6000;
        private String val1;
        @Override
        protected MyTaskResult doInBackground(Object... params) {
            //path to grab file from
            MyTaskResult res = new MyTaskResult();
            val1 = (String) params[0];
            String location = "/storage/emulated/0/Filesz/"+val1 ;
            try {
                Socket client;
                OutputStream outputStream;
                client = new Socket(dstAddress, dstPort);
                outputStream = client.getOutputStream();
              //buffer to send everything (Very Important)
                byte[] buffer = new byte[1024];
                FileInputStream in = new FileInputStream(location);
                int rBytes;
                while((rBytes = in.read(buffer, 0, 1024)) != -1)
                {
                    outputStream.write(buffer, 0, rBytes);
                }
                outputStream.flush();
                outputStream.close();
                client.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(MyTaskResult myLoc) {
           textView.setText(val1);
        }
    }//end of inner class
}//end of everything

