package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest02.network.ClientAsyncTask;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private ServerThread serverThread;
    private ClientAsyncTask clientAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        final EditText serverPortEditText = findViewById(R.id.server_port);
        Button serverConnectBtn = findViewById(R.id.server_connect_btn);
        serverConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverThread = new ServerThread(serverPortEditText);
                serverThread.startServer();
            }
        });

        final EditText clientAddressEditText = findViewById(R.id.client_address);
        final EditText clientPortEditText = findViewById(R.id.client_port_number);
        final EditText currencyEditText = findViewById(R.id.currency);
        final TextView currencyDisplayInfo = findViewById(R.id.currency_info);

        Button clientRequestBtn = findViewById(R.id.client_request_btn);
        clientRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientAsyncTask = new ClientAsyncTask(currencyEditText.getText().toString(), currencyDisplayInfo);
                clientAsyncTask.execute(clientAddressEditText.getText().toString(), clientPortEditText.getText().toString());
            }
        });
    }
}
