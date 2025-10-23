package br.com.windisch.xtract.compiler.outputters;

import java.util.Map;

public class JsonOutputter implements IOutputter {
    @Override
    public void output(Map<String, String> data, String destination) {
        System.out.println("Outputting JSON to " + destination);
        // Implementation for outputting data as JSON
    }    
}
