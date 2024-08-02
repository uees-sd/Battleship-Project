package com.example.Views;

import java.io.Serializable;

public class PointXY implements Serializable {

  public int x;
  public int y;
  private boolean touched;

  public PointXY(int x, int y) {
    this.x = x;
    this.y = y;
    touched = false;
  }

  public boolean isTouched() {
    return touched;
  }

  public void setTouched(boolean touched) {
    this.touched = touched;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PointXY) {
      PointXY otherPoint = (PointXY) obj;
      // Son iguales si coincide la X y la Y
      return (x == otherPoint.x && y == otherPoint.y);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return "PointXY [x=" + x + ", y=" + y + "]";
  }
}
