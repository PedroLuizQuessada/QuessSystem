package view.cadastro;

import controle.*;
import controle.enums.TipoCampoEnum;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.cadastro.SalvarAlterarListener;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdicionarConsultar extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final RadioUtil radioUtil = new RadioUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final JButton voltar = new JButton("Voltar");
    private int posX = 0;
    private final JButton jButton = new JButton("Salvar");
    private final Map<String, Object> campos = new HashMap<>();

    public AdicionarConsultar(Integer idCadastro, String nomeCadastro, Integer idRegistro){
        List<Map<String, Object>> info = new ArrayList<>();
        String titulo = "Consultar Registro";
        jPanel.setLayout(new GridBagLayout());
        jButton.addActionListener(new SalvarAlterarListener(this, idCadastro, idRegistro, campos));
        voltar.addActionListener(new VoltarListener(this, idCadastro));
        c.gridy = 0;

        try {
            List<Map<String, Object>> campos = daoUtil.select(String.format("SELECT id, label, coluna, tipo, bloqueado, obrigatorio, quebralinha, nativo FROM CAMPOSCADASTROS WHERE vinculado = true AND idcadastro = %d ORDER BY ordem", idCadastro), Arrays.asList("id", "label", "coluna", "tipo", "bloqueado", "obrigatorio", "quebralinha", "nativo"));
            for (int i = 0; i < campos.size(); i++){
                if (idRegistro != null) {
                    jButton.setText("Alterar");
                    info = daoUtil.select(String.format("SELECT %s FROM %s WHERE id = %d", campos.get(i).get("coluna"), nomeCadastro, idRegistro), Collections.singletonList(String.format("%s", campos.get(i).get("coluna"))));
                }
                if(i == 0 || (campos.get(i).get("quebralinha") != null && campos.get(i).get("quebralinha").toString().equalsIgnoreCase("true"))){
                    c.insets = new Insets(0, 20, 0, 0);
                    c.gridx = 0;

                    if(i != 0){
                        c.gridy++;
                    }
                }
                else {
                    c.gridx++;
                    c.insets = new Insets(0, 40, 0, 0);
                }

                jPanel.add(new JLabel(campos.get(i).get("label").toString()), c);

                JComponent campo = new JTextField();
                c.insets.top = 45;

                if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())){
                    JTextField campoTexto = new JTextField();
                    if (idRegistro != null){
                        try {
                            campoTexto.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                        }
                        catch (NullPointerException exception){
                            campoTexto.setText("");
                        }
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                        if(configsCampo.get(0).get("valorpadrao") != null) {
                            campoTexto.setText(configsCampo.get(0).get("valorpadrao").toString());
                        }
                    }
                    campoTexto.setColumns(7);
                    campo = campoTexto;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())){
                    JTextField campoNumerico = new JTextField();
                    if(idRegistro != null){
                        try {
                            campoNumerico.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                        }
                        catch (NullPointerException exception){
                            campoNumerico.setText("");
                        }
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                        if(configsCampo.get(0).get("valorpadrao") != null) {
                            campoNumerico.setText(configsCampo.get(0).get("valorpadrao").toString());
                        }
                    }
                    campoNumerico.setColumns(7);
                    campoNumerico.addKeyListener(new NumericoUtil(campoNumerico));
                    campo = campoNumerico;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                    JTextField campoDataHora = new JTextField();
                    if(idRegistro != null){
                        DataHoraUtil dataHoraUtil = new DataHoraUtil(campoDataHora);
                        try {
                            campoDataHora.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                            campoDataHora.setText(dataHoraUtil.converterDataHoraString());
                        }
                        catch (NullPointerException exception){
                            campoDataHora.setText("");
                        }
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                        if(configsCampo.get(0).get("valorpadrao") != null) {
                            campoDataHora.setText(configsCampo.get(0).get("valorpadrao").toString());
                        }
                    }
                    campoDataHora.setColumns(7);
                    campoDataHora.addKeyListener(new DataHoraUtil(campoDataHora));
                    campo = campoDataHora;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                    JTextField campoData = new JTextField();
                    if(idRegistro != null){
                        DataUtil dataUtil = new DataUtil(campoData);
                        try {
                            campoData.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                            campoData.setText(dataUtil.converterDataString());
                        }
                        catch (NullPointerException exception){
                            campoData.setText("");
                        }
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao FROM CONFIGSCAMPOSDATA WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("valorpadrao"));
                        if(configsCampo.get(0).get("valorpadrao") != null) {
                            campoData.setText(configsCampo.get(0).get("valorpadrao").toString());
                        }
                    }
                    campoData.setColumns(7);
                    campoData.addKeyListener(new DataUtil(campoData));
                    campo = campoData;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                    JCheckBox campoCheckbox = new JCheckBox();
                    if(idRegistro != null){
                        campoCheckbox.setSelected(Boolean.parseBoolean(info.get(0).get(campos.get(i).get("coluna")).toString()));
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT estadopadrao FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("estadopadrao"));
                        campoCheckbox.setSelected(Boolean.parseBoolean(configsCampo.get(0).get("estadopadrao").toString()));
                    }
                    campo = campoCheckbox;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())){
                    JTextArea campoAreaTexto = new JTextArea();
                    c.insets.top = 90;
                    AreaTextoUtil areaTextoUtil = new AreaTextoUtil(campoAreaTexto);
                    if(idRegistro != null){
                        try {
                            campoAreaTexto.setText(info.get(0).get(campos.get(i).get("coluna")).toString());
                        }
                        catch (NullPointerException exception){
                            campoAreaTexto.setText("");
                        }
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT valorpadrao, limitecaracteres FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Arrays.asList("valorpadrao", "limitecaracteres"));
                        if(configsCampo.get(0).get("valorpadrao") != null) {
                            campoAreaTexto.setText(configsCampo.get(0).get("valorpadrao").toString());
                        }
                        if(configsCampo.get(0).get("limitecaracteres") != null){
                            areaTextoUtil = new AreaTextoUtil(campoAreaTexto, Integer.parseInt(configsCampo.get(0).get("limitecaracteres").toString()));
                            campoAreaTexto.addKeyListener(areaTextoUtil);
                        }
                    }
                    areaTextoUtil.configurarTextArea();
                    campo = campoAreaTexto;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())){
                    JComboBox<String> campoCombobox = new JComboBox<>();
                    comboboxUtil.carregarOpcoesCombobox(Integer.parseInt(campos.get(i).get("id").toString()), campoCombobox, false);
                    if(idRegistro != null){
                        try {
                            campoCombobox.setSelectedItem(info.get(0).get(campos.get(i).get("coluna")).toString());
                        }
                        catch (NullPointerException exception){

                        }
                    }
                    else {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT opcaopadrao FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d AND cadastro = true", Integer.parseInt(campos.get(i).get("id").toString())), Collections.singletonList("opcaopadrao"));
                        if(configsCampo.get(0).get("valorpadrao") != null) {
                            campoCombobox.setSelectedItem(configsCampo.get(0).get("opcaopadrao").toString());
                        }
                    }
                    campo = campoCombobox;
                }
                else if (campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())){
                    ButtonGroup campoRadio = new ButtonGroup();
                    List<JRadioButton> botoes = radioUtil.carregarOpcoesRadio(Integer.parseInt(campos.get(i).get("id").toString()), campoRadio);
                    for(JRadioButton botao: botoes){
                        botao.setActionCommand(botao.getText());
                        jPanel.add(botao, c);
                        c.insets.top = c.insets.top + 45;

                        if(idRegistro != null && info.get(0).get(campos.get(i).get("coluna")) != null && info.get(0).get(campos.get(i).get("coluna")).toString().equals(botao.getText())){
                            botao.setSelected(true);
                        }

                        if(campos.get(i).get("bloqueado").toString().equalsIgnoreCase("true") || (campos.get(i).get("nativo") != null && campos.get(i).get("nativo").toString().equalsIgnoreCase("true"))){
                            botao.setEnabled(false);
                        }
                    }

                    this.campos.put(campos.get(i).get("coluna").toString(), campoRadio);
                }

                if(campos.get(i).get("bloqueado").toString().equalsIgnoreCase("true") || (campos.get(i).get("nativo") != null && campos.get(i).get("nativo").toString().equalsIgnoreCase("true"))){
                    campo.setEnabled(false);
                }

                if(!campos.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
                    jPanel.add(campo, c);

                    if(campos.get(i).get("nativo") == null) {
                        this.campos.put(campos.get(i).get("coluna").toString(), campo);
                    }
                }

                try {
                    if (c.gridy == 0 && (campos.get(i + 1).get("quebralinha") != null && campos.get(i + 1).get("quebralinha").toString().equalsIgnoreCase("true"))) {
                        c.gridx++;
                        c.insets.top = 45;
                        jPanel.add(voltar, c);
                    }
                }
                catch (IndexOutOfBoundsException exception){
                    c.gridx++;
                    jPanel.add(voltar, c);
                }

                if (c.gridx > posX){
                    posX = c.gridx;
                }
            }

            c.gridy++;
            c.gridx = posX / 2;
            c.insets.top = 40;

            if(posX % 2 == 1){
                c.insets.left = c.insets.left + 180;
            }

            jPanel.add(jButton, c);

            if(campos.size() == 0){
                c.insets.left = 40;
                c.gridx++;
                jPanel.add(voltar, c);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(jPanel);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 1200, 700);
    }
}
