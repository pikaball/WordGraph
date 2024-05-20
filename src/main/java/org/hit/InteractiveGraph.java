package org.hit;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class InteractiveGraph extends JFrame {
    private mxGraph graph;
    private Object parent;
    private mxGraphComponent graphComponent;
    public Words graph_root;

    public InteractiveGraph() {
        super("Interactive Directed Graph");

        graph = new mxGraph();
        parent = graph.getDefaultParent();


        // 界面布局：顶部为操作区域，中部为图形展示区域
        setLayout(new BorderLayout());

        graphComponent = new mxGraphComponent(graph);
        graphComponent.setBorder(BorderFactory.createEmptyBorder(10, 10,0,0));

        JPanel controlPanel = new JPanel(new FlowLayout());

        // 添加桥接词查询 UI
        JTextField word1Field = new JTextField(10);
        JTextField word2Field = new JTextField(10);
        JButton queryButton = new JButton("Query Bridge Words");
        JTextArea resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);

        queryButton.addActionListener(e -> {
            String word1 = word1Field.getText();
            String word2 = word2Field.getText();
            List<String> bridgeWords = graph_root.queryBridgeWords(word1, word2, 0); // logmode 设置为 0，防止控制台输出
            if (bridgeWords != null && !bridgeWords.isEmpty()) {
                resultArea.setText("[+] The bridge words from \""+word1+"\" to \""+word2+"\" is: "+String.join(", ", bridgeWords));
            } else {
                resultArea.setText("[-] No bridge words found.");
            }
            word1Field.setText("");
            word2Field.setText("");
        });

        JTextField inputTextField = new JTextField(20);
        JButton generateTextButton = new JButton("Generate New Text");

        generateTextButton.addActionListener(e -> {
            // 调用 generateNewText 方法并显示结果
            String inputText = inputTextField.getText();
            String newText = graph_root.generateNewText(inputText);
            resultArea.setText("[+] Raw Input: " + inputText + "\n" + (newText != null ? "[+] Generate Text: "+newText : "[-] No new text generated."));
            inputTextField.setText("");
        });

        JTextField pathStart = new JTextField(10);
        JTextField pathEnd = new JTextField(10);
        JButton pathCalcButton = new JButton("Calc shortest path");
        pathCalcButton.addActionListener(e -> {
            resultArea.setText("");
            String startNode = pathStart.getText();
            String endNode = pathEnd.getText();
            if (startNode.equals("") || endNode.equals("")) return;
            resultArea.append("[+] Calcing shortest path from \""+ startNode +"\" to \""+ endNode +"\"\n");
            String result = graph_root.calcShortestPath(startNode, endNode);
            if (result == null) resultArea.append("[-] No path found");
            else resultArea.append("[+] " + result);
            pathStart.setText("");
            pathEnd.setText("");
        });

        JButton randomWalkButton = new JButton("Random Walk");
        randomWalkButton.addActionListener(e -> {
            resultArea.setText("[+] "+graph_root.randomWalk());
        });

        JButton savaGraphButton = new JButton("Save Graph ...");
        savaGraphButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            // 设置默认的保存文件名称
            fileChooser.setSelectedFile(new File("MyGraph.png"));

            // 显示保存文件的对话框
            int userSelection = fileChooser.showSaveDialog(this);

            // 当用户确认保存操作时，获取选择的文件，并调用保存图像的方法
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                saveGraphAsImage(graphComponent, fileToSave.getAbsolutePath());
            }
        });

        controlPanel.setLayout(new GridLayout(4, 1));

        // 每个 JPanel 包含一行的控件
        JPanel row1 = new JPanel(new FlowLayout());
        JPanel row2 = new JPanel(new FlowLayout());
        JPanel row3 = new JPanel(new FlowLayout());
        JPanel row4 = new JPanel(new FlowLayout());

        row1.add(new JLabel("Word 1:"));
        row1.add(word1Field);
        row1.add(new JLabel("Word 2:"));
        row1.add(word2Field);
        row1.add(queryButton);
        row2.add(new JLabel("Input Text:"));
        row2.add(inputTextField);
        row2.add(generateTextButton);
        row3.add(new JLabel("Path Start:"));
        row3.add(pathStart);
        row3.add(new JLabel("Path End:"));
        row3.add(pathEnd);
        row3.add(pathCalcButton);
        row4.add(randomWalkButton);
        row4.add(savaGraphButton);

        controlPanel.add(row1);
        controlPanel.add(row2);
        controlPanel.add(row3);
        controlPanel.add(row4);

        add(controlPanel, BorderLayout.NORTH);
        add(graphComponent, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.SOUTH);

        // 字体
        Font font = new Font("Consolas", Font.BOLD, 15);
        resultArea.setFont(font);
        inputTextField.setFont(font);
        word1Field.setFont(font);
        word2Field.setFont(font);
        pathStart.setFont(font);
        pathEnd.setFont(font);
        resultArea.setMargin(new Insets(10, 10, 10, 10));

        // 设置间距
//        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        nodePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
//        edgePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // 窗口属性设置
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void applyLayout() {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setOrientation(SwingConstants.WEST);
        layout.execute(graph.getDefaultParent());
    }

    private Object findVertexByName(String name) {
        for (Object vertex : graph.getChildVertices(parent)) {
            if (graph.getModel().getValue(vertex).equals(name)) {
                return vertex;
            }
        }
        return null;
    }

    public void addNode(String nodeName) {
        if (!nodeName.isEmpty()) {
            graph.getModel().beginUpdate();
            try {
                graph.insertVertex(parent, null, nodeName, 0, 0, 80, 30);
                applyLayout();
            } finally {
                graph.getModel().endUpdate();
            }
        }
    }

    public void connectNodes(String fromNode, String toNode) {
        if (!fromNode.isEmpty() && !toNode.isEmpty()) {
            graph.getModel().beginUpdate();
            try {
                Object source = findVertexByName(fromNode);
                Object target = findVertexByName(toNode);
                if (source != null && target != null) {
                    graph.insertEdge(parent, null, "", source, target);
                    applyLayout();
                }
            } finally {
                graph.getModel().endUpdate();
            }
        }
    }

    public void saveGraphAsImage(mxGraphComponent graphComponent, String filePath) {
        BufferedImage image = new BufferedImage(graphComponent.getWidth(), graphComponent.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphComponent.paintAll(graphics);
        try {
            ImageIO.write(image, "PNG", new File(filePath));
            System.out.println("Graph image saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while saving graph image: " + e.getMessage());
        } finally {
            graphics.dispose();
        }
    }
}

