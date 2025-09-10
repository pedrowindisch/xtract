package br.com.windisch.xtract.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import br.com.windisch.xtract.compiler.models.ParserException;
import br.com.windisch.xtract.compiler.models.Token;

public class Xtract
{
    public static void main( String[] args )
    {
        List<Token> tokens = null;

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
                tokens = lexer.run();
                // tokens.forEach(System.out::println);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        var parser = new Parser(tokens);
        try {
            var program = parser.parse();

            System.out.println(program.getBase().getUrl());
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
