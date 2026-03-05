package Teatriprojekt;

import org.w3c.dom.ls.LSOutput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Valimine {
    private Teater teater;
    private Random random;
    private List<String> sobivad; // Kõik sobivad kombinatsioonid

    public Valimine(Teater teater) {
        this.teater = teater;
        this.random = new Random();
        this.sobivad = new ArrayList<>();
    }

    // Avab faili broneeringud.txt kirjutamiseks.
    public static void avaFail() {
        try (FileWriter writer = new FileWriter("broneeringud.txt", false)) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Viga faili lähtestamisel: " + e.getMessage());
        }
    }

    // Leiab kõik piletite arvule sobivad kombiatsioonid
    public void misSobivad(int arv) {
        sobivad.clear();

        for (int rida = 0; rida < teater.getRead(); rida++) {
            for (int alg = 0; alg <= teater.getIstmed() - arv; alg++) {

                boolean vaba = true;

                for (int i = 0; i < arv; i++) {
                    if (teater.getIste(rida, alg + i).isKinni()) {
                        vaba = false;
                        break;
                    }
                }

                if (vaba) {
                    if (arv == 1) {
                        sobivad.add("Rida " + (rida + 1) + ", Koht " + (alg + 1));
                    } else {
                        sobivad.add("Rida " + (rida + 1) + ", Kohad " + (alg + 1) + "-" + (alg + arv));
                    }
                }
            }
        }
    }

    // Kohtade valimine
    public String valimine(int arv) {
        Scanner scanner = new Scanner(System.in);

        // Kasutaja valib kas pakutakse kohti suvaliselt või kas kasutaja valib kohad ise
        System.out.println("Kas soovid, et süsteem pakuks Teile suvalisi kohti (s) või valite kohad ise (i)?");
        String valikumeetod = scanner.nextLine().toLowerCase();

        if (valikumeetod.equals("i")) {
            // Kasutaja valib ühe koha
            return broneeriKoht(scanner);
        }


        // kui kasutaja valis "s", siis suvaline valik
        misSobivad(arv);

        // Kui sobivaid kohti pole
        if (sobivad.isEmpty()) {
            return "Kõrvuti olevaid kohti ei ole.";
        }

        // Kui kasutaja soovib suvaliselt kohti ja leidub sobivaid kohti
        while (!sobivad.isEmpty()) {
            String valik = sobivad.get(random.nextInt(sobivad.size()));
            // Pakub sobivatest kasutajale broneerimise võimalust
            System.out.println("Kas soovid broneerida: (jah/ei) " + valik);

            String kasTahab = scanner.nextLine().toLowerCase();

            //Kui kasutaja vastab jah siis salvestab broneeringu
            if (kasTahab.equals("jah") || kasTahab.equals("ja")) {
                String[] osad = valik.split(", ");
                int rida = Integer.parseInt(osad[0].split(" ")[1]) - 1;
                String broneering;

                // Üksiku koha broneerimisel
                if (arv == 1) {
                    int koht = Integer.parseInt(osad[1].split(" ")[1]) - 1;
                    teater.getIste(rida, koht).paneKinni();
                    broneering = "Teater: " + teater.getNimi() + ", Rida: " + (rida + 1) + ", Koht: " + (koht + 1);
                } else { // Mitme koha broneerimisel
                    String[] istmed = osad[1].split(" ")[1].split("-");
                    int alg = Integer.parseInt(istmed[0]) - 1;
                    int lopp = Integer.parseInt(istmed[1]) - 1;

                    for (int i = alg; i <= lopp; i++) {
                        teater.getIste(rida, i).paneKinni();
                    }

                    broneering = "Teater: " + teater.getNimi() + ", Rida: " + (rida + 1) + ", Kohad: " + (alg + 1) + "-" + (lopp + 1);
                }
                // Broneeringu salvestamine faili
                salvestaBroneering(broneering);
                return "Broneeritud: " + broneering;

            } else if (kasTahab.equals("ei")) { //Mittesobivuse korral
                sobivad.remove(valik);
                System.out.println("Järgmine valik: ");
            }
        }

        return "Kahjuks pole sobivaid kohti.";
    }


    // Kohtade broneerimine
    private String broneeriKoht(Scanner scanner) {
        while (true) {
            System.out.println("Sisesta rida (1-" + teater.getRead() + "): ");
            int rida = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.println("Sisesta koht või vahemik (1-" + teater.getIstmed() + "): ");
            String kohtInput = scanner.nextLine().trim();

            int alg, lopp;

            // Kontrollime, kas sisend sisaldab vahemikku
            if (kohtInput.contains("-")) {
                String[] vahemikOsad = kohtInput.split("-");

                // Kontrollime, kas vahemikus on ainult kaks numbrit
                if (vahemikOsad.length == 2) {
                    try {
                        alg = Integer.parseInt(vahemikOsad[0].trim()) - 1;
                        lopp = Integer.parseInt(vahemikOsad[1].trim()) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Vigane sisend. Kasuta ainult numbreid ja vahemiku korral '-' sümbolit.");
                        continue;
                    }
                } else {
                    System.out.println("Vigane sisend. Palun sisesta kas üks koht või vahemik.");
                    continue;
                }
            } else {
                // Üks koht
                try {
                    alg = Integer.parseInt(kohtInput) - 1;
                    lopp = alg;
                } catch (NumberFormatException e) {
                    System.out.println("Vigane sisend. Kasuta ainult numbreid.");
                    continue;
                }
            }

            // Kontrollime, kas sisestatud rida ja kohad on kehtivad
            if (rida < 0 || rida >= teater.getRead() || alg < 0 || lopp >= teater.getIstmed() || alg > lopp) {
                System.out.println("Vigane sisend, proovi uuesti!");
                continue;
            }

            // Kontrollime, kas kõik valitud kohad on vabad
            boolean vaba = true;
            for (int i = alg; i <= lopp; i++) {
                if (teater.getIste(rida, i).isKinni()) {
                    vaba = false;
                    break;
                }
            }

            if (vaba) {
                // Broneerime kõik kohad
                for (int i = alg; i <= lopp; i++) {
                    teater.getIste(rida, i).paneKinni();
                }

                // Salvestame broneeringu info
                String broneering = "Teater: " + teater.getNimi() + ", Rida: " + (rida + 1) + ", Kohad: " + (alg + 1) + (alg == lopp ? "" : "-" + (lopp + 1));
                salvestaBroneering(broneering);

                return "Broneeritud: " + broneering;
            } else {
                System.out.println("Mõni valitud koht on juba kinni. Proovi uuesti!");
            }
        }
    }

    //Broneeringu salvestamine faili broneeringud.txt
    private void salvestaBroneering(String broneering) {
        try (FileWriter writer = new FileWriter("broneeringud.txt", true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(broneering);
            out.flush();
        } catch (IOException e) {
            System.out.println("Viga faili kirjutamisel: " + e.getMessage());
        }
    }
}


