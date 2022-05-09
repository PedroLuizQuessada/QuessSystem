package main;

import controle.DaoUtil;
import exception.DaoException;
import view.Login;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final DaoUtil daoUtil = new DaoUtil();

    private static ImageIcon imageIcon = null;
    private static UsuarioLogado usuarioLogado;
    private static final List<JFrame> janelas = new ArrayList<>();

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        try {
            daoUtil.conectarBd();

            imageIcon = new ImageIcon("imagens/icon.png");
            new Login();
        }
        catch (ClassNotFoundException exception){
            JOptionPane.showMessageDialog(null, "O driver do banco de dados não foi encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        catch (SQLException exception){
            JOptionPane.showMessageDialog(null, "Erro na iniciação do acesso ao banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static ImageIcon getImageIcon() {
        return imageIcon;
    }

    public static UsuarioLogado getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(UsuarioLogado usuarioLogado) {
        Main.usuarioLogado = usuarioLogado;
    }

    public static List<JFrame> getJanelas() {
        return janelas;
    }
}
