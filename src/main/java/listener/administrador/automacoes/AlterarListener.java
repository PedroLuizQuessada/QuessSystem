package listener.administrador.automacoes;

import controle.*;
import controle.validacoes.AutomacaoUtil;
import exception.DaoException;
import exception.validacoes.AutomacaoException;
import view.administrador.automacoes.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final AutomacaoUtil automacaoUtil = new AutomacaoUtil();
    private final PermissaoUtil permissaoUtil = new PermissaoUtil();

    private final AdicionarConsultar janela;
    private final Integer id;
    private final JTextField nome;
    private final JTextArea descricao;
    private final JCheckBox horarioLimites;
    private final JTextField horarioLimiteInicio;
    private final JTextField horarioLimiteFim;
    private final JCheckBox maquinaExterna;
    private final JCheckBox diasExecucao;
    private final JCheckBox segunda;
    private final JCheckBox terca;
    private final JCheckBox quarta;
    private final JCheckBox quinta;
    private final JCheckBox sexta;
    private final JCheckBox sabado;
    private final JCheckBox domingo;
    private final JTextField emailFalha;
    private final JComboBox<String> tipoPermissao;
    private final JComboBox<String> permitidos;
    private final JCheckBox ativo;

    public AlterarListener(AdicionarConsultar janela, Integer id) {
        this.janela = janela;
        this.id = id;
        this.nome = janela.getNome();
        this.descricao = janela.getDescricao();
        this.horarioLimites = janela.getHorarioLimites();
        this.horarioLimiteInicio = janela.getHorarioLimiteInicio();
        this.horarioLimiteFim = janela.getHorarioLimiteFim();
        this.maquinaExterna = janela.getMaquinaExterna();
        this.diasExecucao = janela.getDiasExecucao();
        this.segunda = janela.getSegunda();
        this.terca = janela.getTerca();
        this.quarta = janela.getQuarta();
        this.quinta = janela.getQuinta();
        this.sexta = janela.getSexta();
        this.sabado = janela.getSabado();
        this.domingo = janela.getDomingo();
        this.emailFalha = janela.getEmailFalha();
        this.tipoPermissao = janela.getTipoPermissao();
        this.permitidos = janela.getPermitidos();
        this.ativo = janela.getAtivo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            automacaoUtil.validarAutomacao(id, nome, horarioLimites, horarioLimiteInicio, horarioLimiteFim, emailFalha);
            daoUtil.update(String.format("UPDATE AUTOMACOES SET nome = '%s', descricao = '%s', horariolimites = %s, horariolimiteinicio = '%s', horariolimitefim = '%s', maquinaexterna = %s, diasexecucao = %s, segunda = %s, terca = %s, quarta = %s, quinta = %s, sexta = %s, sabado = %s, domingo = %s, emailfalha = '%s', tipopermissao = '%s', permitidos = %d, ativo = %s WHERE id = %d", nome.getText(), descricao.getText(), horarioLimites.isSelected(), horarioLimiteInicio.getText(), horarioLimiteFim.getText(), maquinaExterna.isSelected(), diasExecucao.isSelected(), segunda.isSelected(), terca.isSelected(), quarta.isSelected(), quinta.isSelected(), sexta.isSelected(), sabado.isSelected(), domingo.isSelected(), emailFalha.getText(), tipoPermissao.getSelectedItem(), permissaoUtil.carregaPermitidosId(tipoPermissao.getSelectedItem().toString(), permitidos.getSelectedItem().toString()), ativo.isSelected(), id));

            JOptionPane.showMessageDialog(null, "Automação alterada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException | AutomacaoException exception) {
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
