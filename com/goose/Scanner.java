package com.goose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.goose.Token.*;
import static com.goose.TokenType.*;

public class Scanner {

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    private final String code;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    List <Token> tokenList = new ArrayList<>();

    Scanner(String code){
        this.code = code;
    }

    // this is calls scanToken until the file is at its end
    List <Token> scanTokens(){
        
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
        return tokenList;
    }

    private char advance(){
        // note that this is fine that we increment even the first char we look at, because String literals start with "
        return code.charAt(current++);
    }

    // scans characters into tokens
    private void scanToken(){
        char currChar = advance();

        switch (currChar) {
            //single character tokens
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break; 
            //can be either single token or 2 tokens
            case '=': addToken(checkNext('=') ? EQUAL_EQUAL : EQUAL); break;
            case '!': addToken(checkNext('=') ? BANG_EQUAL : BANG); break;
            case '>': addToken(checkNext('=') ? GREATER_EQUAL : GREATER); break;
            case '<': addToken(checkNext('=') ? LESS_EQUAL : LESS); break;
            // slashes are used for comments too, so we want to go to the next line if we see two in a row
            case '/': 
                if(!checkNext('/')){
                    addToken(SLASH);
                }
                else{
                    while(peek() != '\n' && !isAtEnd()){
                        current ++;
                    }
                }
                break;
            
            //skip the empty string case
            case ' ':
            case '\r':
            // Ignore whitespace
            case '\t':
                break;
        
            case '\n':
                line++;
                break;
            
            case '"': string(); break;
        
            // if we see a character we don't expect
            default:
                if(isDigit(currChar)){
                    number();
                }
                // checks for reserved words
                else if(isChar(currChar)){
                    identifier();
                }
                else{
                    Goose.error(line, "Unexpected character." + Character.toString(currChar));
                    break;
                }
          }
        
    }

    private void identifier(){
        //while the character ahead is either a number or character
        while(isCharOrNum(peek())){
            advance();
        }

        String text = code.substring(start, current);

        // if our word is a reserved word
        if(keywords.containsKey(text)){
            addToken(keywords.get(text));
        }
        else{
            addToken(IDENTIFIER);
        }
    }

    private void number(){

        while(isDigit(peek())){
            advance();
        }

        if(peek() == '.'  && isDigit(peekNext())){
            
            advance();

            while(isDigit(peek())){
                advance();
            }
        }

        addToken(NUMBER, Double.parseDouble(code.substring(start, current)));
    }

    private void string(){

        while(peek() != '"' && !isAtEnd()){
            if (peek() == '\n'){
                line++;
            }

            advance();
        }

        if(isAtEnd()){
            Goose.error(line, "String is not terminated.");
        }

        // add the closing quote
        advance();

        String lexeme = code.substring(start + 1, current-1);
        addToken(STRING, lexeme);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
      }

    private boolean isChar(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||  (c == '_');
    }

    private boolean isCharOrNum(char c){
        return isChar(c) || isDigit(c);
    }

    // to be used to create a token for non literal tokens
    private void addToken(TokenType type){
        addToken(type, null);
    }

    // creates the token for a given type and literal, and adds it to our list of tokens
    // type is one of our predefined types, eg. NUMBER, lexeme is the string representation of our literal,
    // and literal is the representation of our literal in the type that it is. line is line number.
    private void addToken(TokenType type, Object literal){
        String lexeme = code.substring(start, current);
        tokenList.add(new Token(type, lexeme, literal, line));
    }

    // checks if our current pointer has gotten out of bounds of the string
    private boolean isAtEnd() {
        return current >= code.length();
    }

    // checks the next character, and includes it if it matches what is expected
    private Boolean checkNext(char expected){
        if(isAtEnd()){
            return false;
        }
        char next = code.charAt(current);
        if(next == expected){
            current ++;
            return true;
        }

        return false;
    }

    // function to check the next character in the string (note that current gets auto incremented in scanToken)
    private char peek(){
        if(!isAtEnd()){
            return code.charAt(current);
        }
        else{
            return '\0';
        }
    }

    private char peekNext(){
        if(!isAtEnd()){
            int temp = current + 1;
            return code.charAt(temp);
        }
        else{
            return '\0';
        }
    }

    
}
