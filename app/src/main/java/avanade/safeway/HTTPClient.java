package avanade.safeway;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static avanade.safeway.SafeWayGPSListener.saveData;


public class HTTPClient extends AsyncTask<String, Void, Void> {
    @Override
    protected void onPostExecute(Void v) {
        Log.e("HTTPClient", "onPostExecute");
    }


    @Override
    protected void onPreExecute() {
        Log.e("HTTPClient", "onPreExecute");
    }

    @Override
    protected Void doInBackground(String... data) {
        Log.e("HTTPClient", "doInBackground");

        try {
            URL url = new URL("http://82.243.88.198");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.e("HTTPClient", data[1]);
            wr.write(data[1]);
            wr.flush();

            // Get the server response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                line += "\n";
                sb.append(line);
            }

            String text = conn.getResponseCode() + sb.toString();
            Log.e("HTTPClient", text);


        } catch (IOException e) {
            Log.e("HTTPClient", e.toString());
            saveData(data[0], data[1] + '\n');
        }

        return null;
    }
}
