package br.com.windisch.xtract.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Xtract
{
    public static void main( String[] args )
    {
        if (args.length != 1) System.out.println("Usage: xtract <source-file>");
        else {
            String sourceFile = args[0];

            if (!Files.exists(Paths.get(sourceFile))) {
                System.err.println("Source file does not exist: " + sourceFile);
                return;
            }

            String content = null;
            try {
                content = Files.readString(Paths.get(sourceFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            Lexer lexer = new Lexer(content);

            try {
                var tokens = lexer.run();
                tokens.forEach(System.out::println);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
