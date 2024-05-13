package org.hit;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.JFrame;

public class DirectedGraphDemo extends JFrame
{
    public DirectedGraphDemo()
    {
        super("Hello, World!");

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object v1 = graph.insertVertex(parent, null, "Node A", 20, 20, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "Node B", 200, 150, 80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally
        {
            graph.setCellsMovable(false);
            graph.setCellsResizable(false);
            graph.setCellsDeletable(false);
            graph.setAllowDanglingEdges(false);
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    public static void main(String[] args)
    {
        DirectedGraphDemo frame = new DirectedGraphDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
    }
}