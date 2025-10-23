package br.com.windisch.xtract.compiler.outputters;

import java.util.Map;

public interface IOutputter {
    void output(Map<String, String> data, String destination);
}
