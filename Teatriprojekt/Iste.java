package Teatriprojekt;

import java.io.Serializable;

public class Iste implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rida;
    private int koht;
    private boolean kinni; // Kontrollib, kas koht on broneeritud

    public Iste(int rida, int koht) {
        this.rida = rida;
        this.koht = koht;
        this.kinni = false;
    }

    public boolean isKinni() {
        return kinni;
    }

    public void paneKinni() {
        this.kinni = true;
    }

    public void mitteKinni() {
        this.kinni = false;
    }

    public int getRida() {
        return rida;
    }

    public int getKoht() {
        return koht;
    }

    @Override
    public String toString() {
        return "Rida: " + (rida+1) + ", Koht: " + (koht+1);
    }
}
