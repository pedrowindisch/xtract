package br.com.windisch.xtract.compiler.utils;

import br.com.windisch.xtract.compiler.models.ScrapeContext;

public class ScrapeContextOutputter {
    public static void output(ScrapeContext context) {
        for (var result : context.parseContexts()) {
            var fileName = result.getOutputFileName();
            
            System.out.println("Outputting to file: " + fileName);
        }
    }
}