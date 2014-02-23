package geoassist.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class JanelaPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = -7821914581385199443L;

    private static final String TITULO_PROJETO = "GeoAssist";

    private static final String MENU_ARQUIVO = "Arquivo";
    private static final String MENU_NOVO = "Novo...";
    private static final String MENU_SAIR = "Sair";
    private static final String MENU_AJUDA = "Ajuda";
    private static final String MENU_SOBRE = "Sobre...";

    private JDesktopPane desktop;

    public JanelaPrincipal() {
        super(TITULO_PROJETO);

        // setExtendedState(MAXIMIZED_BOTH);

        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(320, 240, 800, 600);

        desktop = new JDesktopPane(); // a specialized layered pane

        criarFormView();

        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // menu arquivo
        menuBar.add(criaMenuArquivo());

        // menu ajuda
        menuBar.add(criaMenuAjuda());

        return menuBar;
    }

    private JMenu criaMenuArquivo() {

        JMenu menu = new JMenu(MENU_ARQUIVO);
        menu.setMnemonic(KeyEvent.VK_D);

        // novo
        JMenuItem menuItem = new JMenuItem(MENU_NOVO);
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand(MENU_NOVO);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        // sair
        menuItem = new JMenuItem(MENU_SAIR);
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand(MENU_SAIR);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menu;
    }

    private JMenu criaMenuAjuda() {

        JMenu menuHelp = new JMenu(MENU_AJUDA);
        menuHelp.setMnemonic(KeyEvent.VK_J);

        // sobre
        JMenuItem menuItem = new JMenuItem(MENU_SOBRE);
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.setActionCommand(MENU_SOBRE);
        menuItem.addActionListener(this);
        menuHelp.add(menuItem);

        return menuHelp;
    }

    // acoes do menu
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (MENU_NOVO.equals(command)) {
            criarSubJanela();
        }

        if (MENU_SAIR.equals(command)) {
            quit();
        }

    }

    // Create a new internal frame.
    protected void criarSubJanela() {
        MyInternalFrame frame = new MyInternalFrame();
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    protected void criarFormView() {
        JanelaPlanta frame = new JanelaPlanta();
        frame.setVisible(true);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    // sair
    protected void quit() {
        System.exit(0);
    }

}
