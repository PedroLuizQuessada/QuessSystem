package listener.administrador.automacoes;

import controle.enums.TipoPermissaoEnum;
import view.administrador.automacoes.Automacoes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarListener implements ActionListener {
    private final Automacoes janela;
    private final JTextField nome;
    private final JComboBox<String> tipoPermissao;
    private final JTextField permitidos;
    private final JCheckBox ativo;

    public BuscarListener(Automacoes janela, JTextField nome, JComboBox<String> tipoPermissao, JTextField permitidos, JCheckBox ativo) {
        this.janela = janela;
        this.nome = nome;
        this.tipoPermissao = tipoPermissao;
        this.permitidos = permitidos;
        this.ativo = ativo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String sql;

        if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.USUARIO.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE nome LIKE '%" + nome.getText() + "%' AND ativo = " + ativo.isSelected() + " AND tipopermissao = 'Usuário' ORDER BY nome";
            } else {
                sql = "SELECT a.id AS id, a.nome AS nome, a.tipopermissao AS tipopermissao, a.permitidos AS permitidos, a.ativo AS ativo FROM AUTOMACOES a INNER JOIN USUARIOS u ON u.id = a.permitidos WHERE a.nome LIKE '%" + nome.getText() + "%' AND a.ativo = " + ativo.isSelected() + " AND u.login LIKE '%" + permitidos.getText() + "%' AND a.tipopermissao = 'Usuário' ORDER BY a.nome";
            }
            janela.carregarAutomacoes(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE nome LIKE '%" + nome.getText() + "%' AND ativo = " + ativo.isSelected() + " AND tipopermissao = 'Departamento' ORDER BY nome";
            } else {
                sql = "SELECT a.id AS id, a.nome AS nome, a.tipopermissao AS tipopermissao, a.permitidos AS permitidos, a.ativo AS ativo FROM AUTOMACOES a INNER JOIN DEPARTAMENTOS d ON d.id = a.permitidos WHERE a.nome LIKE '%" + nome.getText() + "%' AND a.ativo = " + ativo.isSelected() + " AND d.nome LIKE '%" + permitidos.getText() + "%' AND a.tipopermissao = 'Departamento' ORDER BY a.nome";
            }
            janela.carregarAutomacoes(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GRUPO.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE nome LIKE '%" + nome.getText() + "%' AND ativo = " + ativo.isSelected() + " AND tipopermissao = 'Grupo' ORDER BY nome";
            } else {
                sql = "SELECT a.id AS id, a.nome AS nome, a.tipopermissao AS tipopermissao, a.permitidos AS permitidos, a.ativo AS ativo FROM AUTOMACOES a INNER JOIN GRUPOS g ON g.id = a.permitidos WHERE a.nome LIKE '%" + nome.getText() + "%' AND a.ativo = " + ativo.isSelected() + " AND g.nome LIKE '%" + permitidos.getText() + "%' AND a.tipopermissao = 'Grupo' ORDER BY a.nome";
            }
            janela.carregarAutomacoes(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GERENTE.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE nome LIKE '%" + nome.getText() + "%' AND ativo = " + ativo.isSelected() + " AND tipopermissao = 'Gerente de departamento' ORDER BY nome";
            } else {
                sql = "SELECT a.id AS id, a.nome AS nome, a.tipopermissao AS tipopermissao, a.permitidos AS permitidos, a.ativo AS ativo FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON u.departamento = d.id INNER JOIN AUTOMACOES a ON d.id = a.permitidos WHERE a.nome LIKE '%" + nome.getText() + "%' AND a.ativo = " + ativo.isSelected() + " AND u.login LIKE '%" + permitidos.getText() + "%' AND a.tipopermissao = 'Gerente de departamento' ORDER BY a.nome";
            }
            janela.carregarAutomacoes(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.LIDER.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE nome LIKE '%" + nome.getText() + "%' AND ativo = " + ativo.isSelected() + " AND tipopermissao = 'Líder de grupo' ORDER BY nome";
            } else {
                sql = "SELECT a.id AS id, a.nome AS nome, a.tipopermissao AS tipopermissao, a.permitidos AS permitidos, a.ativo AS ativo FROM USUARIOS u INNER JOIN GRUPOS g ON u.id = g.lider INNER JOIN AUTOMACOES a ON g.id = a.permitidos WHERE a.nome LIKE '%" + nome.getText() + "%' AND a.ativo = " + ativo.isSelected() + "%' AND u.login LIKE '%" + permitidos.getText() + "%' AND a.tipopermissao = 'Líder de grupo' ORDER BY a.nome";
            }
            janela.carregarAutomacoes(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.PADRAO.getDescricao())) {
            janela.carregarAutomacoes("SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE nome LIKE '%" + nome.getText() + "%' AND ativo = " + ativo.isSelected() + " ORDER BY nome");
        }
    }
}
