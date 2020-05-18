package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class CommunicationThread extends Thread {

    private Socket socket;
    private ServerThread serverThread;

    public CommunicationThread(Socket socket, ServerThread serverThread) {
        this.socket = socket;
        this.serverThread = serverThread;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort()+ " from " + socket.getInetAddress());

            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);

            HttpClient httpClient = new DefaultHttpClient();
            String pageSourceCode = "";

            String currency = bufferedReader.readLine();
            Log.v(Constants.TAG, "Currency is:" + currency);

            if (currency.equals(Constants.EUR)) {
                HttpGet httpGet = new HttpGet(Constants.API_ADDRESS + Constants.EUR_JSON);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                pageSourceCode = EntityUtils.toString(httpGetEntity);


                JSONObject content = new JSONObject(pageSourceCode);
                JSONObject bpi = content.getJSONObject("bpi");
                JSONObject eur = bpi.getJSONObject(Constants.EUR);
                String eurRate = eur.getString("rate");
                Log.v(Constants.TAG, "Eur rate is:" + eurRate);

                printWriter.println(eurRate);
                printWriter.flush();
            } else {
                HttpGet httpGet = new HttpGet(Constants.API_ADDRESS + Constants.USD_JSON);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                pageSourceCode = EntityUtils.toString(httpGetEntity);

                JSONObject content = new JSONObject(pageSourceCode);
                JSONObject bpi = content.getJSONObject("bpi");
                JSONObject usd = bpi.getJSONObject(Constants.USD);
                String usdRate = usd.getString("rate");
                Log.v(Constants.TAG, "Usd rate is:" + usdRate);

                printWriter.println(usdRate);
                printWriter.flush();
            }


            socket.close();
            Log.v(Constants.TAG, "Connection closed");
        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
