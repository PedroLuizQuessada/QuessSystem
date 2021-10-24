package view.cadastro;

import controle.DaoUtil;
import exception.DaoException;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Cadastro extends JFrame {
    private final DaoUtil daoUtil = new DaoUtil();

    private final int idCadastro;

    public Cadastro(int idCadastro){
        this.idCadastro = idCadastro;
    }

    private void carregarCabecalho(JPanel jPanel) throws DaoException {
        List<Map<String, Object>> camposPesquisaveis = daoUtil.select(String.format("SELECT label, coluna, tipo FROM CAMPOSCADASTROS WHERE pesquisavel = true AND idcadastro = %d", idCadastro), Arrays.asList("label", "coluna", "tipo"));
        jPanel.setLayout(new GridBagLayout());
    }
}
