package io.github.ssebs.piledcontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

	static DatagramSocket sock;
	static int port = 7788;
	static InetAddress host;
	static byte[] b;
	static Thread receive;

	String textToSend;
	String replyData;
	final String URL = "ssebs.ddns.net";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Button btnOn = (Button) findViewById(R.id.btnOn);
		btnOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				proc(true);
			}
		});

		Button btnOff = (Button) findViewById(R.id.btnOff);
		btnOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				proc(false);
			}
		});
	}
	private void proc(boolean b) {
		textToSend = ""+b;

		Thread init = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					host = InetAddress.getByName(URL);
					sock = new DatagramSocket();

					receiveAndToast(); // makes a receive thread and waits
					sendMsg(textToSend); // actually sends the data
				} catch (IOException e) {
					System.err.println("IOException " + e);
				}
			}
		});
		init.start();

	}

	private void receiveAndToast() {
		receive = new Thread(new Runnable() {
			@Override
			public void run() {
				//buffer to receive incoming data
				byte[] buffer = new byte[65536];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				try {
					sock.receive(reply);

					byte[] data = reply.getData();
					replyData = new String(data, 0, reply.getLength());

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "Server: " + replyData, Toast.LENGTH_SHORT).show();
						}
					});

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		receive.start();

	}


	private void sendMsg(String s) {
		b = s.getBytes();
		try {
			sock.send(new DatagramPacket(b, b.length, host, port));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}