
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.DefaultListModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
//import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

//todo: crear archivo configuacion /etc/runner.lalo  
public class AppRunner extends JFrame {
    public AppRunner() {
        init();
        leerArchivo();
    }

    private void init() {

        // ventana
        Dimension deskSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(200, 300);
        this.setLayout(null);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(0, (int) deskSize.getHeight() - 300);

        // lblStatus = new JLabel();
        // lista de aplicaciones
        lstApp = new JList<String>();
        lstApp.setBounds(0, 0, 200, 300);
        lstApp.setFont(new Font("Hack Nerd Font Mono", Font.PLAIN, 12));
        lstApp.setForeground(Color.ORANGE);
        lstApp.setBackground(new Color(ERROR, HEIGHT, ABORT));

        lstApp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirApp(e);
            }

        });
        lstApp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    Thread.sleep(2000);
                } catch (Exception ex) {
                    // TODO: handle exception
                }
                System.exit(0);
            }
        });
        scrollPane = new JScrollPane(lstApp);
        scrollPane.setBounds(0, 0, 200, 300);
        scrollPane.setBackground(Color.CYAN);
        scrollPane.setVerticalScrollBar(new JScrollBar());

        // scrollPane.add(lstApp);

        this.add(scrollPane);
        scrollPane.updateUI();

    }

    private void abrirApp(MouseEvent e) {
        try {
            if (e.getClickCount() == 2) {

                String app = lstApp.getSelectedValue();
                int indiceCmd = lstApp.getSelectedIndex();
                String cmd[] = cmds.get(indiceCmd).split("%");

                JOptionPane.showMessageDialog(this, "Ejecutando..." + " " + app + "" + indiceCmd + cmd[0] + cmd[1]);
                Runtime.getRuntime().exec(cmd);
                System.exit(0);
            }
        } catch (Exception ex) {
            System.out.println(ex);

        }
    }

    private void leerArchivo() {
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        try {
            cmds = new Vector<String>();
            File archivo = new File("./apprunner.lalo");
            if (archivo.exists()) {
                FileReader stream = new FileReader(archivo);
                BufferedReader reader = new BufferedReader(stream);
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datoLinea = linea.split(",");
                    // System.out.println(datoLinea[1]);
                    dlm.addElement(datoLinea[0] + " " + datoLinea[1]);
                    cmds.addElement(datoLinea[2] + "%" + datoLinea[3]);
                }

                lstApp.setModel(dlm);
                reader.close();

            } else {
                System.out.println("no existe");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        AppRunner foo = new AppRunner();
        foo.setVisible(true);
    }

    private Vector<String> cmds;
    private JList<String> lstApp;
    private JScrollPane scrollPane;
}
