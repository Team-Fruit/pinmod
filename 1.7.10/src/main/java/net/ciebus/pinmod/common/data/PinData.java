package net.ciebus.pinmod.common.data;

public class PinData {

    public double x;
    public double y;
    public double z;
    public String player;
    public int dimId;

    public boolean isVisible;
    public double dx;
    public double dy;

    public PinData(double x, double y, double z, String player, int dimId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
        this.dimId = dimId;
        dx = 0;
        dy = 0;
        isVisible = false;
    }

    public void update(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(boolean isVisible) {
        this.isVisible = isVisible;
    }

}
