package Teatriprojekt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Loome erinevad teatrid
        Teater Vanemuine = new Teater(3,15, "Vanemuine");
        Teater Draamateater = new Teater(5,13, "Draamateater");
        Teater Ugala = new Teater(4,15, "Ugala");
        Teater Estonia = new Teater(2,15, "Estonia");
        Teater Linnateater = new Teater(6,20, "Linnateater");

        // Lisame erinevad teatrid listi
        List<Teater> teatrid = new ArrayList<>();
        teatrid.add(Vanemuine);
        teatrid.add(Draamateater);
        teatrid.add(Ugala);
        teatrid.add(Estonia);
        teatrid.add(Linnateater);

        Scanner scanner = new Scanner(System.in);
        Valimine.avaFail();
        boolean running = true;

        System.out.println("Teatripiletid");
        System.out.println("Vali üks järgmistest teatritest:");
        for (Teater t : teatrid) {
            System.out.print(t.getNimi() + " "); // Kuvame iga teatri nime listist
        }
        System.out.println();

        while (running) {

            // Kasutaja valib teatri
            System.out.println("Sisesta teater: ");
            String steater = scanner.nextLine().toLowerCase();

            Teater valitudTeater = null; // Määrame valitud teatri

            if (steater.equals("vanemuine")) {
                valitudTeater = Vanemuine;
            } else if (steater.equals("draamateater")) {
                valitudTeater = Draamateater;
            }else if (steater.equals("ugala")) {
                valitudTeater = Ugala;
            }else if (steater.equals("estonia")) {
                valitudTeater = Estonia;
            }else if (steater.equals("linnateater")) {
                valitudTeater = Linnateater;
            }else{
                System.out.println("Teie teatrit pole meie programmis. Sisesta teater valikust.");
                continue;
            }

            valitudTeater.printKohad();//Valitud teatri kohad
            Valimine valimine = new Valimine(valitudTeater);//Valitud teatrile Valimine

            // Kohtade arvu sisestamine
            System.out.println("Sisesta kohtade arv või 0 et lõpetada");
            int soov = Integer.parseInt(scanner.nextLine());

            if (soov == 0) {
                System.out.println("Programm lõpetab.");
                running = false;
            } else {
                String valik = valimine.valimine(soov);
                System.out.println(valik);
            }

            // Pärast broneerimist
            if (running) {
                System.out.println("Kas soovid jätkata? (jah/ei)");
                String vastus = scanner.nextLine().toLowerCase();
                if (!vastus.equals("jah") && !vastus.equals("ja")) {
                    running = false;
                    System.out.println("Programm lõpetab.");
                }
            }
        }
        scanner.close();
    }
}

