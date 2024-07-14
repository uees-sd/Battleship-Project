package Views;

import java.io.Serializable;

public class Ship implements Serializable {
    private final int length;
    private ShipStatus status;

    private enum Direction {
        HORIZONTAL, VERTICAL
    }

    public enum ShipStatus {
        ALIVE, DESTROYED
    }

    public Ship(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public ShipStatus getShipStatus() {
        return status;
    }
}