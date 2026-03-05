package Teatriprojekt;

import java.io.*;

public class Teater implements Serializable {
    private static final long serialVersionUID = 1L;

    private Iste[][] kohad;
    private String nimi;

    public Teater(int read, int istmed, String nimi) {
        this.nimi = nimi;
        kohad = new Iste[read][istmed];

        for (int i = 0; i < read; i++) {
            for (int j = 0; j < istmed; j++) {
                kohad[i][j] = new Iste(i, j);
            }
        }

    }

    public String getNimi() {
        return nimi;
    }

    public Iste getIste(int rida, int koht) {
        return kohad[rida][koht];
    }

    public int getRead() {
        return kohad.length;
    }

    public int getIstmed() {
        return kohad[0].length;
    }

    // Prindib saali vaate vastavalt kohtade arvule ja kas koht on kinni või mitte
    public void printKohad() {
        for (int i = 0; i < kohad.length; i++) {
            for (int j = 0; j < kohad[i].length; j++) {
                System.out.print(kohad[i][j].isKinni() ? "[X]" : "[ ]");
            }
            System.out.println();
        }
    }
}
