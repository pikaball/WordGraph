package org.hit;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import javax.swing.JFrame;

public class JGraphXExample extends JFrame {

    public JGraphXExample() {
        super("JGraphX Directed Graph Example");

        // 创建一个mxGraph对象，并获取它的默认父对象
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        // 更新图形
        graph.getModel().beginUpdate();
        try {
            // 添加节点
            Object v1 = graph.insertVertex(parent, null, "Node 1", 0, 0, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "Node 2", 0, 0, 80, 30);
            Object v3 = graph.insertVertex(parent, null, "Node 3", 0, 0, 80, 30);

            // 添加边
            graph.insertEdge(parent, null, "", v1, v2);
            graph.insertEdge(parent, null, "", v2, v3);
            graph.insertEdge(parent, null, "", v3, v1);

            // 应用布局
            mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
            layout.execute(graph.getDefaultParent());
        } finally {
            // 完成更新
            graph.getModel().endUpdate();
        }

        // 将图形添加到界面
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    public static void main(String[] args) {
        JGraphXExample frame = new JGraphXExample();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }
}