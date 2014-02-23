package geoassist.views;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import geoassist.dominio.Imovel;
import geoassist.dominio.UtilEntidades;
import geoassist.geometria.Ponto3D;
import geoassist.geometria.ProjecaoUtils;
import geoassist.misc.Utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class JanelaPlanta extends JInternalFrame {

    private static final long serialVersionUID = -1501421300152452657L;

    private static final int LARGURA_FONTE_PIXELS = 8;

    private CadView view;

    private boolean cfgPrintView = false;
    private boolean cfgShowGrid = true;
    private boolean cfgMarkName = true;
    private boolean dragging;

    private float origemX;
    private float origemY;
    private float origemClickX;
    private float origemClickY;
    private float mouseX;
    private float mouseY;
    private float mouseClickX;
    private float mouseClickY;

    /**
     * Pixels por unidade (metro).
     */
    private float escala = 100;

    // private int minX;
    // private int maxX;
    // private int minY;
    // private int maxY;

    private double espacamentoDaGrade;

    private int aux;

    private Graphics graphics;

    private Imovel imovel;

    public JanelaPlanta() {

        super("Visualizacao", true, // resizable
                true, // closable
                true, // maximizable
                true);// iconifiable

        setSize(640, 480);

        // Set the window's location.
        // setLocation(xOffset * openFrameCount, yOffset * openFrameCount);

        view = new CadView();

        view.setFocusable(true);
        view.requestFocusInWindow();

        view.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (KeyEvent.VK_UP == e.getKeyCode()) {
                    moverPosicao(0, 1);
                }
                if (KeyEvent.VK_DOWN == e.getKeyCode()) {
                    moverPosicao(0, -1);
                }
                if (KeyEvent.VK_LEFT == e.getKeyCode()) {
                    moverPosicao(-1, 0);
                }
                if (KeyEvent.VK_RIGHT == e.getKeyCode()) {
                    moverPosicao(1, 0);
                }

                if (KeyEvent.VK_PAGE_UP == e.getKeyCode()) {
                    aplicarZoom(1.1f);
                }
                if (KeyEvent.VK_PAGE_DOWN == e.getKeyCode()) {
                    aplicarZoom(0.9f);
                }
                view.repaint();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickX = e.getX();
                mouseClickY = e.getY();
                origemClickX = origemX;
                origemClickY = origemY;

                if (e.getButton() == MouseEvent.BUTTON1) {
                    dragging = true;
                }
            }
        });

        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    dragging = false;
                }
            }
        });

        view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                view.repaint();
            }
        });

        getContentPane().add(view);

    }

    private class CadView extends JPanel {

        private static final long serialVersionUID = -8630160951581942156L;

        public CadView() {
            setBorder(BorderFactory.createLineBorder(Color.black));
            setBackground(Color.BLACK);
        }

        public Dimension getPreferredSize() {
            return new Dimension(10, 10);
        }

        public void paint(Graphics g) {
            super.paint(g);
            graphics = g;
            atualizaGrafico();
        }

    }

    private void atualizaGrafico() {

        if (cfgPrintView) {
            view.setBackground(Color.WHITE);
        } else {
            view.setBackground(Color.BLACK);
        }

        // pa.Cls;

        if (dragging) {
            origemX = origemClickX - ((mouseX - mouseClickX) / escala);
            origemY = origemClickY + ((mouseY - mouseClickY) / escala);
        }

        // if (zooming = True) {
        // gZoom 1 + ((mouseX - mouseClickX) / 1000000)
        // }

        if (cfgShowGrid) {
            desenharReguas();
        }

        mostraEscala();

        if (getImovel().getNumeroDePontos() < 2) {
            return;
        }

        desenhaPoligonoDoImovel();

        // if (form1.marcoSel != 0) {
        // g.drawOval ((getImovel().dPoligono(form1.marcoSel).X - origemX) *
        // escala,
        // pa.getHeight() - ((getImovel().dPoligono(form1.marcoSel).Y - origemY)
        // *
        // escala), 120, vbRed);
        // }

        // DoEvents

    }

    private void mostraEscala() {
        if (!cfgPrintView) {
            if (escala > 15) {
                imprime((escala / 15) + " px/m", view.getWidth() * 0.9, view.getHeight() * 0.9);
            } else {
                if (escala != 0) {
                    imprime((1 / (escala / 15)) + " m/px", view.getWidth() * 0.9, view.getHeight() * 0.9);
                }
            }
        } else {
            // if (Printer.Width > Printer.Height) {
            // aux = (pa.getWidth() / escala) / 0.297;
            // } else {
            // aux = (pa.getWidth() / escala) / 0.21;
            // }

            imprime("Escala = 1:" + Math.round(aux) + "", view.getWidth() * 0.85, view.getHeight() * 0.85);
            imprime("Folha A4", view.getWidth() * 0.85, (view.getHeight() * 0.85) + 300);
        }
    }

    private void desenharReguas() {
        if (escala > 0.5) {
            espacamentoDaGrade = Math.pow(10, 5 - String.valueOf(Math.round(escala)).length()) / 100;
        } else {
            espacamentoDaGrade = Math.round(1 / escala) * 1000;
        }

        if (espacamentoDaGrade < 1) {
            espacamentoDaGrade = 1;
        }

        graphics.setColor(Color.GRAY);

        double gradeSpcOffset;
        int gradeInicio, gradeFim;
        int x, y;
        int yTextoHorizontal = (int) (view.getHeight() * 0.96);

        gradeInicio = (int) (origemX / espacamentoDaGrade);
        gradeFim = (int) ((origemX / espacamentoDaGrade) + ((view.getWidth() / escala) / espacamentoDaGrade) + 1);

        for (int i = gradeInicio; i <= gradeFim; i++) {
            gradeSpcOffset = i * espacamentoDaGrade;
            x = (int) ((-origemX + gradeSpcOffset) * escala);
            graphics.drawLine(x, 0, x, view.getHeight());
            imprime(gradeSpcOffset, x, yTextoHorizontal);
        }

        gradeInicio = (int) (origemY / espacamentoDaGrade);
        gradeFim = (int) ((origemY / espacamentoDaGrade) + ((view.getHeight() / escala) / espacamentoDaGrade) + 1);

        for (int i = gradeInicio; i <= gradeFim; i++) {
            gradeSpcOffset = i * espacamentoDaGrade;
            y = (int) (view.getHeight() - (-origemY + gradeSpcOffset) * escala);
            graphics.drawLine(0, y, (int) view.getWidth(), y);
            imprime(gradeSpcOffset, 5, y);
        }
    }

    private void desenhaPoligonoDoImovel() {

        int pontoX, pontoY;

        int indiceUltimaCoordenada = getImovel().getNumeroDePontos() - 1;

        double anguloDivisao;

        Ponto3D centroDoImovel = getImovel().getCentro();

        for (int i = 0; i <= indiceUltimaCoordenada; i++) {

            graphics.setColor(Color.WHITE);

            pontoX = (int) ((getImovel().getCoordenada(i).x - origemX) * escala);
            pontoY = (int) (view.getHeight() - ((getImovel().getCoordenada(i).y - origemY) * escala));

            if (i < indiceUltimaCoordenada) {
                int proximoX = (int) ((getImovel().getCoordenada(i + 1).x - origemX) * escala);
                int proximoY = (int) (view.getHeight() - (getImovel().getCoordenada(i + 1).y - origemY) * escala);
                graphics.drawLine(pontoX, pontoY, proximoX, proximoY);

            } else {
                int ultimoX = (int) ((getImovel().getCoordenada(indiceUltimaCoordenada).x - origemX) * escala);
                int ultimoY = (int) ((view.getHeight() - ((getImovel().getCoordenada(indiceUltimaCoordenada).y - origemY) * escala)));
                int primeiroX = (int) ((getImovel().getCoordenada(0).x - origemX) * escala);
                int primeiroY = (int) (view.getHeight() - ((getImovel().getCoordenada(0).y - origemY) * escala));
                graphics.drawLine(ultimoX, ultimoY, primeiroX, primeiroY);
            }

            graphics.drawOval(pontoX - 2, pontoY - 2, 4, 4);

            if (cfgMarkName) {
                imprime(getImovel().getMarco(i).nome, pontoX, pontoY);
            }

            if (Utils.isNotEmpty(getImovel().getMarco(i).confrontante)) {

                anguloDivisao = ProjecaoUtils.AnguloCoord(getImovel().getCoordenada(i).x,
                        getImovel().getCoordenada(i).y, centroDoImovel.x, centroDoImovel.y);

                graphics.setColor(Color.YELLOW);

                double senoLinhaDivisoria = sin(anguloDivisao) * 100;
                double cossenoLinhaDivisoria = cos(anguloDivisao) * 100;

                graphics.drawLine(pontoX, pontoY, (int) (pontoX - senoLinhaDivisoria),
                        (int) (pontoY + cossenoLinhaDivisoria));

                if (anguloDivisao < 1.57 || anguloDivisao > 4.71) {
                    imprime(getImovel().getMarco(i).confrontante, //
                            (pontoX - senoLinhaDivisoria - (getImovel().getMarco(i).confrontante.length() * LARGURA_FONTE_PIXELS)), //
                            (pontoY + cossenoLinhaDivisoria));
                } else {
                    imprime(getImovel().getMarco(i).confrontante, //
                            (pontoX - senoLinhaDivisoria), //
                            (pontoY + cossenoLinhaDivisoria));
                }
            }

        }
    }

    private void aplicarZoom(float s) {
        if (escala < 0.01 && s < 1) {
            return;
        }
        if (escala > 6000 && s > 1) {
            return;
        }
        if (s == 0) {
            return;
        }

        float w = view.getWidth();
        float h = view.getHeight();

        float x = (w / (escala * s)) - (w / escala);
        float y = (h / (escala * s)) - (h / escala);

        origemX = origemX - (x / 2);
        origemY = origemY - (y / 2);
        escala = escala * s;
    }

    private void moverPosicao(int x, int y) {
        origemX = origemX + (x / escala) * 10;
        origemY = origemY + (y / escala) * 10;
    }

    private void imprime(String texto, double x, double y) {
        graphics.drawString(texto, (int) x, (int) y);
    }

    private void imprime(double texto, double x, double y) {
        graphics.drawString(String.valueOf(texto), (int) x, (int) y);
    }

    private Imovel getImovel() {
        if (imovel == null) {
            imovel = UtilEntidades.criaImovel();
        }
        return imovel;
    }

}
