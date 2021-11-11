package com.assignment.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.logging.Logger;

public class JsonNodeMapper {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    JsonNode rootNode;
    ObjectMapper objectMapper;

    public JsonNodeMapper(String line) {
        this.objectMapper = new ObjectMapper();
        try {
            this.rootNode = objectMapper.readTree(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonNode readJsonWithJsonNode() throws IOException {
        String printEntry = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        logger.info(printEntry+"\n");
        return rootNode;
    }

    public String readIdNode() throws IllegalArgumentException {
        JsonNode idNode=rootNode.path("id");

        if(idNode.asText()==null || idNode.asText().isEmpty()){
                throw new IllegalArgumentException("Entry ID can not be null.");
        }

        return idNode.asText();
    }

    public String readStateNode()
    {
        JsonNode stateNode=rootNode.path("state");
        return stateNode.asText();
    }

    public String readTypeNode()
    {
        JsonNode typeNode=rootNode.path("type");
        return typeNode.asText();
    }

    public String readHostNode()
    {
        JsonNode hostNode=rootNode.path("host");
        return hostNode.asText();
    }

    public String readTStamp(){
        JsonNode tsNode = rootNode.path("timestamp");
        return tsNode.asText();

    }

    public Entry getEntry() throws Exception{
        return new Entry(readIdNode(),readStateNode(),readTypeNode(),readHostNode(),readTStamp());
    }

}
