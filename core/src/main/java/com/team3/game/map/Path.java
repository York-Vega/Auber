package com.team3.game.map;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;
import java.util.Iterator;

/**
 * Path class represents a path between two nodes in a graph.
 */
public class Path implements GraphPath<Node> {
  Array<Node> nodes;

  public Path() {
    nodes = new Array<Node>();
  }

  @Override
  public Iterator<Node> iterator() {
    return nodes.iterator();
  }

  @Override
  public int getCount() {
    return nodes.size;
  }

  @Override
  public Node get(int index) {
    return nodes.get(index);
  }

  @Override
  public void add(Node node) {
    nodes.add(node);
  }

  @Override
  public void clear() {
    nodes.clear();
  }

  @Override
  public void reverse() {
    nodes.reverse();
  }
}
