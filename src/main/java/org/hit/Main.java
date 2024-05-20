package org.hit;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Need input text file.\n");
            System.exit(1);
        }
        String inputFilePath = args[0];
        Words root = new Words(inputFilePath);
        root.showDirectedGraph(root);
//        bridgewords = root.queryBridgeWords("strange","new",1);
//        System.out.println(root.generateNewText("seek to explore new and exciting synergies"));
//        System.out.println(root.calcShortestPath("To","and"));
//        System.out.println(root.randomWalk());
        System.out.println("Service running\n");
    }
}