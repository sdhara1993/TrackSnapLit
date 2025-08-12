package com.example.tracksnaplite;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KMLWriter {
    private static final String FILE_NAME = "tracklog.kml";

    public static void appendWaypoint(Context context, double lat, double lon) {
        File file = new File(context.getExternalFilesDir(null), FILE_NAME);

        try {
            if (!file.exists()) {
                FileWriter fw = new FileWriter(file);
                fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                fw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n<Document>\n");
                fw.close();
            }

            FileWriter fw = new FileWriter(file, true);
            fw.write("<Placemark><Point><coordinates>" + lon + "," + lat + "</coordinates></Point></Placemark>\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeKML(Context context) {
        File file = new File(context.getExternalFilesDir(null), FILE_NAME);
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write("</Document>\n</kml>");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
