import java.awt.*;
import java.util.HashMap;


public class Qyteti {
    public HashMap<Qyteti, Double> qytetiDistanca = new HashMap<Qyteti, Double>();//rune qytete edhe distancat mes tyre edhe qitej qyteti qe gjendet en qit klase
    public Qyteti[] qytetet;//krejt qytete(pikat) qe jane
    public Point lokacioniQytetit;//pozita e qetij qyteti qe gjindet ne klase
    public String emriQytetit;

    public Qyteti(Point lokacioni, String emri) {
        this.lokacioniQytetit = lokacioni;
        this.emriQytetit = emri;
    }


    public void mbushiDistancatQyteteve(Qyteti[] qytetet) {
        this.qytetet = qytetet;
        for (int i = 0; i < qytetet.length; i++) {
            double distanca = Math.sqrt(Math.pow(lokacioniQytetit.getX() - qytetet[i].lokacioniQytetit.getX(), 2)//e llogarit distancen me teoreme tpitagores
                    + Math.pow(lokacioniQytetit.getY() - qytetet[i].lokacioniQytetit.getY(), 2));
            qytetiDistanca.put(qytetet[i], distanca);

        }

    }


}
