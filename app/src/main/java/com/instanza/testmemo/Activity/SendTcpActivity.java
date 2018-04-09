package com.instanza.testmemo.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.instanza.testmemo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by apple on 2017/12/26.
 */

public class SendTcpActivity extends AppCompatActivity {
    Button ConnectButton;//定义连接按钮
    Button SendButton;//定义发送按钮
    EditText IPEditText;//定义ip输入框
    EditText PortText;//定义端口输入框
    EditText MsgText;//定义信息输出框
    EditText RrceiveText;//定义信息输入框
    Socket socket = null;//定义socket
    EditText MsgEditText;//定义信息输出框
    EditText RrceiveEditText;//定义信息输入框
    private BufferedReader in = null;
    private PrintWriter out = null;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RrceiveEditText.setText(content);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_tcp);

        ConnectButton = (Button) findViewById(R.id.Connect_Bt);//获得按钮对象
        SendButton = (Button) findViewById(R.id.Send_Bt);//获得按钮对象
        IPEditText = (EditText) findViewById(R.id.ip_ET);//获得ip文本框对象
        PortText = (EditText) findViewById(R.id.Port_ET);//获得端口文本框按钮对象
        MsgEditText = (EditText) findViewById(R.id.Send_ET);//获得发送消息文本框对象
        RrceiveEditText = (EditText) findViewById(R.id.Receive_ET);//获得接收消息文本框对象
        ConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect_onClick(v);
            }
        });

//        SendButton.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                String msg = MsgEditText.getText().toString();
//                if (socket.isConnected()) {
//                    if (!socket.isOutputShutdown()) {
//                        out.println(msg);
//                    }
//                }
//            }
//        });
        TcpThread tcpThread = new TcpThread();
        new Thread(tcpThread).start();
    }

    private boolean  isConnect = false;
    public void Connect_onClick(View v) {
        if (isConnect == false) //标志位 = true表示连接
        {
            isConnect = true;//置为false
            ConnectButton.setText("断开");//按钮上显示--断开
        }
        else //标志位 = false表示退出连接
        {
            isConnect = false;//置为true
            ConnectButton.setText("连接");//按钮上显示连接
        }
    }


    private String content = "";
    private class TcpThread implements Runnable{
        @Override
        public void run() {
            try {
                System.out.println("start ");
                socket = new Socket(IPEditText.getText().toString(), Integer.valueOf(PortText.getText().toString()));
                in = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                while (true) {
                    if (!socket.isClosed()) {
                        if (socket.isConnected()) {
                            if (!socket.isInputShutdown()) {
                                System.out.println("while  = ");
                                if ((content = in.readLine()) != null) {
                                    System.out.println("content  = " + content);
                                    content += "\n";
                                    mHandler.sendMessage(mHandler.obtainMessage());
                                } else {

                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
