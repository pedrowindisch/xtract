package br.com.windisch.xtract.compiler.nodes;

public sealed interface Node
    permits Base, Output, ParseBody, Parser, Program, Scrape, SelectField {
    
}
