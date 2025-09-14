package br.com.windisch.xtract.compiler;

import java.net.http.HttpRequest;

import javax.lang.model.element.Modifier;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;

import br.com.windisch.xtract.compiler.models.DocumentType;
import br.com.windisch.xtract.compiler.nodes.Base;
import br.com.windisch.xtract.compiler.nodes.Node;
import br.com.windisch.xtract.compiler.nodes.Output;
import br.com.windisch.xtract.compiler.nodes.ParseBody;
import br.com.windisch.xtract.compiler.nodes.Parser;
import br.com.windisch.xtract.compiler.nodes.Program;
import br.com.windisch.xtract.compiler.nodes.Scrape;
import br.com.windisch.xtract.compiler.nodes.SelectField;

public class CodeGenerator {
    private final Program program;

    public CodeGenerator(Program program) {
        this.program = program;
    }

    public String generate() {
        TypeSpec generatedClass = (TypeSpec) generate(program);
        JavaFile javaFile = JavaFile.builder("br.com.windisch.xtract.generated", generatedClass).build();

        return javaFile.toString();
    }

    private Object generate(Node node) {
        return switch (node) {
            case Program program -> generateProgram(program);
            case Scrape scrape -> generateScrape(scrape);
            case Parser parser -> generateParser(parser);
            case Output output -> generateOutput(output);
            case SelectField field -> generateSelectField(field);
            case ParseBody body -> generateParseBody(body);
            case Base base -> generateBase(base);
            default -> throw new IllegalStateException("Unexpected value: " + node);
        };
    }

    private TypeSpec generateProgram(Program program) {
        var builder = TypeSpec
            .classBuilder("GeneratedScraper")
            .addModifiers(javax.lang.model.element.Modifier.PUBLIC)
            .addField((FieldSpec) generate(program.getBase()));

        for (Scrape scrape : program.getScrapes()) builder.addMethod((MethodSpec) generate(scrape));

        var mainMethod = MethodSpec
            .methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement(
                CodeBlock.builder()
                    .add("// Entry point\n")
                    .build()
            );

        // for (Scrape scrape : program.getScrapes()) mainMethod.addStatement("$N()", scrape.getName());

        // builder.addMethod(mainMethod.build());

        return builder.build();
    }

    private FieldSpec generateBase(Base base) {
        var builder = FieldSpec
            .builder(String.class, "BASE_URL", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$S", base.getUrl());

        return builder.build();
    }

    

    private MethodSpec generateScrape(Scrape scrape) {
        var method = MethodSpec
            .methodBuilder(scrape.getName())
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class);

        for (Parser parser : scrape.getParsers()) method.addCode((CodeBlock) generate(parser));

        return method.build();
    }

    private CodeBlock generateParser(Parser parser) {
        var builder = CodeBlock.builder();

        if (parser.getDocumentType() == DocumentType.HTML) {
            builder.add("// HTML parsing \n");

            var body = parser.getParseBody();
            builder.add((CodeBlock) generate(body));

            builder.add("// output: " + (CodeBlock) generate(parser.getOutput()) + "\n");
        } // No other types yet

        return builder.build();
    }

    private CodeBlock generateParseBody(ParseBody body) {
        var builder = CodeBlock.builder()
            .add("// select " + body.getSelector() + "\n");

        for (SelectField field : body.getSelectFields()) {
            builder.add((CodeBlock) generate(field));
        }

        return builder.build();
    }

    private CodeBlock generateSelectField(SelectField field) {
        return CodeBlock.builder()
            .add("// select field " + field.getField() + " using " + field.getExtractionMethodType() + " " + field.getAttributeName() + "\n")
            .build();
    }

    private CodeBlock generateOutput(Output output) {
        return CodeBlock.builder()
            .add("// output to " + output.getFileName() + " as " + output.getOutputType() + "\n")
            .build();
    }



    // private TypeSpec generateHttpRequest(HttpRequest request) {
    //     var method = MethodSpec
    //         .methodBuilder("run")
    //         .addModifiers(javax.lang.model.element.Modifier.PUBLIC, javax.lang.model.element.Modifier.STATIC)
    //         .returns(void.class)
    //         .addStatement("// do " + request.method() + " " + request.path())
    //         .build();

    //     return TypeSpec
    //         .classBuilder("Request")
    //         .addModifiers(javax.lang.model.element.Modifier.STATIC)
    //         .addMethod(method)
    //         .build();
    // }
}
