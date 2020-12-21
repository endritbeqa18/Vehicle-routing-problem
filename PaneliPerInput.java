import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PaneliPerInput extends JPanel  {

    private ArrayList<Point> pikat=new ArrayList<>();
    private Dimension screenDimensions=Toolkit.getDefaultToolkit().getScreenSize();
    private Point depo=new Point((int)(0.541*screenDimensions.width),(int)(0.448*screenDimensions.height));
    private JFrame frame;
    private Font font= new Font("Times New Roman",Font.BOLD,20);
    private AlgoritmiGjenetik obj;
    private Image harta;
    { try {
        harta = ImageIO.read(getClass().getResource("images/HartaKosoves.png"));
    } catch (Exception e){}}

    private MouseAdapter mouseAdapter= new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            pikat.add(e.getPoint());
            repaint();
        }
    };



    public class runAlgorithm implements Runnable{
        @Override
        public void run() {
            Point[] p = arrayListToArray(pikat);
            obj = new AlgoritmiGjenetik(9, p,depo,300,300);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(screenDimensions);
            frame.setTitle("Vehicle Routing Problem Genetic Algorithm");
            frame.setVisible(true);
            frame.getContentPane().add(obj);
            obj.evolucioni();
        }
    }


    private KeyAdapter keyAdapter=new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_ENTER)
            {

                frame.dispose();
                Point[] p=arrayListToArray(pikat);
                Thread algoritmi=new Thread(new runAlgorithm());
                algoritmi.start();

            }
        }
    };



    public Point[] arrayListToArray(ArrayList<Point> arrayList)
    {Point[] rez= new Point[arrayList.size()];

        for(int i=0; i<arrayList.size(); i++)
        {
            rez[i]=arrayList.get(i);
        }
        return rez;
    }



    public PaneliPerInput()
    {

        this.addMouseListener(this.mouseAdapter);

        frame=new JFrame();
        frame.addKeyListener(this.keyAdapter);
        frame.setSize(this.screenDimensions);
        frame.setTitle("Input");
        frame.setVisible(true);
        frame.getContentPane().add(this);

    }



    public void paintComponent(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,screenDimensions.width,screenDimensions.height);
        g.setColor(Color.RED);
        g.fillOval((int)depo.getX(),(int)depo.getY(),20,20);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Kur te vendosen pikat shtyp Enter",20,20);
        for(int i=0; i<pikat.size(); i++)
        {
            g.fillOval((int)pikat.get(i).getX()-10,(int)pikat.get(i).getY()-1,20,20);
        }
    }






    public static void main(String[] args) {
        PaneliPerInput paneliPerInput= new PaneliPerInput();

    }


}
