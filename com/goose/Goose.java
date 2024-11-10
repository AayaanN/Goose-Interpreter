package com.goose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Goose {

    static Boolean hadError = false;

    //start point of the interpreter
    public static void main(String args[]) throws IOException {
        if (args.length > 1){
            System.out.println("Usage: jgoose [Script]");
            System.exit(64);
        }
        else if (args.length == 1){
            runFile(args[0]);
        }
        else{
            runPrompt();
        }
    }
    
    // if given an actual file, convert the entire file into parsable text
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    // run prompts line by line as they are given
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while(true){
            System.out.println("< ");
            String line = reader.readLine();
            if(line == null){
                break;
            }
            run(line);
        }
    }

    // run the code given through the scanner, return a list of tokens
    private static void run(String code){
        Scanner scanner = new Scanner(code);
        List<Token> token_list = scanner.scanTokens();

        for(Token token: token_list){
            System.out.println(token);
        }
    }

    private static void error(int line, String message){
        report(line, "",message);
    }

    private static void report(int line, String where, String message){
        System.out.println(
            "[line " + line + "] Error" + where + ": " + message
            );
        hadError = true;
    }

    
}



