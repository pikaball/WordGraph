package org.hit;

import java.util.*;
import java.io.*;

public class Words {
    WordNode root; // 有向图入口
    List<WordNode> nodelist; // 存储有向图所有结点的列表
    public InteractiveGraph graph;
    int num; // nodelist列表长度
    public Words(String filename) {
        root=null;
        nodelist = new ArrayList<>();
        num = 0 ;
        readFromFile(filename);
    }

    // 需求3 查询桥接词
    // 返回一个包含着所有桥接词的List 或者 null
    public List<String> queryBridgeWords(String word1,String word2,int logmode) {
        WordNode node1=null;
        WordNode node2=null;
        List<String> bridgeWords = new ArrayList<>();
        for (WordNode node : this.nodelist) {
            if (node.words.equals(word1)) {
                node1 = node;
                break;
            }
        }
        for (WordNode node : this.nodelist) {
            if (node.words.equals(word2)) {
                node2 = node;
                break;
            }
        }
        if (node1 == null && node2 != null){
            // 输出报错信息
            if (logmode == 1) System.out.println("[-] No \""+word1+"\""+"in the graph");
            return null;
        }
        if (node1 != null && node2 == null){
            if (logmode == 1) System.out.println("[-] No \""+word2+"\" in the graph");
            return null;
        }
        if(node1 == null){
            if (logmode == 1) System.out.println("[-] No \""+word1+"\" and \""+ word2 +"\" in the graph");
            return null;
        }
        List<WordNode> middlelist = new ArrayList<>(node1.getNextlist());
        for (WordNode node : middlelist){
//            System.out.println(node.words);
            if (node.findNextNode(word2) != -1){
                bridgeWords.add(node.words);
            }
        }
        if (bridgeWords.isEmpty()){
            if (logmode == 1) System.out.println("[-] No bridge words from \""+word1+"\" to \""+word2+"\"");
        }
        else{
            if (logmode == 1) System.out.print("[+] The bridge words from \""+word1+"\" to \""+word2+"\" is: ");
            for (String output: bridgeWords){
                if (logmode == 1) System.out.print(output+" ");
            }
            if (logmode == 1) System.out.print("\n");
        }
        return bridgeWords;
    }

    // 需求2 绘制有向图
    public void showDirectedGraph(Words entry){
        graph = new InteractiveGraph();
        graph.graph_root = this;
        for(WordNode node : entry.nodelist){
            graph.addNode(node.words);
        }
        for(WordNode node : entry.nodelist){
            node.addedgeGraph(graph);
        }
    }

    // 需求4 根据桥接词生成新文本
    public String generateNewText(String inputtext){
        String[] parts = inputtext.split(" ");
        StringBuilder output = new StringBuilder();
        List<String> bridgeWords;
        Random rand = new Random();
        if (parts.length == 1 || parts.length == 0){
            return null;
        }
        for (int i = 0; i < parts.length - 1; i++){
            output.append(parts[i]);
            output.append(" ");
            bridgeWords = queryBridgeWords(parts[i], parts[i+1], 0);
            if(bridgeWords==null){
                continue;
            }
            else{
                int choice = rand.nextInt(bridgeWords.size());
                output.append(bridgeWords.get(choice));
                output.append(" ");
            }
        }
        output.append(parts[parts.length-1]);
        return output.toString();
    }

    //需求5 计算两个单词之间最短路径
    public String calcShortestPath(String word1, String word2){
        StringBuilder output = new StringBuilder();
        int[] dis = new int[num];
        int[] from = new int[num];
        for(int i = 0; i < num; i++){
            from[i] = -1;
            dis[i] = -1;
        }

        Deque<Integer> queue = new ArrayDeque<>();
        WordNode start = this.findNode(word1);
        int start_idx = this.findIndex(start);
        WordNode end = this.findNode(word2);
        int end_idx = this.findIndex(end);
        int tmp_idx;
        WordNode cur_node;
        int cur_idx;
        int next_idx;

        if (start == null || end == null){
            return null;
        }
        
        // 宽搜
        for (WordNode node: start.getNextlist()) {
            tmp_idx = this.findIndex(node);
            queue.add(tmp_idx);
            dis[tmp_idx] = start.getValuelist().get(start.getNextlist().indexOf(node));
            from[tmp_idx] = start_idx;
        }
        while (!queue.isEmpty()){
            cur_idx = queue.poll();
            if (cur_idx == start_idx){
                continue;
            }
            cur_node = this.nodelist.get(cur_idx);
            for (WordNode next : cur_node.getNextlist()){
                next_idx = this.findIndex(next);
                int diff = cur_node.getValuelist().get(cur_node.getNextlist().indexOf(next));
                if (dis[next_idx] == -1){
                    dis[next_idx] = dis[cur_idx] + diff;
                    from[next_idx] = cur_idx;
                    queue.add(next_idx);
                }
                else{
                    if (dis[next_idx] > dis[cur_idx] + diff){
                        dis[next_idx] = dis[cur_idx] + diff;
                        from[next_idx] = cur_idx;
                        queue.add(next_idx);
                    }
                    else{
                        continue;
                    }
                }
            }

        }

//        for (int i = 0;i < num;i++){
//            System.out.print(nodelist.get(i).words);
//            System.out.print(dis[i]+" ");
//        }

        // 如果不可达则返回null
        if (dis[end_idx] == -1){
            return null;
        }
        // 否则返回一个String，标识最短路径
        List<String> res = new ArrayList<>();
        tmp_idx = end_idx;
        while (tmp_idx!=start_idx){
            res.add(this.nodelist.get(tmp_idx).words);
            tmp_idx = from[tmp_idx];
        }

        output.append(start.words);
        for(int i = res.size()-1;i>=0;i--){
            output.append("->");
            output.append(res.get(i));
        }
        return output.toString();
    }

    // 需求6 随机游走
    // 用int列表存储边的映射
    public String randomWalk(){
        StringBuilder output = new StringBuilder();
        List<Integer> used_edge = new ArrayList<>();
        Random rand = new Random();
        int start_idx = rand.nextInt(num);
        int next_idx;
        int cur_idx = start_idx;
        int r;

        WordNode next_node;
        WordNode cur_node = this.nodelist.get(start_idx);

        output.append(this.nodelist.get(start_idx).words);
        output.append(" ");
        while(true){
            r = rand.nextInt(cur_node.getNextlist().size());
            next_node = cur_node.getNextlist().get(r);
            next_idx = this.findIndex(next_node);
            if(used_edge.contains((cur_idx<<16)+next_idx)){
                output.append(next_node.words);
                break;
            }
            else{
                used_edge.add((cur_idx<<16)+next_idx);
                output.append(next_node.words);
                output.append(" ");
                cur_node = next_node;
                cur_idx = next_idx;
            }
        }
        return output.toString();
    }


//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(Words::new);
//    }

    // 基础设施
    private WordNode findNode(String words){
        for (WordNode i : nodelist){
            if (i.words.equals(words)){
                return i;
            }
        }
        return null;
    }
    private int findIndex(WordNode node){
        for (int i =0; i < nodelist.size(); i++){
            if (nodelist.get(i).words.equals(node.words)){
                return i;
            }
        }
        return -1;
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
                            if (c_node!=null && f_node!= null){
                                f_node.addedge(c_node);
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
}
