package org.hit;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.*;
import java.awt.*;

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
        add(graphComponent, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // 添加节点控件
        JLabel nodeNameLabel = new JLabel("Node Name:");
        JTextField nodeNameField = new JTextField();
        nodeNameField.addActionListener(e -> addNode(nodeNameField.getText()));
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(e -> addNode(nodeNameField.getText()));
        JPanel nodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nodePanel.add(nodeNameLabel);
        nodePanel.add(nodeNameField);

        // 添加边控件
        JLabel fromNodeLabel = new JLabel("From Node:");
        JLabel toNodeLabel = new JLabel("To Node:");
        JTextField fromNodeField = new JTextField();
        JTextField toNodeField = new JTextField();
        fromNodeField.addActionListener(e -> connectNodes(fromNodeField.getText(), toNodeField.getText()));
        toNodeField.addActionListener(e -> connectNodes(fromNodeField.getText(), toNodeField.getText()));
        JButton addEdgeButton = new JButton("Connect Nodes");
        addEdgeButton.addActionListener(e -> connectNodes(fromNodeField.getText(), toNodeField.getText()));
        JPanel edgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        edgePanel.add(fromNodeLabel);
        edgePanel.add(fromNodeField);
        edgePanel.add(toNodeLabel);
        edgePanel.add(toNodeField);

        // 调整输入框大小
        nodeNameField.setPreferredSize(new Dimension(200, 26));
        fromNodeField.setPreferredSize(new Dimension(90, 26));
        toNodeField.setPreferredSize(new Dimension(90, 26));

        // 将按钮面板添加至操作面板
        controlPanel.add(nodePanel);
        controlPanel.add(addNodeButton);
        controlPanel.add(edgePanel);
        controlPanel.add(addEdgeButton);

        // 设置间距
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nodePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        edgePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // 把控制面板加到窗口顶部
        add(controlPanel, BorderLayout.NORTH);

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
}

