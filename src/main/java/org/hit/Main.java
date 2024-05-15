package org.hit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Words root = new Words("D:\\javaproject\\WordGraph\\src\\main\\java\\org\\hit\\test.txt");
        List<String> bridgewords = new ArrayList<>();
//        root.showDirectedGraph(root);
        bridgewords = root.queryBridgeWords("exciting","synergies",1);
        System.out.println(root.generateNewText("seek to explore new and exciting synergies"));
        System.out.println(root.calcShortestPath("To","and"));
        System.out.println(root.randomWalk());
        // System.out.println("Hello");
    }
}