package br.com.windisch.xtract.compiler.utils;

import br.com.windisch.xtract.compiler.models.ScrapeContext;
import br.com.windisch.xtract.compiler.outputters.IOutputter;
import br.com.windisch.xtract.compiler.outputters.JsonOutputter;

public class ScrapeContextOutputter {
    public static void output(ScrapeContext context) {
        for (var result : context.parseContexts()) {
            var fileName = result.getOutputFileName();
            
            IOutputter outputter = new JsonOutputter();
            outputter.output(result.getData(), fileName);

            System.out.println("Outputting to file: " + fileName);
        }
    }
}