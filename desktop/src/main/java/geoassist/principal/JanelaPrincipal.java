package geoassist.principal;

import geoassist.misc.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JanelaPrincipal {

    private JFrame janela;

    public JanelaPrincipal() {
        // default
    }

    public void onInitialize() {
        janela = new JFrame("GeoAssist2");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        janela.pack();
        janela.setSize(800, 600);
        janela.setVisible(true);

        JPanel painelPrincipal = new JPanel();
        janela.add(painelPrincipal);

        painelPrincipal.add(criaBotaoGenerico());
        painelPrincipal.add(criaBotaoSair());
        
        janela.doLayout();
    }

    private JButton criaBotaoGenerico() {
        JButton botao = new JButton(Utils.getNome());
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        });
        return botao;
    }

    private JButton criaBotaoSair() {
        JButton botao = new JButton("Sair");
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return botao;
    }

}
