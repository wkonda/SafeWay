package avanade.safeway;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


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
            URL url = new URL("http://192.168.43.169:8080");

            //String data="name=Sid&email=58999&user=user&pass=passsss";

            BufferedReader reader;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.e("HTTPClient", data[0]);
            wr.write(data[0]);
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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
            e.printStackTrace();
        }

        return null;
    }
}
