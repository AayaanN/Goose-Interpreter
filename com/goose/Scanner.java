package com.goose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.goose.Token.*;
import static com.goose.TokenType.*;

public class Scanner {

    private final String code;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    List <Token> tokenList = new ArrayList<>();

    Scanner(String code){
        this.code = code;
    }

    List <Token> scanTokens(){
        
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
        return tokenList;
    }

    private void scanToken(){
        char currChar = code.charAt(current++);

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
        
            // if we see a character we don't expect
            default:
                Goose.error(line, "Unexpected character.");
                break;
          }
        
    }

    // to be used to create a token for non literal tokens
    private void addToken(TokenType type){
        addToken(type, null);
    }

    // creates the token for a given type and literal, and adds it to our list of tokens
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

    
}