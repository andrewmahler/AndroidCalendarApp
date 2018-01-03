package com.example.andrew.calendarapp;


import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SynonymTaskForEdit implements Runnable {
    private final Edit context;
    private final String URL;

    SynonymTaskForEdit(Edit context, String url) {
        this.context = context;
        this.URL = url;
    }

    public void run() {
        ArrayList<String> synonyms = new ArrayList<>();
        URL url;
        HttpURLConnection con = null;

        try {
            if (Thread.interrupted())
                throw new InterruptedException();
            System.out.println(URL);
            url = new URL(URL);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setDoInput(true);
            con.connect();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(con.getInputStream(), null);
            int eventType = parser.getEventType();
            String text = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equalsIgnoreCase("synonyms")) {
                            String[] tokens = text.split("\\|");
                            for (int i = 0; i < tokens.length; i++) {
                                synonyms.add(tokens[i]);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (MalformedURLException mal) {
            // nothing?
            System.out.println("Malformed URL Exception");
        } catch (IOException io) {
            // nothing?
            System.out.println("IO Exception");
            System.out.print(io.getMessage() + "\n");

        } catch (XmlPullParserException parse) {
            // nothing?
            System.out.println("XML Pull Parser Exception");
        } catch (InterruptedException inter) {
            System.out.println("Interrupted Exception");
        } finally {
            if (con != null)
                con.disconnect();
        }

        context.setSynonyms(synonyms);
    }
}
