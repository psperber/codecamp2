package de.uks.challenger.ui.graph;

import com.jjoe64.graphview.GraphViewDataInterface;

/**
 * Data pair for graph view
 *
 */
public class GraphData implements GraphViewDataInterface {
    private double x,y;
    
    public GraphData(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }
}