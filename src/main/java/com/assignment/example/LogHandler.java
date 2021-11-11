package com.assignment.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogHandler {
    private static final String path = "src/main/resources/server.log";
    List<String> lineList = new ArrayList<>();

    public List<String> readLog(String filePath) throws IOException
    {
        if (filePath == null || filePath.isEmpty()){
            filePath = path;
        }
        try( FileInputStream logStream = new FileInputStream(filePath);
                BufferedReader br = new BufferedReader( new InputStreamReader(logStream)))
        {
            String line;
            while(( line = br.readLine()) != null ) {
                lineList.add(line);
            }
            return lineList;
        }
    }
}
