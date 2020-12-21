import java.awt.*;
import java.util.ArrayList;

public class Individe {


    public ArrayList<Point>[] pikatEVeturave;//numri i veturave edhe pikat e tyre
    //aaraji paraqet veturat kurse ArrayLista paraqet pikat(qytetet) qe ka mi vizitu
    public Point[] pikat;//pikat en ekran qe jone nevojitet per normalizim
    public double score;//shuma e rruges krejt veturave (sa ma e vogel ma e mire)


    public Individe(int nrAutomjeteve, Point[] pikat) {
        pikatEVeturave = new ArrayList[nrAutomjeteve];//cakton sa automjete jane
        for (int i = 0; i < nrAutomjeteve; i++) {
            pikatEVeturave[i] = new ArrayList<Point>();//i mbush me aaray lista per mes me pas null pointer exeption
        }
        this.pikat = pikat;
    }


    public Individe normalizimi(Individe individi)//ka raste kur ni vetures i jepen pika njejta me ni veture tjeter ose kur nuk vizitohet hiq nje pike metoda e rregullon,
    //per secilen pike e krijon ni ArrayList edhe e kqyr te cilat vetura gjendet pastaj me math.random e cakton en cilen veture ka me mbete dhe i heq nga veturat tjera
    {
        pikatQeDuhetVizitohen:
        for (int i = 0; i < pikat.length; i++)//qiky loop i heq pikat e dyfishta
        {
            ArrayList<PozitaPikesNeIndivide> indeksatKuGjindetQikjoPike = new ArrayList<>();
            kamionat:
            for (int j = 0; j < individi.pikatEVeturave.length; j++) {
                pikatQeIVizitonKamioni:
                for (int k = 0; k < individi.pikatEVeturave[j].size(); k++) {
                    if (individi.pikatEVeturave[j].get(k).equals(pikat[i])) {
                        indeksatKuGjindetQikjoPike.add(new PozitaPikesNeIndivide(j, k));//o ka e shtin en arrayList indeksatKuGjindetQikjoPike
                    }
                }
            }
            int selectCilaMete = (int) (Math.random() * indeksatKuGjindetQikjoPike.size());// e selekton numrin e vetures en cilen do te mbetet;

            for (int a = 0; a < indeksatKuGjindetQikjoPike.size(); a++) {

                if (selectCilaMete != a) {//nese nuk osht numri i vetures qe ka me mbet ka me heq
                    int nrKamionit = indeksatKuGjindetQikjoPike.get(a).nrVetures;//numri i kamionit
                    int indeksiPikesqeVizitonKamioni = indeksatKuGjindetQikjoPike.get(a).indeksiIPikes;//e sata me rradhe osht
                    individi.pikatEVeturave[nrKamionit].remove(indeksiPikesqeVizitonKamioni);//e hek//pe ndodh me qene dy copa en qit veture e pia bon array out of bounds
                    for(int j=0; j<indeksatKuGjindetQikjoPike.size(); j++)
                    {
                        if(indeksatKuGjindetQikjoPike.get(j).nrVetures==nrKamionit){indeksatKuGjindetQikjoPike.get(j).indeksiIPikes-=1;}//nese perseriten dy pika ne nje veture atehere kur provon mi heke tdyten mundet me shkatu index out of bounds
                    }
                }
            }

        }


        pikatQeDuhetVizitohen:
        for (int i = 0; i < pikat.length; i++)//shikon mes duhet mu shtu naj pika qe osht mongi
        {
            boolean aMungonQikjoPike = true;
            veturat:
            for (int j = 0; j < individi.pikatEVeturave.length; j++) {
                pikatQeIVizitonVetura:
                for (int k = 0; k < individi.pikatEVeturave[j].size(); k++) {
                    if (individi.pikatEVeturave[j].get(k).equals(pikat[i])) {
                        aMungonQikjoPike = false;
                        continue pikatQeDuhetVizitohen;
                    }
                }
            }


            if (aMungonQikjoPike) {//qikjo pike koka mongi e vendos ne menyre te rastesishme ne nje veture
                int selectKuVendoset = (int) (Math.random() * pikatEVeturave.length);
                individi.pikatEVeturave[selectKuVendoset].add(pikat[i]);
                // System.out.println("u vendos");
            }

        }

        return individi;
    }

    public class PozitaPikesNeIndivide {//klas qe e mban nmend poziten e nje pikes en nje individ pra numrin e vetures edhe e sata eshte ne rradhe ajo pike
        public int nrVetures;
        public int indeksiIPikes;

        public PozitaPikesNeIndivide(int nrkamionit, int indeksiIPikes) {
            this.nrVetures = nrkamionit;
            this.indeksiIPikes = indeksiIPikes;
        }

    }


    public Individe crossoveri(Individe individe1, Individe individe2) {//e zgjedh ne menyre te rastesishme cilat vetura(dmth rradhitjen e pikave per ate veture) me i marre nga njeri prej dy individeve
        Individe rez = new Individe(pikatEVeturave.length, pikat);

        for (int i = 0; i < pikatEVeturave.length; i++) {
            int select = (int) (Math.random() * 2);//zgjedh prej cilit individe mi marr qit veture
            if (select == 0) {
                rez.pikatEVeturave[i] = (ArrayList<Point>) individe1.pikatEVeturave[i].clone();//clone se dilke ni problem en lidhje me referencat
            } else {
                rez.pikatEVeturave[i] = (ArrayList<Point>) individe2.pikatEVeturave[i].clone();
            }
        }

        rez = normalizimi(rez);//duhet mi normalizu individin dmth mi hek pozitat(qytetet) e dyfishta edhe mi shtu pika(qytetet) qe jane mongu

        return rez;
    }


    public void mutacioni(Individe individi, double probabiliteti) {//i zgjedh dy pika(qytete) prej dy veturave edhe ju ndrron vendet
        int gjasaPerNdryshim = (int) ((1 / probabiliteti) * Math.random());
        if (gjasaPerNdryshim == 1) {
            int selectVetura1 = (int) (Math.random() * individi.pikatEVeturave.length);//selekton veturen e pare
            int selectPikenKamioni1 = (int) (Math.random() * individi.pikatEVeturave[selectVetura1].size());//selekton piken e vetures tpare
            if (pikatEVeturave[selectVetura1].size() == 0) {
                return;
            }
            Point pika1 = individi.pikatEVeturave[selectVetura1].get(selectPikenKamioni1);//pika 1

            int selectVetura2 = (int) (Math.random() * individi.pikatEVeturave.length);//selekton veturen e dyte
            int selectPikenKamioni2 = (int) (Math.random() * individi.pikatEVeturave[selectVetura2].size());//selekton piken e vetures tdyte
            if (pikatEVeturave[selectVetura2].size() == 0) {
                return;
            }
            Point pika2 = individi.pikatEVeturave[selectVetura2].get(selectPikenKamioni2);//pika 2

            individi.pikatEVeturave[selectVetura2].set(selectPikenKamioni2, pika1);//i ndrron vendet ketyre dy pikave
            individi.pikatEVeturave[selectVetura1].set(selectPikenKamioni1, pika2);
        }

    }


}
