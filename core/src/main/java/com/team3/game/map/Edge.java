package com.team3.game.map;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Edge class represents a connection between two nodes in a graph.
 */
public class Edge implements Connection<Node> {
  float cost;
  Node fromNode;
  Node toNode;

  /**
   * Defines cost and nodes to travel between - an edge with its heuristic value.
   *
   * @param cost Cost of edge
   * @param fromNode Origin node
   * @param toNode Destination node
   */
  public Edge(float cost, Node fromNode, Node toNode) {
    this.cost = cost;
    this.fromNode = fromNode;
    this.toNode = toNode;
  }

  @Override
  public float getCost() {
    return this.cost;
  }

  @Override
  public Node getFromNode() {
    return this.fromNode;
  }

  @Override
  public Node getToNode() {
    return this.toNode;
  }

}
