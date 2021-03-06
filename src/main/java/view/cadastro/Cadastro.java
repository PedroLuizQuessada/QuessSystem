package view.cadastro;

import controle.ComboboxUtil;
import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.TipoCampoEnum;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.cadastro.AdicionarListener;
import listener.cadastro.BuscarListener;
import listener.cadastro.ExcluirListener;
import listener.cadastro.GerarRelatorioListener;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Cadastro extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();
    private final List<Map<String, JComponent>> camposPesquisaveis = new ArrayList<>();
    private final List<List<String>> infos = new ArrayList<>();
    private final List<JComponent> camposBloqueaveisRelatorio = new ArrayList<>();

    private final int idCadastro;
    private String tabelaCadastro = "";

    public Cadastro(int idCadastro) {
        this.idCadastro = idCadastro;

        try {
            List<Map<String, Object>> tabelaCadastroList = daoUtil.select(String.format("SELECT tabela FROM CADASTROS WHERE id = %d", idCadastro), Collections.singletonList("tabela"));
            this.tabelaCadastro = tabelaCadastroList.get(0).get("tabela").toString();

            carregarCabecalho(jPanel);
            carregarRegistros(String.format("SELECT * FROM %s order BY id", tabelaCadastro));
        } catch (DaoException exception) {
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(jPanel);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), tabelaCadastro, 1200, 700);
    }

    private void carregarCabecalho(JPanel jPanel) {
        try {
            List<String> camposPesquisaveisLabel = new ArrayList<>();
            List<Map<String, Object>> camposPesquisaveis = daoUtil.select(String.format("SELECT id, label, coluna, tipo FROM CAMPOSCADASTROS WHERE pesquisavel = true AND idcadastro = %d", idCadastro), Arrays.asList("id", "label", "coluna", "tipo"));
            jPanel.setLayout(new GridBagLayout());

            for (int i = 0; i < camposPesquisaveis.size(); i++) {
                String campoPesquisavelLabel = camposPesquisaveis.get(i).get("label").toString();
                camposPesquisaveisLabel.add(campoPesquisavelLabel);

                if (i == 0) {
                    c.insets = new Insets(0, 20, 0, 0);
                    c.gridx = 0;
                    c.gridy = 0;
                } else {
                    c.insets = new Insets(0, 40, 0, 0);
                    c.gridx++;
                }

                jPanel.add(new JLabel(campoPesquisavelLabel), c);
            }

            for (int i = 0; i < camposPesquisaveis.size(); i++) {
                Map<String, JComponent> mapCampoPesquisavel = new HashMap<>();
                JComponent filtro;

                if (camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
                    JComboBox<String> filtroCombobox = new JComboBox<>();
                    comboboxUtil.carregarOpcoesCombobox(Integer.parseInt(camposPesquisaveis.get(i).get("id").toString()), filtroCombobox, false);
                    filtro = filtroCombobox;
                } else if (camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
                    JComboBox<String> filtroCombobox = new JComboBox<>();
                    comboboxUtil.carregarOpcoesCombobox(Integer.parseInt(camposPesquisaveis.get(i).get("id").toString()), filtroCombobox, true);
                    filtro = filtroCombobox;
                } else if (camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
                    filtro = new JCheckBox();
                } else {
                    JTextField filtroTexto = new JTextField();
                    filtroTexto.setColumns(7);

                    if (camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
                        filtroTexto.addKeyListener(new NumericoUtil(filtroTexto));
                    } else if (camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
                        filtroTexto.addKeyListener(new DataUtil(filtroTexto));
                    } else if (camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
                        filtroTexto.addKeyListener(new DataHoraUtil(filtroTexto));
                    }

                    filtro = filtroTexto;
                }

                mapCampoPesquisavel.put(camposPesquisaveis.get(i).get("coluna").toString(), filtro);
                this.camposPesquisaveis.add(mapCampoPesquisavel);

                if (i == 0) {
                    c.insets = new Insets(0, 20, 30, 0);
                    c.gridx = 0;
                    c.gridy++;
                } else {
                    c.insets = new Insets(0, 40, 30, 0);
                    c.gridx++;
                }

                camposBloqueaveisRelatorio.add(filtro);
                jPanel.add(filtro, c);
            }

            c.gridx++;
            JButton botaoFiltro = new JButton("Buscar");
            camposBloqueaveisRelatorio.add(botaoFiltro);
            botaoFiltro.addActionListener(new BuscarListener(this, this.camposPesquisaveis, idCadastro));
            jPanel.add(botaoFiltro, c);

            c.gridx++;
            JButton gerarRelatorio = new JButton("Gerar relat??rio");
            gerarRelatorio.addActionListener(new GerarRelatorioListener(camposPesquisaveisLabel, infos, gerarRelatorio, camposBloqueaveisRelatorio));
            jPanel.add(gerarRelatorio, c);

            c.gridx++;
            JButton adicionarRegistro = new JButton("Adicionar registro");
            camposBloqueaveisRelatorio.add(adicionarRegistro);
            adicionarRegistro.addActionListener(new AdicionarListener(this, idCadastro, null, null));
            jPanel.add(adicionarRegistro, c);

            c.gridx++;
            JButton voltar = new JButton("Voltar");
            camposBloqueaveisRelatorio.add(voltar);
            voltar.addActionListener(new VoltarListener(this));
            jPanel.add(voltar, c);
        } catch (DaoException exception) {
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarRegistros(String sql) throws DaoException {
        infos.clear();
        for (JComponent campo : campos) {
            jPanel.remove(campo);
        }
        campos.clear();

        List<String> camposPesquisaveis = new ArrayList<>();
        for (Map<String, JComponent> campoPesquisavel : this.camposPesquisaveis) {
            for (Map.Entry<String, JComponent> valores : campoPesquisavel.entrySet()) {
                camposPesquisaveis.add(valores.getKey());
            }
        }

        List<Map<String, Object>> registros = daoUtil.select(sql, camposPesquisaveis);

        for (Map<String, Object> registro : registros) {
            List<String> info = new ArrayList<>();
            for (int i = 0; i < camposPesquisaveis.size(); i++) {
                if (i == 0) {
                    c.insets = new Insets(0, 20, 0, 0);
                    c.gridx = 0;
                    c.gridy++;
                } else {
                    c.insets = new Insets(0, 40, 0, 0);
                    c.gridx++;
                }

                JComponent campo;
                List<Map<String, Object>> tipoCampoList = daoUtil.select(String.format("SELECT tipo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND coluna = '%s'", idCadastro, camposPesquisaveis.get(i)), Collections.singletonList("tipo"));
                if (tipoCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
                    JCheckBox campoCheckbox = new JCheckBox();
                    boolean informacao = registro.get(camposPesquisaveis.get(i)).toString().equalsIgnoreCase("true");
                    campoCheckbox.setSelected(informacao);
                    campo = campoCheckbox;

                    if (informacao) {
                        info.add("Sim");
                    }
                    else {
                        info.add("N??o");
                    }
                } else {
                    JTextField campoTexto = new JTextField();
                    campoTexto.setColumns(7);
                    try {
                        String informacao = registro.get(camposPesquisaveis.get(i)).toString();
                        campoTexto.setText(informacao);
                        info.add(informacao);

                        if(tipoCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                            DataHoraUtil dataHoraUtil = new DataHoraUtil(campoTexto);
                            String informacaoDataHora = dataHoraUtil.converterDataHoraString();
                            campoTexto.setText(informacaoDataHora);
                            info.remove(info.lastIndexOf(informacao));
                            info.add(informacaoDataHora);
                        }
                        else if (tipoCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
                            DataUtil dataUtil = new DataUtil(campoTexto);
                            String informacaoData = dataUtil.converterDataString();
                            campoTexto.setText(informacaoData);
                            info.remove(info.lastIndexOf(informacao));
                            info.add(informacaoData);
                        }
                    }
                    catch (NullPointerException exception){
                        String informacao = "";
                        campoTexto.setText(informacao);
                        info.add(informacao);
                    }
                    campo = campoTexto;
                }

                campo.setEnabled(false);
                jPanel.add(campo, c);
                campos.add(campo);
            }

            c.gridx++;
            JButton consultar = new JButton("Consultar");
            camposBloqueaveisRelatorio.add(consultar);
            consultar.addActionListener(new AdicionarListener(this, idCadastro, tabelaCadastro, Integer.parseInt(registro.get("id").toString())));
            jPanel.add(consultar, c);
            campos.add(consultar);

            c.gridx++;
            JButton excluir = new JButton("Excluir");
            camposBloqueaveisRelatorio.add(excluir);
            excluir.addActionListener(new ExcluirListener(idCadastro, tabelaCadastro, Integer.parseInt(registro.get("id").toString())));
            jPanel.add(excluir, c);
            campos.add(excluir);

            c.gridy++;
            infos.add(info);
        }

        jFrameUtil.configurarJanela(this, Main.getImageIcon(), tabelaCadastro, 1200, 700);
    }

    public String getTabelaCadastro() {
        return tabelaCadastro;
    }
}
