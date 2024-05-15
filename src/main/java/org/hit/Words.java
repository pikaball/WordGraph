package org.hit;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class WordNode{
    public String words; // 单词
    private List<WordNode> nextlist; // 有向图中的后继结点
    private List<Integer> valuelist; // 后继节点的边权值
    public int num; // next列表长度
    WordNode(String words) {
        this.num = 0;
        this.words = words;
        valuelist = new ArrayList<>();
        nextlist = new ArrayList<>();
    }
    private int findNode(String nodewords) {
        for(int i = 0; i<num ; i++){
            if(nextlist.get(i).words.equals(nodewords)){
                return i;
            }
        }
        return -1;
    }
    public void addVertexGraph(InteractiveGraph graph){
        WordNode tmp;
        for (int i = 0; i < num; i++) {
            tmp = nextlist.get(i);
            graph.connectNodes(tmp.words, this.words);
        }
    }
    public void addVertex(WordNode nextone){
        int idx = findNode(nextone.words);
        if(idx == -1){
            nextlist.add(nextone);
            valuelist.add(1);
            num += 1;
        }
        else{
            int tmp = valuelist.get(idx);
            valuelist.set(idx, tmp+1);
        }
    }
}

public class Words {
    WordNode root; // 有向图入口
    List<WordNode> nodelist; // 存储有向图所有结点的列表
    int num; // nodelist列表长度
    public Words() {
        root=null;
        nodelist = new ArrayList<>();
        num = 0 ;
        readFromFile("D:\\javaproject\\WordGraph\\src\\main\\java\\org\\hit\\test.txt");
        drawGraph();
    }
    private void drawGraph(){
        InteractiveGraph tmp = new InteractiveGraph();
        for(WordNode node : nodelist){
            tmp.addNode(node.words);
        }
        for(WordNode node : nodelist){
            node.addVertexGraph(tmp);
        }
    }
    private void readFromFile(String filename){
        File f = new File(filename);
        try(InputStream is = new FileInputStream(f);) {
            char input_c;
            String cur_input = "";
            String former_input = null;
            int total = is.available();
            for (int i = 0 ;i < total;i++){
                input_c = (char)is.read();
                if ((input_c<='z'&&input_c>='a')||(input_c<='Z'&&input_c>='A')){
                    cur_input = cur_input.concat(String.valueOf(input_c));
                }
                // 如果读到非英文字符则中断
                // 按空格处理
                else{
                    if(cur_input.length()==0){
                        continue;
                    }
                    else{
//                        System.out.println(cur_input);
                        if (findNode(cur_input)==null){
                            nodelist.add(new WordNode(cur_input));
                            num += 1;
                        }

                        if (former_input == null){
                            root = findNode(cur_input);
                            former_input = cur_input;
                        }
                        else{
                            WordNode f_node = findNode(former_input);
                            WordNode c_node = findNode(cur_input);
//                            System.out.println(tmp.words);
                            if (c_node!=null && f_node!= null){
                                c_node.addVertex(f_node);
                            }
                            former_input = cur_input;
                        }
                        cur_input = "";
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private WordNode findNode(String words){
        for (WordNode i : nodelist){
            if (i.words.equals(words)){
                return i;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Words::new);
    }
}
