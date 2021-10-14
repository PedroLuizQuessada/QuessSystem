package listener.modelagem.cadastros;

import controle.DaoUtil;
import controle.validacoes.CadastroUtil;
import exception.DaoException;
import exception.validacoes.CadastroException;
import main.Main;
import view.modelagem.cadastros.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CriarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final CadastroUtil cadastroUtil = new CadastroUtil();

    private final AdicionarConsultar janela;
    private final JTextField nome;
    private final JTextField tabela;
    private final JComboBox<String> tipoPermissao;
    private final JComboBox<String> permitidos;

    public CriarListener(AdicionarConsultar janela) {
        this.janela = janela;
        this.nome = janela.getNome();
        this.tabela = janela.getTabela();
        this.tipoPermissao = janela.getTipoPermissao();
        this.permitidos = janela.getPermitidos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            cadastroUtil.validarCadastro(null, nome, tabela.getText());

            daoUtil.create(String.format("CREATE TABLE %s " +
                    "(id INTEGER NOT NULL IDENTITY, " +
                    "\nregistrado_em TIMESTAMP, " +
                    "\nregistrado_por INTEGER, " +
                    "\nultima_atualizacao TIMESTAMP, " +
                    "\natualizado_por INTEGER, " +
                    "\nPRIMARY KEY(id))", tabela.getText()));

            daoUtil.insert(String.format("INSERT INTO CADASTROS (nome, tabela, tipopermissao, permitidos) VALUES ('%s', '%s', '%s', %d)", nome.getText(), tabela.getText(), tipoPermissao.getSelectedItem().toString(), cadastroUtil.carregaPermitidosId(tipoPermissao.getSelectedItem().toString(), permitidos.getSelectedItem().toString())));

            List<Map<String, Object>> cadastroIdList = daoUtil.select("SELECT MAX(id) AS id FROM CADASTROS", Collections.singletonList("id"));
            int cadastroId = Integer.parseInt(cadastroIdList.get(0).get("id").toString());

            daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, label, coluna, tipo, vinculado, bloqueado, obrigatorio, nativo) VALUES (%d, 1, 'ID', 'id', 'Numérico', false, false, false, true)", cadastroId));
            daoUtil.insert("INSERT INTO CONFIGSCAMPOSNUMERICO (idcampo, cadastro) VALUES (0, true)");

            daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, label, coluna, tipo, vinculado, bloqueado, obrigatorio, nativo) VALUES (%d, 2, 'Registrado em', 'registrado_em', 'Data hora', false, false, false, true)", cadastroId));
            daoUtil.insert("INSERT INTO CONFIGSCAMPOSDATAHORA (idcampo, cadastro) VALUES (1, true)");

            daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, label, coluna, tipo, vinculado, bloqueado, obrigatorio, nativo) VALUES (%d, 3, 'Registrado por', 'registrado_por', 'Numérico', false, false, false, true)", cadastroId));
            daoUtil.insert("INSERT INTO CONFIGSCAMPOSNUMERICO (idcampo, cadastro) VALUES (2, true)");

            daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, label, coluna, tipo, vinculado, bloqueado, obrigatorio, nativo) VALUES (%d, 4, 'Última atualização', 'ultima_atualizacao', 'Data hora', false, false, false, true)", cadastroId));
            daoUtil.insert("INSERT INTO CONFIGSCAMPOSDATAHORA (idcampo, cadastro) VALUES (3, true)");

            daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, label, coluna, tipo, vinculado, bloqueado, obrigatorio, nativo) VALUES (%d, 5, 'Atualizado por', 'atualizado_por', 'Numérico', false, false, false, true)", cadastroId));
            daoUtil.insert("INSERT INTO CONFIGSCAMPOSNUMERICO (idcampo, cadastro) VALUES (4, true)");

            janela.dispose();
            Main.getJanelas().add(new AdicionarConsultar(cadastroId));
            JOptionPane.showMessageDialog(null, "Cadastro adicionado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (CadastroException | DaoException exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
