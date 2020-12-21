import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class HillClimbing extends JPanel {

    private Qyteti[] qytetet;
    private Qyteti depoja;
    private int[] rradhitjaTanishme;//jon trujne me int se hashmapit per distanca tqytetit pe duhet mi jap saktesishte qate reference qe e ki shti en hashmap e kshtuqe vetem pi ja japi indeksin e qytetit en qytetet[] se prej qatij jon ndertu hashmapat
    private int[] rradhitjaMeMire;
    private int numriRestartave;
    private int numriHapave;
    private double distancaMeMire = 1000000000;
    private double distancaTanishme = 0;
    private int numriQytetveQeDuhetVizituar;
    private Dimension screenDimensions=Toolkit.getDefaultToolkit().getScreenSize();
    private boolean ukry = false;

    public double RouteCalculator(Point[] lokacionet, Point lokacioniDepoja,int numriRestartave,int numriHapave) {//numriRestarteve eshte sa here ka mi bo shufle array trradhitjes edhe mia nis apet
        //numriHapave eshte sa hapa i ben en hillClimbing
        this.numriHapave=numriHapave;
        this.numriRestartave=numriRestartave;

        numriQytetveQeDuhetVizituar = lokacionet.length;

        rradhitjaTanishme = new int[lokacionet.length + 2];
        rradhitjaMeMire = new int[lokacionet.length + 2];

        qytetet = new Qyteti[lokacionet.length + 1];
        this.depoja = new Qyteti(lokacioniDepoja, "Depoja");

        qytetet[0] = depoja;

        for (int i = 0; i < lokacionet.length; i++) {
            qytetet[i + 1] = new Qyteti(lokacionet[i], "Qyteti " + (i + 1));
        }

        for (int i = 0; i < qytetet.length; i++) {
            qytetet[i].mbushiDistancatQyteteve(qytetet);
        }

        rradhitjaTanishme[0] = 0;
        rradhitjaTanishme[rradhitjaTanishme.length - 1] = 0;

        for (int i = 1; i < rradhitjaTanishme.length - 1; i++) {
            rradhitjaTanishme[i] = i;
        }

        hillClimbing();
        return distancaMeMire;
    }

    static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }


    public void hillClimbing() {
        int count = 0;// sa hapa i kemi bo
        int randomCount = 0;//sa here ja kemi bo reset
        int pozitaEQytetitQeLeviz = -1;//ruhet indeksi i qytetit qe po e levizim nalt apo posht ne rradhitje
        int drejtimi = -1;//0 hec mrapa 1 hec perpara
        while (randomCount < numriRestartave) {//reseti e qet me ni rrahitje trastesishme edhe e len me hype maleve (hill climbing)
            int[] temp = new int[rradhitjaTanishme.length - 2];//per mia bo reset
            for (int i = 0; i < rradhitjaTanishme.length - 2; i++) {
                temp[i] = rradhitjaTanishme[i + 1];
            }
            shuffleArray(temp);
            for (int i = 1; i < rradhitjaTanishme.length - 1; i++) {
                rradhitjaTanishme[i] = temp[i - 1];
            }

            count = 0;
            while (count < numriHapave) {//sa hapa munet mi bo
                double distancaRe = 0;//distanca per kete raund
                if (pozitaEQytetitQeLeviz == -1) {
                    pozitaEQytetitQeLeviz = 1 + (int) (Math.random() * (numriQytetveQeDuhetVizituar));//indeksi i qytetit en rradhitje qe ka me levize tash
                    drejtimi = (int) (Math.random() * 2);//a he hec nalt a posht en rradhitje
                }
                int gatsiaLevizjes = (int) (Math.random() * numriQytetveQeDuhetVizituar);//kom mujt edhe numer fiks po nuk punojke mir
                // mi bo qe mi dit en cfar drejtimi po shkon edhe mi bo kercimet math.random*distanca prej fundit
                if ((pozitaEQytetitQeLeviz + gatsiaLevizjes >= rradhitjaTanishme.length - 1 && drejtimi == 1) || (pozitaEQytetitQeLeviz - gatsiaLevizjes <= 0 && drejtimi == 0)) {//e kqyr mes me kcy out of bounds ose me e ndrru poziten me depon
                    pozitaEQytetitQeLeviz = -1;
                    drejtimi = -1;
                    continue;
                }

                if (drejtimi == 1) {
                    int qyteti1 = rradhitjaTanishme[pozitaEQytetitQeLeviz];//qyteti qe po leviz
                    int qyteti2 = rradhitjaTanishme[pozitaEQytetitQeLeviz + gatsiaLevizjes];//qyteti i cili do ta nderroj poziten me qytetin qe po leviz
                    rradhitjaTanishme[pozitaEQytetitQeLeviz] = qyteti2;
                    rradhitjaTanishme[pozitaEQytetitQeLeviz + gatsiaLevizjes] = qyteti1;
                    distancaRe = llogaritDistancen();//e llogarit distance mbas ndryshimit
                }

                if (drejtimi == 0) {
                    int qyteti1 = rradhitjaTanishme[pozitaEQytetitQeLeviz];
                    int qyteti2 = rradhitjaTanishme[pozitaEQytetitQeLeviz - gatsiaLevizjes];
                    rradhitjaTanishme[pozitaEQytetitQeLeviz] = qyteti2;
                    rradhitjaTanishme[pozitaEQytetitQeLeviz - gatsiaLevizjes] = qyteti1;
                    distancaRe = llogaritDistancen();
                }

                if (distancaRe < distancaMeMire) {//e gjeti ni rruge ma tmire
                    distancaMeMire = distancaRe;
                    for (int a = 0; a < rradhitjaTanishme.length; a++) {
                        rradhitjaMeMire[a] = rradhitjaTanishme[a];//e rune edhe qite rrahitje
                    }
                    if (drejtimi == 1) {
                        pozitaEQytetitQeLeviz += gatsiaLevizjes;//e qete indeksin e qytetit qe po leviz ne rradhitje aty ku dehet me qene
                    }

                    if (drejtimi == 0) {
                        pozitaEQytetitQeLeviz -= gatsiaLevizjes;
                    }
                    count = 0;
                    continue;
                } else {//nese nuk u kon suksesshme atehere mi kthy qysh u kone (hape mrapa)
                    if (drejtimi == 1) {
                        int qyteti1 = rradhitjaTanishme[pozitaEQytetitQeLeviz];
                        int qyteti2 = rradhitjaTanishme[pozitaEQytetitQeLeviz + gatsiaLevizjes];
                        rradhitjaTanishme[pozitaEQytetitQeLeviz] = qyteti2;
                        rradhitjaTanishme[pozitaEQytetitQeLeviz + gatsiaLevizjes] = qyteti1;
                    }

                    if (drejtimi == 0) {
                        int qyteti1 = rradhitjaTanishme[pozitaEQytetitQeLeviz];
                        int qyteti2 = rradhitjaTanishme[pozitaEQytetitQeLeviz - gatsiaLevizjes];
                        rradhitjaTanishme[pozitaEQytetitQeLeviz] = qyteti2;
                        rradhitjaTanishme[pozitaEQytetitQeLeviz - gatsiaLevizjes] = qyteti1;
                    }
                    drejtimi = -1;
                    pozitaEQytetitQeLeviz = -1;
                    count++;
                }

                 //try{Thread.sleep(1);System.out.println("thread.sleep is on");}catch (Exception e){}//pe shkon shum shpejt ka niher e spe dallohet shka o ka bohet e mi ngadalsu
            }
            repaint();
            randomCount++;
        }
        ukry = true;
    }


    public void paint(Graphics g2) {//kjo osht painti vetem per traveling salesman problem i vizaton gjate krejt kohes rrugen qe po e merr
        Graphics2D g = (Graphics2D) g2;
        g.setStroke(new BasicStroke(10));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenDimensions.width, screenDimensions.height);
        g.setColor(Color.GREEN);
        g.fillOval(depoja.lokacioniQytetit.x - 15, depoja.lokacioniQytetit.y - 15, 30, 30);
        for (int i = 0; i < rradhitjaTanishme.length - 1; i++) {
            g.setColor(Color.BLACK);
            g.drawLine(qytetet[rradhitjaTanishme[i]].lokacioniQytetit.x, qytetet[rradhitjaTanishme[i]].lokacioniQytetit.y
                    , qytetet[rradhitjaTanishme[i + 1]].lokacioniQytetit.x, qytetet[rradhitjaTanishme[i + 1]].lokacioniQytetit.y);

        }
        if (ukry) {//kur tkryhet e vizaton rrugen ma tmire
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, screenDimensions.width, screenDimensions.height);
            g.setColor(Color.GREEN);
            g.fillOval(depoja.lokacioniQytetit.x - 15, depoja.lokacioniQytetit.y - 15, 30, 30);
            for (int i = 0; i < rradhitjaMeMire.length - 1; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(qytetet[rradhitjaMeMire[i]].lokacioniQytetit.x, qytetet[rradhitjaMeMire[i]].lokacioniQytetit.y
                        , qytetet[rradhitjaMeMire[i + 1]].lokacioniQytetit.x, qytetet[rradhitjaMeMire[i + 1]].lokacioniQytetit.y);

            }
        }

    }


    public ArrayList<Point> ktheRradhitjenMeTeMire()// mi kthy si ArrayList<Point> sepse individed i rujne qytetet si points
    {
        ArrayList<Point> rez=new ArrayList<>();//nuk i kthen depot masi qe e din ku gjindet
        for(int i=1; i<rradhitjaMeMire.length-1; i++)//te rradhitjameEMire i pari edhe i fundit jonne depoja
        {
            rez.add(qytetet[rradhitjaMeMire[i]].lokacioniQytetit);
        }

        return rez;
    }



    public double llogaritDistancen() {//e llogarit distancen e rruges
        double distanca = 0;
        for (int i = 0; i < rradhitjaTanishme.length - 1; i++) {
            Double delta = qytetet[rradhitjaTanishme[i]].qytetiDistanca.get(qytetet[rradhitjaTanishme[i + 1]]);// hin en hashMapin e qytetin aktual ku ruhen distancat mes qyteteve edhe e merr distancen qe e ka me qytetin e ardhshem en rradhitje(rrugen qe [po e kalon)
            distanca += delta.doubleValue();
        }
        distancaTanishme = distanca;
        return distanca;
    }


    public static void main(String[] args) {//ktu maini osht per me tregu traveling salesman problem me hill climbing
        int numriPikave=15;

        Point[] p = new Point[numriPikave];
        Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
        for (int i = 0; i < numriPikave; i++)
        { p[i] = new Point((int) (Math.random() * (screen.width-100)), (int) (Math.random() * (screen.height-100))); }

        HillClimbing obj = new HillClimbing();

        JFrame frame = new JFrame();
        frame.setSize(obj.screenDimensions.width, obj.screenDimensions.height);
        frame.setTitle("Traveling Salesman Hill Climbing");
        frame.setVisible(true);
        frame.getContentPane().add(obj);
        obj.RouteCalculator(p, new Point(obj.screenDimensions.width/2-50, obj.screenDimensions.height/2-50),1000,10000);


    }


}

