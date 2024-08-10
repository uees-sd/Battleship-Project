package com.example.Interfaces;

import java.io.Serializable;
import java.util.ArrayList;

public interface ModelObserver extends Serializable {
  void boardUpdated();

  void turnChanged();

  void shipSunkUpdated(ArrayList<String> sunkShips);

  void gameFinished(String winner);
}
