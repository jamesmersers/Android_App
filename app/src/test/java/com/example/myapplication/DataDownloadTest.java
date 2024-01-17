package com.example.myapplication;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DataDownloadTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void download() {
        DataDownload httpDataLoader = new DataDownload();
        URL url = null;
        try {
            url = new URL("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.setRequestProperty("Accept-Charset", "UTF-8");

        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        StringBuilder fileContentBuilder = new StringBuilder();
        String line;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileContentBuilder.append(line);
        }

        String fileContent = fileContentBuilder.toString();
        assertTrue(fileContent.contains("\"name\": \"暨大珠海\","));
        assertTrue(fileContent.contains("\"memo\": \"珠海二城广场\""));
    }

    @Test
    public void parseJsonObjects() {
        DataDownload httpDataLoader=new DataDownload();
        String fileContent="{\n" +
                "  \"shops\": [{\n" +
                "    \"name\": \"暨大珠海\",\n" +
                "    \"latitude\": \"22.255925\",\n" +
                "    \"longitude\": \"113.541112\",\n" +
                "    \"memo\": \"暨南大学珠海校区\"\n" +
                "  },\n" +
                "    {\n" +
                "      \"name\": \"沃尔玛(前山店)\",\n" +
                "      \"latitude\": \"22.261365\",\n" +
                "      \"longitude\": \"113.532989\",\n" +
                "      \"memo\": \"沃尔玛(前山店)\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"明珠商业广场\",\n" +
                "      \"latitude\": \"22.251953\",\n" +
                "      \"longitude\": \"113.526421\",\n" +
                "      \"memo\": \"珠海二城广场\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ArrayList<LocationMessage> locations= httpDataLoader.parseJsonObjects(fileContent);

        assertEquals(3,locations.size());
        assertEquals("暨大珠海",locations.get(0).getName());
        assertEquals(22.251953,locations.get(2).getLatitude(),1e-6);
        assertEquals(113.526421,locations.get(2).getLongitude(),1e-6);
        assertEquals("珠海二城广场",locations.get(2).getMemo());
    }
}