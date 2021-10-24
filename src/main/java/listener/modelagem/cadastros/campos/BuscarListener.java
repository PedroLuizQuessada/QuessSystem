package listener.modelagem.cadastros.campos;

import controle.enums.OpcaoComboEnum;
import view.modelagem.cadastros.campos.Campos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarListener implements ActionListener {
    private final Campos janela;
    private final Integer idCadastro;
    private final JTextField ordem;
    private final JTextField label;
    private final JTextField coluna;
    private final JComboBox<String> tipo;

    public BuscarListener(Campos janela, Integer idCadastro){
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.ordem = janela.getOrdemFiltro();
        this.label = janela.getLabelFiltro();
        this.coluna = janela.getColunaFiltro();
        this.tipo = janela.getTipoFiltro();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String filtroOrdem = " ";
        String filtroTipo = " ";
        if(!ordem.getText().equalsIgnoreCase("")){
            filtroOrdem = " AND ordem = " + ordem.getText() + " ";
        }
        if(!tipo.getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.CAMPO.getDescricao())){
            filtroTipo = " AND tipo = '" + tipo.getSelectedItem() + "' ";
        }
        janela.carregarCampos("SELECT id, idcadastro, ordem, label, coluna, tipo, nativo, agrupador, ordemagrupador FROM CAMPOSCADASTROS WHERE idcadastro = " + idCadastro + filtroOrdem + filtroTipo + " AND label LIKE '%" + label.getText() + "%' AND coluna LIKE '%" + coluna.getText() + "%' AND inativo <> true ORDER BY ordem, ordemagrupador");
    }
}
