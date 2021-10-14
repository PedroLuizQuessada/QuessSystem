package listener.modelagem.cadastros;

import controle.enums.TipoPermissaoEnum;
import view.modelagem.cadastros.Cadastros;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarListener implements ActionListener {
    private final Cadastros janela;
    private final JTextField nome;
    private final JComboBox<String> tipoPermissao;
    private final JTextField permitidos;

    public BuscarListener(Cadastros janela, JTextField nome, JComboBox<String> tipoPermissao, JTextField permitidos) {
        this.janela = janela;
        this.nome = nome;
        this.tipoPermissao = tipoPermissao;
        this.permitidos = permitidos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String sql;

        if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.USUARIO.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE nome LIKE '%" + nome.getText() + "%' AND tipopermissao = 'Usuário' ORDER BY nome";
            } else {
                sql = "SELECT c.id AS id, c.nome AS nome, c.tabela AS tabela, c.tipopermissao AS tipopermissao, c.permitidos AS permitidos FROM CADASTROS c INNER JOIN USUARIOS u ON u.id = c.permitidos WHERE c.nome LIKE '%" + nome.getText() + "%' AND u.login LIKE '%" + permitidos.getText() + "%' AND c.tipopermissao = 'Usuário' ORDER BY c.nome";
            }
            janela.carregarCadastros(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE nome LIKE '%" + nome.getText() + "%' AND tipopermissao = 'Departamento' ORDER BY nome";
            } else {
                sql = "SELECT c.id AS id, c.nome AS nome, c.tabela AS tabela, c.tipopermissao AS tipopermissao, c.permitidos AS permitidos FROM CADASTROS c INNER JOIN DEPARTAMENTOS d ON d.id = c.permitidos WHERE c.nome LIKE '%" + nome.getText() + "%' AND d.nome LIKE '%" + permitidos.getText() + "%' AND c.tipopermissao = 'Departamento' ORDER BY c.nome";
            }
            janela.carregarCadastros(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GRUPO.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE nome LIKE '%" + nome.getText() + "%' AND tipopermissao = 'Grupo' ORDER BY nome";
            } else {
                sql = "SELECT c.id AS id, c.nome AS nome, c.tabela AS tabela, c.tipopermissao AS tipopermissao, c.permitidos AS permitidos FROM CADASTROS c INNER JOIN GRUPOS g ON g.id = c.permitidos WHERE c.nome LIKE '%" + nome.getText() + "%' AND g.nome LIKE '%" + permitidos.getText() + "%' AND c.tipopermissao = 'Grupo' ORDER BY c.nome";
            }
            janela.carregarCadastros(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.GERENTE.getDescricao())) {
            if (permitidos.getText().equalsIgnoreCase("")) {
                sql = "SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE nome LIKE '%" + nome.getText() + "%' AND tipopermissao = 'Gerente de departamento' ORDER BY nome";
            } else {
                sql = "SELECT c.id AS id, c.nome AS nome, c.tabela AS tabela, c.tipopermissao AS tipopermissao, c.permitidos AS permitidos FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON u.departamento = d.id INNER JOIN CADASTROS c ON d.id = c.permitidos WHERE c.nome LIKE '%" + nome.getText() + "%' AND u.login LIKE '%" + permitidos.getText() + "%' AND c.tipopermissao = 'Gerente de departamento' ORDER BY c.nome";
            }
            janela.carregarCadastros(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.LIDER.getDescricao())) {
                if (permitidos.getText().equalsIgnoreCase("")) {
                    sql = "SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE nome LIKE '%" + nome.getText() + "%' AND tipopermissao = 'Líder de grupo' ORDER BY nome";
                } else {
                    sql = "SELECT c.id AS id, c.nome AS nome, c.tabela AS tabela, c.tipopermissao AS tipopermissao, c.permitidos AS permitidos FROM USUARIOS u INNER JOIN GRUPOS g ON u.id = g.lider INNER JOIN CADASTROS c ON g.id = c.permitidos WHERE c.nome LIKE '%" + nome.getText() + "%' AND u.login LIKE '%" + permitidos.getText() + "%' AND c.tipopermissao = 'Líder de grupo' ORDER BY c.nome";
                }
                janela.carregarCadastros(sql);
        }
        else if(tipoPermissao.getSelectedItem().toString().equalsIgnoreCase(TipoPermissaoEnum.PADRAO.getDescricao())) {
                janela.carregarCadastros("SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE nome LIKE '%" + nome.getText() + "%' ORDER BY nome");
        }
    }
}
