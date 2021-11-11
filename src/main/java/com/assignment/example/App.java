package com.assignment.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {

	 private static final String path = "src/main/resources/server.log";
	 
	public static void main (String[] argv) throws Exception {

        LogHandler logHandler = new LogHandler();
        EntryDbHandler entryDbHandler = new EntryDbHandler();

        List<String> lineList = logHandler.readLog(argv != null && argv.length > 0 ? argv[0] : path );
        List<Entry> entries = new ArrayList<>();

        for (String line : lineList){
            JsonNodeMapper jsonNodeMapper = new JsonNodeMapper(line);
            entries.add(jsonNodeMapper.getEntry());
        }

        ArrayList<Entry> uniqId = new ArrayList<Entry>();
        Map<String, List<Entry>> entriesById = entries.stream().collect(Collectors.groupingBy(Entry::getId));
        final Entry maxEntry = new Entry();
        entriesById.values().stream()
                .filter(entriesWithSameId -> entriesWithSameId.size()>1)
                .forEach(entriesWithSameId -> {
                	Entry newEntry = entriesWithSameId.get(0);
                	long maxDuration1 = Math.abs(Long.parseLong(entriesWithSameId.get(0).getTimestamp()) - Long.parseLong(entriesWithSameId.get(1).getTimestamp()));
                	if(maxDuration1 > maxEntry.getMaxDuration()){
                		maxEntry.setMaxDuration(maxDuration1);;
                		maxEntry.setId(newEntry.getId());
                	}
                	uniqId.add(newEntry);
                });



        entryDbHandler.createDb();
        uniqId.forEach(entry -> {
            try {
                entryDbHandler.insertRecord(entry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
       // entryDbHandler.getAllEntries();

        try {
        	entryDbHandler.alertEntry(maxEntry.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        entryDbHandler.lookupAlert();
        entryDbHandler.destroy();
    }
}
