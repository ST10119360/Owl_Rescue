package com.example.guidinglight_opsc_part_2_group_5;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Installlinkl
{
    public String readUrl(String myUrl) throws IOException
    {
        String d = "";
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL lin = new URL(myUrl);
            connection = (HttpURLConnection) lin.openConnection();
            connection.connect();

            stream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = br.readLine()) != null)
            {
                sb.append(line);

            }

            d = sb.toString();


            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(stream != null)
                stream.close();
            connection.disconnect();
        }

        return d;

    }
}

