package br.com.windisch.xtract.compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import br.com.windisch.xtract.compiler.formatters.LexerExceptionFormatter;
import br.com.windisch.xtract.compiler.formatters.ParserExceptionFormatter;
import br.com.windisch.xtract.compiler.models.LexerException;
import br.com.windisch.xtract.compiler.models.ParserException;
import br.com.windisch.xtract.compiler.models.Token;
import br.com.windisch.xtract.compiler.nodes.Program;
import br.com.windisch.xtract.compiler.nodes.SelectField;

public class Xtract
{
    public static void main( String[] args )
    {
        List<Token> tokens = null;
        String content = null;

        if (args.length != 1) System.out.println("Usage: xtract <source-file>");
        else {
            String sourceFile = args[0];

            if (!Files.exists(Paths.get(sourceFile))) {
                System.err.println("Source file does not exist: " + sourceFile);
                return;
            }

            try {
                content = Files.readString(Paths.get(sourceFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            Lexer lexer = new Lexer(content);

            try {
                tokens = lexer.run();
                // tokens.forEach(System.out::println);
            } catch (LexerException e) {
                var formatter = new LexerExceptionFormatter(content, e);
                System.out.println(formatter.format());
                return;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return;
            }
        }

        var parser = new Parser(tokens);
        Program program = null;
        try {
            program = parser.parse();
        } catch (ParserException e) {
            var formatter = new ParserExceptionFormatter(content, e);
            System.out.println(formatter.format());
            return;
        }

        CodeGenerator generator = new CodeGenerator(program);
        System.out.println(generator.generate());
    }
}
