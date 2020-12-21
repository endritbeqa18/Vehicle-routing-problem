//import org.apache.commons.math3.distribution.BinomialDistribution;
//import org.apache.commons.math3.distribution.CauchyDistribution;
//import org.apache.commons.math3.distribution.PoissonDistribution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


//crossoveri i merr selecton kromosomet(pikat qe ka mi kalu ni kamion) ne menyre te rastesishme nga dy individe edhe mane e bon normalizimin
//dmth e kqyr mes e viziton naj pik 2 her edhe mes osht naj pik mongi
//mutacioni i ndrron dy pika mes kromosomeve dmth dy kamiona e ndrrojin ka ni pike tyne

public class AlgoritmiGjenetik extends JPanel {



    private int numriVeturave;
    private Point[] pikat;
    private int gjenerataTanishme=0;
    private ArrayList<Individe> popullata = new ArrayList<Individe>();
    private double probabilitetiPerMutacion = 0.03;
    private int madhesiaPopullates =300;
    private int numriMaksimalIterimeve=1000;
    private int numriFemijevePerGjenerate=300;
    private TravelingSalesman TravelingSalesman = new TravelingSalesman();
    private Dimension screenDimensions=Toolkit.getDefaultToolkit().getScreenSize();
    private Point depoja ;//= new Point(screenDimensions.width/2-50, screenDimensions.height/2-50);
    private Font font= new Font("Times New Roman",Font.BOLD,20);
    private HashMap<ArrayList<Point>, Double> listeGjeneve = new HashMap<>();//i rune gjenet(grupet e pikave) me rradhitje me te mire per rruge me te shkurte dhe distancen e rruges



    public AlgoritmiGjenetik(int numriVeturave, Point[] pikat,Point depoja,int madhesiaPopullates,int numriFemijeve) {
        this.depoja=depoja;
        this.madhesiaPopullates=madhesiaPopullates;
        this.numriFemijevePerGjenerate=numriFemijeve;


        this.pikat = pikat;
        this.numriVeturave = numriVeturave;
        for (int i = 0; i < madhesiaPopullates; i++) {//krijon popullaten fillestare
            Individe individeFillestar = krijoIndivideFillestare();
            popullata.add(individeFillestar);
        }
    }



    public void sortPopullaten() {//e rradhite popullaten sipas gjatsise se rruges qe e bojne veturat e atij individi
        popullata.sort(new Comparator<Individe>() {
            @Override
            public int compare(Individe individe, Individe t1) {
                if (t1.score == individe.score) {
                    return 0;
                }
                if (t1.score > individe.score) {
                    return -1;
                }
                return 1;
            }
        });
    }


    public void paintComponent(Graphics g2) {//e vizaton gjendjen ma tmire per momentin
        Graphics2D g = (Graphics2D) g2;
        g.setStroke(new BasicStroke(10));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenDimensions.width, screenDimensions.height);
        g.setColor(Color.GREEN);
        g.fillOval(depoja.x - 10, depoja.y - 10, 20, 20);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Algoritmi gjenetik",10,20);
        g.drawString("Gjenerata :"+gjenerataTanishme,10,50);
        g.drawString("Rruga me e shkurte :"+(int)(popullata.get(0).score),10,80);
        for (int i = 0; i < popullata.get(0).pikatEVeturave.length; i++) {
            switch (i) {
                case (0): {
                    g.setColor(Color.BLACK);
                    break;
                }
                case (1): {
                    g.setColor(Color.BLUE);
                    break;
                }
                case (2): {
                    g.setColor(Color.GREEN);
                    break;
                }
                case (3): {
                    g.setColor(Color.RED);
                    break;
                }
                case (4): {
                    g.setColor(Color.PINK);
                    break;
                }
                case (5): {
                    g.setColor(Color.MAGENTA);
                    break;
                }
                case (6): {
                    g.setColor(Color.CYAN);
                    break;
                }
                case (7): {
                    g.setColor(Color.LIGHT_GRAY);
                    break;
                }

            }


            try {// ni veture mi pas 0 qytete ma bojke out of bounds
                g.drawLine(depoja.x, depoja.y
                        , popullata.get(0).pikatEVeturave[i].get(0).x, popullata.get(0).pikatEVeturave[i].get(0).y);
            } catch (Exception e) {
            }
            for (int j = 0; j < popullata.get(0).pikatEVeturave[i].size() - 1; j++) {

                g.drawLine(popullata.get(0).pikatEVeturave[i].get(j).x, popullata.get(0).pikatEVeturave[i].get(j).y
                        , popullata.get(0).pikatEVeturave[i].get(j + 1).x, popullata.get(0).pikatEVeturave[i].get(j + 1).y);
            }
            try {
                g.drawLine(popullata.get(0).pikatEVeturave[i].get(popullata.get(0).pikatEVeturave[i].size() - 1).x, popullata.get(0).pikatEVeturave[i].get(popullata.get(0).pikatEVeturave[i].size() - 1).y
                        , depoja.x, depoja.y);
            } catch (Exception e) {
            }
        }
    }


    public Individe krijoIndivideFillestare() {//secilen pike(qytete) ia cakton nje veture ne menyre te rastesishme
        Individe rez = new Individe(numriVeturave, pikat);
        for (int i = 0; i < pikat.length; i++) {
            int nrVetures = (int) (Math.random() * numriVeturave);
            rez.pikatEVeturave[nrVetures].add(pikat[i]);
        }
        return rez;
    }



    public void llogaritKostonIndivideve()
    {
        for (int i = 0; i < popullata.size(); i++) {
            if (popullata.get(i).score > 1) {//e kqyr nese veqse ju ka numru niher poenat mes mia shtu apet edhe rune kohe
                continue;
            }//mes mi numru dy her
            for (int nrgjeneve = 0; nrgjeneve < popullata.get(i).pikatEVeturave.length; nrgjeneve++) {//i kqyr gjenet(rruget e vetures)
                if (listeGjeneve.containsKey(popullata.get(i).pikatEVeturave[nrgjeneve])) {//a osht en hashmap per mes mi llogarit edhe niher
                    popullata.get(i).score += listeGjeneve.get(popullata.get(i).pikatEVeturave[nrgjeneve]);


                } else {
                    double kostojaGjenitTeRi = TravelingSalesman.llogaritKoston(popullata.get(i).pikatEVeturave[nrgjeneve], depoja);//nuk e ka gjete en hashmap(atabaze per gjene) e gjen rrugen ma tshkurt per qato pika
                    ArrayList<Point> gjeniRregulluar = TravelingSalesman.getPikatRregulluar();//rradhitja e pikave per rruge ma tshkurte
                    popullata.get(i).score += kostojaGjenitTeRi;
                    popullata.get(i).pikatEVeturave[nrgjeneve] = gjeniRregulluar;
                    listeGjeneve.put((popullata.get(i).pikatEVeturave[nrgjeneve]), kostojaGjenitTeRi);//e shton gjenin e ri en databaze
                }
            }

        }
    }


    public void evolucioni() {
        //perdor poisson distribution per me caktu cilet individe mi zhgjedh
        int a = 0;
        llogaritKostonIndivideve();//llogarite koston e individeve
        sortPopullaten();
        while (a < numriMaksimalIterimeve && (popullata.get(popullata.size()-1).score-popullata.get(0).score>10)) {//300 gjenerata ose shkuje naj kusht tjeter per mbarim psh ndryshimi i poenave mes individid tpar edhe tfundit mu kon 50 a qashtu dishka
            //ose jone shum afer vlerat mrena 10
            System.out.println("gjenerata" + a);
            int count = 0;
            while (count < numriFemijevePerGjenerate ){//500 femije kane mu bo per gjenerate

                int index1 = (int) (Math.random() * popullata.size() );//poisson.sample());
                int index2 = (int) (Math.random() * popullata.size() );//poisson.sample());//edhe me poisson nese munesh mi bo me ti zgjedh
                Individe i1 = popullata.get(index1);
                Individe i2 = popullata.get(index2);
                Individe femija = krijoIndivide(i1, i2);
                popullata.add(femija);//e shton femine
                count++;
            }

            llogaritKostonIndivideve();//llogarite kostot e secilit individe dmth gjatesine e rruges

            sortPopullaten();
            while (popullata.size() > madhesiaPopullates) {
                popullata.remove(popullata.size() - 1);//i heke ata me poena tdobeta deri sa tmesin 300
            }
            //mi hek kush osht nen average ose deri sa tmberrin ni numer tcktun popullata
            for (int i = 0; i < popullata.size(); i++) {
                System.out.println(popullata.get(i).score);//printon poenat secilit individ
            }
            a++;
            gjenerataTanishme++;
            repaint();
        }
        sortPopullaten();
        repaint();
        System.out.println("Algortmi konvergjoi pas "+a+" iteracioneve me kosto te rruges prej "+popullata.get(0).score);
    }


    public Individe krijoIndivide(Individe individi1, Individe individi2) {
        Individe femije = new Individe(numriVeturave, pikat);

        femije = individi1.crossoveri(individi1, individi2);//ben krossoveer
        femije.mutacioni(femije, probabilitetiPerMutacion);//ben mutacion

        return femije;
    }



    public static void main(String[] args) {
        int numriPikave=35;

        Point[] p = new Point[numriPikave];//krijon pikat
        Dimension screenDimensions=Toolkit.getDefaultToolkit().getScreenSize();
        for (int i = 0; i < numriPikave; i++) {
            p[i] = new Point((int) (Math.random() *(screenDimensions.width-100)), (int) (Math.random() * (screenDimensions.height-100)));
        }


        Point[] Points=new Point[35];
        Points[0]=new Point(1125,542);
        Points[1]=new Point(1804,657);
        Points[2]=new Point(764,120);
        Points[3]=new Point(823,1030);
        Points[4]=new Point(39,57);
        Points[5]=new Point(1116,103);
        Points[6]=new Point(2165,488);
        Points[7]=new Point(1032,48);
        Points[8]=new Point(22,608);
        Points[9]=new Point(904,577);
        Points[10]=new Point(1308,177);
        Points[11]=new Point(54,251);
        Points[12]=new Point(1823,208);
        Points[13]=new Point(2360,756);
        Points[14]=new Point(1447,827);
        Points[15]=new Point(1066,263);
        Points[16]=new Point(827,598);
        Points[17]=new Point(1138,338);
        Points[18]=new Point(708,1231);
        Points[19]=new Point(1898,97);
        Points[20]=new Point(1036,1170);
        Points[21]=new Point(846,378);
        Points[22]=new Point(1094,140);
        Points[23]=new Point(1988,799);
        Points[24]=new Point(641,304);
        Points[25]=new Point(1491,280);
        Points[26]=new Point(190,224);
        Points[27]=new Point(2247,1234);
        Points[28]=new Point(1065,411);
        Points[29]=new Point(1418,1244);
        Points[30]=new Point(545,309);
        Points[31]=new Point(2245,675);
        Points[32]=new Point(975,1304);
        Points[33]=new Point(205,846);
        Points[34]=new Point(988,847);


        AlgoritmiGjenetik obj = new AlgoritmiGjenetik(8,Points,new Point(screenDimensions.width/2,screenDimensions.height/2),300,300);//numri i veturave pe rregullohet prej sveti se pe mujin mu kone gjenet edhe me ka 0 qytete po qikjo osht vetem limiti i siperm


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenDimensions);
        frame.setTitle("Vehicle Routing Problem Genetic Algorithm");
        frame.setVisible(true);
        frame.getContentPane().add(obj);

        obj.evolucioni();
        // System.out.print("maroi");
    }
}