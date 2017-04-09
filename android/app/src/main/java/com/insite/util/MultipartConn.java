package com.insite.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chris on 9/4/17.
 *
 * @author Chris
 * @version 1.0
 */

public class MultipartConn {

    public String attachmentFileName = "bitmap.bmp";

    static String attachmentName = "image";
    static String crlf = "\r\n";
    static String twoHyphens = "--";
    static String boundary =  "*****";

    private OutputStream outputStream;
    private Writer writer;

    HttpURLConnection urlConnection;

    public MultipartConn(String uri, String charset) throws IOException {
        HttpURLConnection httpUrlConnection = null;
        URL url = new URL(uri);
        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);

        outputStream = httpUrlConnection.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }




}
