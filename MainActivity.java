package com.example.wang.finalattempt;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;

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

import java.net.UnknownHostException;

public class MainActivity extends Activity {
    //buttons

    private Button packetAutomaticWifi;

    private  Button fileCreation;

    TextView textView;
    //String path for the creation and creating a file
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Filesz";
    File file = new File(path);

    //handle packet auto stuff
    private Handler wifiHandler;

    //handles automatic wifi stuff
    int changer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assigning buttons
        packetAutomaticWifi = findViewById(R.id.packetAutomaticWifi);
        fileCreation = findViewById(R.id.fileCreate);
        textView = findViewById(R.id.textView);
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
                        break;
                    }else if(changer==1){
                       sendDataTest2k();
                        changer++;
                        break;
                    }else if(changer==2 ){
                        sendDataTest3k();
                        changer++;
                        break;
                    }else if(changer==3){
                        sendDataTest5k();
                        changer++;
                        break;
                    }else if(changer==4){
                        sendDataTest10k();
                        changer++;
                        break;
                    }else if(changer==5 ){
                        sendDataTest100k();
                        changer++;
                        break;
                    }else if(changer==6 ){
                        sendDataTest1mb();
                        changer++;
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




    public void fileCreation(){
        try {
            // catches IOException below
            File file = new File(path + "/1mb.txt");
            String build = "";
            for(int i =0;i<=100000;i++){
                build += "g";
            }
            String mes = build;

            String dataPacket = new String(mes);
            /* We have to use the openFileOutput()-method
             * the ActivityContext provides, to
             * protect your file from others and
             * This is done for security-reasons.
             * We chose MODE_WORLD_READABLE, because
             *  we have nothing to hide in our file */
            FileOutputStream fOut = openFileOutput("1mb.txt",
                    MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            // Write the string to the file
            osw.write(dataPacket);
            /* ensure that everything is
             * really written out and close */
            osw.flush();
            osw.close();
            //Reading the file back...
            /* We have to use the openFileInput()-method
             * the ActivityContext provides.
             * Again for security reasons with
             * openFileInput(...) */
            FileInputStream fIn = openFileInput("1mb.txt");
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
            // Log.i("File Reading stuff", "success = " + isTheSame);

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

    public void sendDataTest1mb(){
        MessageSender  mt = new MessageSender();
        mt.execute("1mb.txt");
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
        String textView1 = "hi";

    }
    public class MessageSender extends AsyncTask<Object, Void, MyTaskResult> {
        String dstAddress = "73.150.49.150";
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

            //Toast.makeText(getApplicationContext(),myLoc.textView1,Toast.LENGTH_LONG).show();

        }


    }//end of inner class
}//end of everything

