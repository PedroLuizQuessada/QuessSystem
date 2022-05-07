package listener.administrador.automacoes;

import controle.DaoUtil;
import controle.PermissaoUtil;
import controle.validacoes.AutomacaoUtil;
import exception.DaoException;
import exception.validacoes.AutomacaoException;
import main.Main;
import view.administrador.automacoes.AdicionarConsultar;
import view.administrador.automacoes.Automacoes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CriarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final AutomacaoUtil automacaoUtil = new AutomacaoUtil();
    private final PermissaoUtil permissaoUtil = new PermissaoUtil();

    private final AdicionarConsultar janela;
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

    public CriarListener(AdicionarConsultar janela) {
        this.janela = janela;
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
            automacaoUtil.validarAutomacao(null, nome, horarioLimites, horarioLimiteInicio, horarioLimiteFim, emailFalha);

            daoUtil.insert(String.format("INSERT INTO AUTOMACOES (nome, descricao, horariolimites, horariolimiteinicio, horariolimitefim, maquinaexterna, diasexecucao, segunda, terca, quarta, quinta, sexta, sabado, domingo, emailfalha, tipopermissao, permitidos, ativo) VALUES ('%s', '%s', %s, '%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', %d, %s)", nome.getText(), descricao.getText(), horarioLimites.isSelected(), horarioLimiteInicio.getText(), horarioLimiteFim.getText(), maquinaExterna.isSelected(), diasExecucao.isSelected(), segunda.isSelected(), terca.isSelected(), quarta.isSelected(), quinta.isSelected(), sexta.isSelected(), sabado.isSelected(), domingo.isSelected(), emailFalha.getText(), tipoPermissao.getSelectedItem(), permissaoUtil.carregaPermitidosId(tipoPermissao.getSelectedItem().toString(), permitidos.getSelectedItem().toString()), ativo.isSelected()));

            janela.dispose();
            Main.getJanelas().add(new Automacoes());
            JOptionPane.showMessageDialog(null, "Automação adicionada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (AutomacaoException | DaoException exception){
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
