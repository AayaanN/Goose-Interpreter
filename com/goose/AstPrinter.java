package com.goose;

import com.goose.Expr.Binary;
import com.goose.Expr.Grouping;
import com.goose.Expr.Literal;
import com.goose.Expr.Unary;

// to use this class, call AstPrinter.visit... on the right function based on the root of the AST
class AstPrinter implements Expr.Visitor<String>{

    String print(Expr expr) {
        return expr.accept(this);
      }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Expr.Literal(123)
                ,
                new Token(TokenType.MINUS, "-", null, 1)
                ),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)));
    
        System.out.println(new AstPrinter().print(expression));
      }

    @Override
    public String visitBinaryExpr(Binary expr){
            
            return "( " + expr.operator.lexeme + expr.left.accept(this)  + expr.right.accept(this) + " )";
        }
    
    @Override
    public String visitUnaryExpr(Unary expr){
        return "( " + expr.operator.lexeme + expr.right.accept(this) + " )";
    }

    @Override
    public String visitGroupingExpr(Grouping expr){
        return "( group " + expr.expression.accept(this) + " )";
    }

    @Override
    public String visitLiteralExpr(Literal expr){
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }
    
}
