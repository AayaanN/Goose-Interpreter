package com.goose;

abstract class Expr{

    interface Visitor<R>{
        R visitBinaryExpr(Binary expr);
        R visitUnaryExpr(Unary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
    }

    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
          this.left = left;
          this.operator = operator;
          this.right = right;
        }
    
        final Expr left;
        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
      }

      static class Unary extends Expr {
        Unary(Expr right, Token operator) {
          this.right = right;
          this.operator = operator;
        }
    
        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
      }

      static class Grouping extends Expr {
        Grouping(Expr expression) {
          this.expression = expression;
        }
    
        final Expr expression;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
      }

      static class Literal extends Expr {
        Literal(Object value) {
          this.value = value;

        }

        final Object value;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
      }
    
    abstract <R> R accept(Visitor<R> visitor);

}
