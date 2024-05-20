package org.hit;

import java.io.*;
import java.util.*;

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
    // 根据String查找本结点是否有对应的后继结点
    // 返回-1表示没找到，非-1表示对应的在nextlist中的索引
    public int findNextNode(String nodewords) {
        for(int i = 0; i<num ; i++){
            if(nextlist.get(i).words.equals(nodewords)){
                return i;
            }
        }
        return -1;
    }
    // 在图像上加边
    public void addedgeGraph (InteractiveGraph graph){
        WordNode tmp;
        for (int i = 0; i < num; i++) {
            tmp = nextlist.get(i);
            graph.connectNodes(this.words, tmp.words);
        }
    }
    // 在数据结构中加边
    public void addedge (WordNode nextone){
//        System.out.println(this.words + " " + nextone.words);
        int idx = findNextNode(nextone.words);
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


    public List<WordNode> getNextlist(){
        return this.nextlist;
    }
    public List<Integer> getValuelist(){
        return this.valuelist;
    }
}



