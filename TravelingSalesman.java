import java.awt.*;
import java.util.ArrayList;

public class TravelingSalesman {



    private Point[] pikat;//pikat qe ka mi vizitu qikjo veture
    private Point Depoja;//pozita e depos
    private ArrayList<Point> pikatRregulluar;//kur ta gjen rrugen ma tshkurt e rune rradhitjen e pikave(qyteteve) qe duhet me shku
    private int numriRestarteve=10;
    private int numriHapave=100;


    public double llogaritKoston(ArrayList<Point> gjeni,Point depoja)
    {
        pikat =new Point[gjeni.size()];
        this.Depoja=depoja;
        for(int i=0; i<gjeni.size(); i++)
        {
            pikat[i]=gjeni.get(i);
        }

        double distancaTotal=0;
        HillClimbing hillClimbing=new HillClimbing();
        distancaTotal=hillClimbing.RouteCalculator(pikat,Depoja,numriRestarteve,numriHapave);//distance e rruges me te shkurte
        pikatRregulluar=hillClimbing.ktheRradhitjenMeTeMire();//radhitja e pikave per distance me te shkurte
        return distancaTotal;
    }

    public ArrayList<Point> getPikatRregulluar() {
        return pikatRregulluar;

    }
}
