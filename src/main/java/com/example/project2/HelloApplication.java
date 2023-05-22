package com.example.project2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HelloApplication extends Application {


    File file;
    Hyperlink hlp = new Hyperlink();

    public static void main(String[] args) {
        launch();
    }

    public static boolean checkBalance(String s) {
        CurssorArrayStack<String> stack = new CurssorArrayStack<>(30);
        String[] c = s.split(" ");
        for (String value : c) {
            switch (value) {
                case "<242>", "<files>", "<equations>", "<file>", "<equation>" -> stack.push(value);
                case "</242>", "</files>", "</equations>", "</file>", "</equation>" -> {
                    if (stack.isEmpty())
                        return false;
                    String o = stack.pop();
                    if (!(value.equals("</242>") && o.equals("<242>") ||
                            value.equals("</files>") && o.equals("<files>") ||
                            value.equals("</equations>") && o.equals("<equations>") ||
                            value.equals("</file>") && o.equals("<file>") ||
                            value.equals("</equation>") && o.equals("<equation>")))
                        return false;
                }

            }
        }

        return stack.isEmpty();
    }

    private static String filter(String s) {
        StringBuilder sl = new StringBuilder();

        String[] c = s.trim().split(" ");
        if (c[0].equals("<242>") || c[0].equals("</242>") ||
                c[0].equals("<files>") || c[0].equals("</files>")
                || c[0].equals("<equations>") || c[0].equals("</equations>"))
            sl.append(c[0]).append(" ");


        if (c[0].equals("<file>") || c[c.length - 1].equals("</file>"))
            sl.append(c[0]).append(" ").append(c[c.length - 1]).append(" ");


        if (c[0].matches("<equation>[0-9]+") || c[0].matches("<equation>\\*+") || c[c.length - 1].matches("\\*+</equation>") || c[0].matches("<equation>\\(+") || c[0].matches("<equation>]+") || c[0].matches("<equation>\\[+") || c[c.length - 1].matches("\\)+</equation>")
                || c[0].matches("<equation>[a-zA-Z]+") || c[c.length - 1].matches("[a-zA-Z]+</equation>") || c[c.length - 1].matches("[0-9]+</equation>") || c[0].matches("<equation>-+") || c[c.length - 1].matches("-+</equation>") || c[0].matches("<equation>\\++") || c[c.length - 1].matches("\\++</equation>"))
            sl.append(c[0].replaceAll("[0-9]", "").replaceAll("\\(", "").replaceAll("\\[", "").replaceAll("\\*", "").replaceAll("-", "").replaceAll("\\+", "")).append(" ")
                    .append(c[c.length - 1].replaceAll("[0-9]", "").replaceAll("\\)", "").replaceAll("]", "").replaceAll("\\*", "").replaceAll("-", "").replaceAll("\\+", "")).append(" ");

            //sl.append(c[0]).append(" ").append(c[c.length - 1]).replace(10, 13, " ").append(" ");
        else if (c[0].matches("<equation>") || c[c.length - 1].matches("</equation>"))
            sl.append(c[0]).append(" ").append(c[c.length - 1]).append(" ");


        return sl.toString();

    }

    public static String[] print(File file) {
        StringBuilder str = new StringBuilder();
        StringBuilder str2 = new StringBuilder();
        StringBuilder str3 = new StringBuilder();
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        while (sc.hasNext()) {
            String s = sc.nextLine().trim();
            if (!s.equals("")) {
                str.append(filter(s));
                String[] d = s.trim().split(" ");
                String[] m = s.trim().split("[><]");
                if (m[0].equals("")) {
                    if (m[1].equals("equation")) {
                        String temp = toPostFix(m[2]);
                        str3.append((m[2])).append(" --> ");
                        str3.append(temp);
                        if (!(temp.equals("Unbalanced"))) {
                            str3.append(" --> ").append(evaluatePost(temp)).append("\n");
                        } else
                            str3.append("\n");
                    }
                }
                if (d[0].equals("<file>"))
                    str2.append(d[1]).append("\n");
            }

        }

        String[] s = new String[3];
        s[0] = str.toString();
        s[1] = str2.toString();
        s[2] = str3.toString();
        return s;
    }

    public static int Operator(String x) {
        return switch (x) {
            case "^" -> 2;
            case "*", "/" -> 1;
            case "+", "-" -> 0;
            default -> -1;
        };
    }

    public static String toPostFix(String infix) {
        CurssorArrayStack<String> transform = new CurssorArrayStack<>(20);
        String topOp;
        StringBuilder postFix = new StringBuilder();
        String[] c = infix.split(" ");
        for (String s : c) {
            if (s.matches("[0-9]+"))
                postFix.append(s).append(" ");
            else if (s.equals("^"))
                transform.push(s);
            else if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) {
                while (!(transform.isEmpty()) && Operator(s) <= Operator(transform.peek())) {

                    postFix.append(transform.peek()).append(" ");
                    transform.pop();
                }
                transform.push(s);

            } else if (s.equals("("))
                transform.push(s);
            else if (s.equals(")")) {
                topOp = transform.pop();
                if (topOp != null) {
                    while (!(topOp.equals("("))) {
                        postFix.append(topOp).append(" ");
                        topOp = transform.pop();
                    }
                } else
                    /* return "this equation isn't balanced there is a \\) without open";*/return "Unbalanced";
            }

        }

        while (!transform.isEmpty()) {
            if (!(transform.peek().equals("("))) {
                topOp = transform.pop();
                postFix.append(topOp).append(" ");

            } else
                return  /* return "This equation isn't balanced there is a \\( without close";*/ "Unbalanced";

        }

        return postFix.toString();
    }


    private static double evaluate(double op1, double op2, String opp) {
        if (opp.equals("+"))
            return op1 + op2;
        if (opp.equals("-"))
            return op1 - op2;
        if (opp.equals("*"))
            return op1 * op2;
        if (opp.equals("^"))
            return Math.pow(op1, op2);
        if (opp.equals("/"))
            return op1 / op2;
        return 0;
    }

    public static String evaluatePost(String post) {
        double op1, op2, result = 0;
        CurssorArrayStack<Double> stack = new CurssorArrayStack<>(20);
        String[] next = post.trim().split(" ");
        for (String s : next) {
            if (s.matches("[0-9]+"))
                stack.push(Double.parseDouble(s));
            else if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^")) {
                if (!stack.isEmpty()) {
                    op2 = stack.pop();
                    if (!stack.isEmpty()) {
                        op1 = stack.pop();
                        result = evaluate(op1, op2, s);
                        stack.push(result);
                    } else
                        return /*if there is an operator without an operand the equation is invalid*/"Invalid";
                } else
                    return /*if there is an operand without an operator the equation is invalid*/"Invalid";

            }

        }
        stack.pop();
        if (stack.peek() != null) {
            return /*if there is an extra operand the equation is invalid*/ "Invalid";
        }

        return String.valueOf(result);
    }

    @Override
    public void start(Stage stage) {
        CurssorArrayStack<String> cs = new CurssorArrayStack<>(20);
        StackPane pane = new StackPane();

        pane.setBorder(new Border(new BorderStroke(Color.DARKORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(4, 4, 4, 4))));

        FileChooser chooser = new FileChooser();
        Button back = new Button("Back");
        back.setDisable(true);
        back.setStyle("-fx-background-radius: 7px;");


        Button load = new Button("Load");
        load.setStyle("-fx-background-radius: 7px;");
        TextField path = new TextField();
        path.setEditable(false);
        path.setPrefWidth(250);
        path.setMaxWidth(250);
        path.setMaxWidth(Double.MAX_VALUE);
        HBox hb1 = new HBox(10);
        hb1.getChildren().addAll(back, path, load);
        hb1.setAlignment(Pos.CENTER);

        Label equL = new Label("Equations");
        equL.setAlignment(Pos.CENTER_LEFT);
        equL.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        HBox equHB = new HBox();
        equHB.getChildren().addAll(equL);
        equHB.setAlignment(Pos.CENTER_LEFT);

        TextArea equ = new TextArea();
        equ.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        hlp.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        equ.setEditable(false);
        equ.setMaxHeight(150);
        equ.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(4, 4, 4, 4))));
        equ.setMaxWidth(437);
        VBox vb1 = new VBox(10);
        vb1.getChildren().addAll(equHB, equ);
        vb1.setAlignment(Pos.CENTER);

        Label fileL = new Label("Files");
        fileL.setFont(Font.font("Verdana", FontWeight.BOLD, 15));


        ListView<String> listView = new ListView<>();
        listView.setMaxHeight(120);
        listView.setStyle("-fx-font: 10pt \"Verdana\"; -fx-font-weight: bold;");
        listView.setEditable(false);
        listView.setBorder(new Border(new BorderStroke(Color.MEDIUMORCHID, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(4, 4, 4, 4))));
        listView.setMaxWidth(437);
        VBox vb2 = new VBox(10);
        hlp.setAlignment(Pos.CENTER_LEFT);
        vb2.getChildren().addAll(fileL, listView);
        vb2.setAlignment(Pos.CENTER_LEFT);

        VBox vb3 = new VBox(10);
        vb3.getChildren().addAll(hb1, vb1, vb2);
        pane.getChildren().addAll(vb3);


        Scene scene = new Scene(pane, 450, 450);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();

        load.setOnAction(e -> {
            listView.getItems().clear();
            file = chooser.showOpenDialog(stage);
            if (file != null) {
                String[] fi = print(file);
                String[] temp = fi[1].split("\n");

                hlp.setText(temp[0]);
                hlp.setVisible(true);
                cs.push(file.getPath());
                path.setText(file.getPath());
                back.setDisable(false);
                if (checkBalance(fi[0])) {
                    if (!fi[1].equals("")) {
                        for (String s : temp) {
                            listView.getItems().add(s);
                        }
                    } else
                        listView.getItems().clear();
                    equ.setText(fi[2]);
                } else {
                    equ.setText("this file isn't balanced");
                    listView.getItems().clear();
                }
            }
        });

        back.setOnAction(e -> {
            listView.getItems().clear();
            cs.pop();
            if (!cs.isEmpty())
                file = new File(cs.peek());
            if (cs.peek() == null) {
                //   cs.push(na);
                back.setDisable(true);
                equ.clear();
                listView.getItems().clear();
                path.clear();

            } else {
                path.setText(cs.peek());
                String[] printBack = print(file);
                String[] temp = printBack[1].split("\n");
                if (checkBalance(printBack[0])) {
                    equ.setText(printBack[2]);
                    if (!printBack[1].equals("")) {
                        for (String s : temp) {
                            listView.getItems().add(s);
                        }
                    } else
                        listView.getItems().clear();
                } else {
                    equ.setText("This file isn't balanced");
                    listView.getItems().clear();
                }
            }

        });

        listView.setOnMousePressed(e -> {
            if (!listView.getItems().isEmpty() && listView.getSelectionModel().getSelectedItem() != null) {
                cs.push(listView.getSelectionModel().getSelectedItem());
                listView.getItems().clear();
                file = new File(cs.peek());
                path.setText(cs.peek());
                if (file.exists()) {
                    String[] printGo = print(file);
                    String[] temp = printGo[1].split("\n");
                    if (checkBalance(printGo[0])) {
                        equ.setText(printGo[2]);
                        if (!printGo[1].equals("")) {
                            for (String s : temp) {
                                listView.getItems().add(s);
                            }
                        } else
                            listView.getItems().clear();


                    } else {
                        equ.setText("This file isn't balanced");
                        listView.getItems().clear();
                    }
                } else
                    equ.setText("There is no file in this path");
            }
        });

    }

}