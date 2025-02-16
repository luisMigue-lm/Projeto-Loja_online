package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dao.FuncionarioDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Funcionario;

public class TelaFuncionarioController {

    @FXML
    private AnchorPane apCadastro;

    @FXML
    private AnchorPane apPesquisa;

    @FXML
    private BorderPane bPane;

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnCadastrarCliente;

    @FXML
    private Button btnDeletar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnOpcoes;

    @FXML
    private Button btnPesquisar;

    @FXML
    private Button btnPesquisarFuncionario;

    @FXML
    private Button btnVoltar;

    @FXML
    private TableColumn<Funcionario, Integer> colCPF;

    @FXML
    private TableColumn<Funcionario, LocalDate> colDtNascmt;

    @FXML
    private TableColumn<Funcionario, String> colEmail;

    @FXML
    private TableColumn<Funcionario, Integer> colIdFuncionario;

    @FXML
    private TableColumn<Funcionario, String> colNome;

    @FXML
    private TableColumn<Funcionario, Integer> colSenha;

    @FXML
    private DatePicker dpDtNascimento;

    @FXML
    private TableView<Funcionario> tbvFuncionarios;

    @FXML
    private TextField tfCPF;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPesquisa;

    @FXML
    private TextField tfSenha;

    ObservableList<Funcionario> obsFunc;

    @FXML
    private void initialize() {
        colIdFuncionario.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeFuncionario"));
        colSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpfFuncionario"));
        colDtNascmt.setCellValueFactory(new PropertyValueFactory<>("dtNascimento"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailFuncionario"));

        obsFunc = FXCollections.observableArrayList();
        tbvFuncionarios.setItems(obsFunc);

        tbvFuncionarios.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            btnEditar.setDisable(newValue == null);
            btnDeletar.setDisable(newValue == null);

        });

    }

    private void alerta(AlertType tipo, String titulo, String cabecalho, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(cabecalho);
        alerta.setContentText(mensagem);
        alerta.show();
    }

    private void limparCampos() {
        tfNome.clear();
        tfSenha.clear();
        tfCPF.clear();
        dpDtNascimento.setValue(null);
        tfEmail.clear();
    }

    private void salvarAtualizacao(int idFuncionario) {
        try {
            String nomeFuncionario = tfNome.getText().trim();
            String cpfFuncionario = tfCPF.getText().trim().replaceAll("[^\\d]", "");
            LocalDate dtNascimento = dpDtNascimento.getValue();
            String senha = tfSenha.getText().trim().replaceAll("[^\\d]", "");
            String emailFuncionario = tfEmail.getText().trim();
    
            if (!nomeFuncionario.contains(" ")) {
                alerta(AlertType.ERROR, "ERRO!", "Nome incompleto!",
                        "Seu nome precisa ter um espaço dividindo o nome do sobrenome!");
                return;
            }
    
            if (nomeFuncionario.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "Nome inválido!", "O campo Nome não pode estar vázio.");
                return;
            }
    
            if (!cpfFuncionario.matches("\\d+")) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O CPF só pode conter números.");
                return;
            }
    
            if (cpfFuncionario.contains(" ")) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O campo CPF não pode conter espaços.");
                return;
            }
    
            if (cpfFuncionario.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O campo CPF não pode estar vázio.");
                return;
            }
    
            if (cpfFuncionario.length() != 11) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O campo CPF precisa ter 11 ou 14 digitos.");
                return;
            }
    
            if (cpfFuncionario.length() == 11) {
                cpfFuncionario = cpfFuncionario.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            }
    
            if (dpDtNascimento.getValue() == null) {
                alerta(AlertType.ERROR, "ERRO!", "Data inválida!", "Por favor, selecione uma data.");
                return;
            }
    
            if (!senha.matches("\\d+")) {
                alerta(AlertType.ERROR, "ERRO!", "Senha Inválida!", "A senha deve conter apenas números.");
                return;
            }
    
            if (senha.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "Senha inválida!", "O campo Senha não pode estar vázio.");
                return;
            }
    
            if (senha.length() < 4) {
                alerta(AlertType.ERROR, "ERRO!", "Senha inválida!", "A senha tem que possuir no mínino 4 caracteres numéricos.");
                return;
            }
    
            if (emailFuncionario.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O campo Email não pode estar vázio.");
                return;
            }
    
            if (emailFuncionario.length() < 5) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O Email tem que possuir mais de 5 caracteres.");
                return;
            }
    
            if (emailFuncionario.contains(" ")) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O campo Email não pode conter espaços.");
                return;
            }
    
            if (!emailFuncionario.contains("@")) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O campo Email possuí um @.");
                return;
            }
    
            Funcionario funcionarioAtualizado = new Funcionario(idFuncionario, nomeFuncionario, senha, cpfFuncionario,
                    dtNascimento, emailFuncionario);
    
            if (FuncionarioDao.atualizar(funcionarioAtualizado)) {
                alerta(AlertType.INFORMATION, "Sucesso!", "É um sucesso!", "Funcionário atualizado com sucesso!");
    
                btnPesquisar.setDisable(false);
                btnOpcoes.setDisable(false);
                btnCadastrar.setDisable(false);
                tbvFuncionarios.refresh();
                limparCampos();
    
            } else {
                alerta(AlertType.ERROR, "ERRO!", "Encontremos um erro!", "Erro ao atualizar Funcionário!");
    
            }
    
            btnCadastrarCliente.setText("Cadastrar");
            btnCadastrarCliente.setOnAction(this::btnCadastrarClienteOnClick);
            
        } catch (Exception e) {
            alerta(AlertType.ERROR, "ERRO!", "Erro Inesperado", "Ocorreu um erro: " + e.getMessage());
        }

    }

    Stage stage;
    public void setStage(Stage stg) {
        preencherDados((Funcionario) stg.getUserData());
    }

    private void preencherDados(Funcionario funcionario) {
        tfNome.setText(funcionario.getNomeFuncionario());
        tfSenha.setText(funcionario.getSenha());
        tfCPF.setText(funcionario.getCpfFuncionario());
        dpDtNascimento.setValue(funcionario.getDtNascimento());
        tfEmail.setText(funcionario.getEmailFuncionario());

    }

    @FXML
    private void fecharTela() {
        Stage primaryStage = (Stage) btnVoltar.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    void btnCadastrarClienteOnClick(ActionEvent event) {
        try {
            String nomeFuncionario = tfNome.getText().trim();
            String cpfFuncionario = tfCPF.getText().trim().replaceAll("[^\\d]", "");
            LocalDate dtNascimento = dpDtNascimento.getValue();
            String senha = tfSenha.getText().trim().replaceAll("[^\\d]", "");
            String emailFuncionario = tfEmail.getText().trim();

            if (!nomeFuncionario.contains(" ")) {
                alerta(AlertType.ERROR, "ERRO!", "Nome incompleto!",
                        "Seu nome precisa ter um espaço dividindo o nome do sobrenome!");
                return;
            }

            if (nomeFuncionario.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "Nome inválido!", "O campo Nome não pode estar vázio.");
                return;
            }

            if (!cpfFuncionario.matches("\\d+")) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O CPF só pode conter números.");
                return;
            }

            if (cpfFuncionario.contains(" ")) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O campo CPF não pode conter espaços.");
                return;
            }

            if (cpfFuncionario.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O campo CPF não pode estar vázio.");
                return;
            }

            if (cpfFuncionario.length() != 11) {
                alerta(AlertType.ERROR, "ERRO!", "CPF inválido!", "O campo CPF precisa ter 11 ou 14 digitos.");
                return;
            }

            if (cpfFuncionario.length() == 11) {
                cpfFuncionario = cpfFuncionario.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            }

            if (dpDtNascimento.getValue() == null) {
                alerta(AlertType.ERROR, "ERRO!", "Data inválida!", "Por favor, selecione uma data.");
                return;
            }

            if (!senha.matches("\\d+")) {
                alerta(AlertType.ERROR, "ERRO!", "Senha Inválida!", "A senha deve conter apenas números.");
                return;
            }

            if (senha.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "Senha inválida!", "O campo Senha não pode estar vázio.");
                return;
            }

            if (senha.length() < 4) {
                alerta(AlertType.ERROR, "ERRO!", "Senha inválida!", "A senha tem que possuir no mínino 4 caracteres numéricos.");
                return;
            }

            if (emailFuncionario.isEmpty()) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O campo Email não pode estar vázio.");
                return;
            }

            if (emailFuncionario.length() < 5) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O Email tem que possuir mais de 5 caracteres.");
                return;
            }

            if (emailFuncionario.contains(" ")) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O campo Email não pode conter espaços.");
                return;
            }

            if (!emailFuncionario.contains("@")) {
                alerta(AlertType.ERROR, "ERRO!", "Email inválido!", "O campo Email possuí um @.");
                return;
            }

            Funcionario Funcionario = new Funcionario(1, nomeFuncionario, senha, cpfFuncionario, dtNascimento,
                    emailFuncionario);

            if (FuncionarioDao.cadastrar(Funcionario)) {
                alerta(AlertType.INFORMATION, "Sucesso!", "É um sucesso!", "Funcionário cadastrado com sucesso!");

                limparCampos();

            } else {
                alerta(AlertType.ERROR, "ERRO!", "Encontremos um erro!", "Erro ao cadastrar Funcionário!");
            }

        } catch (Exception e) {
            alerta(AlertType.ERROR, "ERRO!", "Erro Inesperado", "Ocorreu um erro: " + e.getMessage());
        }

    }

    @FXML
    void btnCadastrarOnClick(ActionEvent event) {
        btnDeletar.setDisable(true);

        apCadastro.setVisible(true);
        apPesquisa.setVisible(false);
    }

    @FXML
    void btnDeletarOnClick(ActionEvent event) {
        Funcionario funcionarioSelecionado = tbvFuncionarios.getSelectionModel().getSelectedItem();

        if (funcionarioSelecionado != null) {
            Alert alertaDeletar = new Alert(AlertType.CONFIRMATION);
            alertaDeletar.setTitle("Confirmação");
            alertaDeletar.setHeaderText("Você tem certeza?");
            alertaDeletar.setContentText(
                    "Deseja realmente excluir o funcionário: " + funcionarioSelecionado.getNomeFuncionario() + "?");

            Optional<ButtonType> resposta = alertaDeletar.showAndWait();

            if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
                if (FuncionarioDao.deletar(funcionarioSelecionado)) {
                    obsFunc.remove(funcionarioSelecionado);
                    tbvFuncionarios.refresh();

                    alerta(AlertType.INFORMATION, "Sucesso!", "É um sucesso!", "Funcionário excluído com sucesso!");

                } else {
                    alerta(AlertType.ERROR, "ERRO!", "OCORREU UM ERRO!", "Encontramos um erro ao realizar a ação!");

                }
            }
        }
    }

    @FXML
    void btnEditarOnClick(ActionEvent event) {
        Funcionario funcionarioSelecionado = tbvFuncionarios.getSelectionModel().getSelectedItem();
        preencherDados(funcionarioSelecionado);

        apCadastro.setVisible(true);
        apPesquisa.setVisible(false);

        btnCadastrar.setDisable(true);
        btnPesquisar.setDisable(true);
        btnDeletar.setDisable(true);
        btnOpcoes.setDisable(true);

        btnCadastrarCliente.setText("Atualizar");
        btnCadastrarCliente.setOnAction(e -> salvarAtualizacao(funcionarioSelecionado.getIdFuncionario()));
    }

    @FXML
    void btnLimparOnclick(ActionEvent event) {
        obsFunc.clear();
    }

    @FXML
    void btnOpcoesOnClick(ActionEvent event) {
        apCadastro.setVisible(false);
        apPesquisa.setVisible(false);
        limparCampos();
        obsFunc.clear();

    }

    @FXML
    void btnPesquisarFuncionariosOnClick(ActionEvent event) {
        String pesquisa = tfPesquisa.getText().trim();
        List<Funcionario> funcionariosCadastrados = FuncionarioDao.listar(pesquisa);

        obsFunc.clear();
        obsFunc.addAll(funcionariosCadastrados);

        tbvFuncionarios.refresh();

        if (funcionariosCadastrados.isEmpty()) {
            alerta(AlertType.WARNING, "AVISO!", "É um AVISO!", "Nenhum funcionário encontrado!");

        }

    }

    @FXML
    void btnPesquisarOnClick(ActionEvent event) {
        apCadastro.setVisible(false);
        apPesquisa.setVisible(true);

    }

    @FXML
    void btnVoltarOnClick(ActionEvent event) throws IOException {
        URL url = getClass().getResource("/view/TelaLogin.fxml");
        Parent root = FXMLLoader.load(url);

        Stage stgTelaPrincipal = new Stage();
        stgTelaPrincipal.setTitle("Morcegão | Login");
        stgTelaPrincipal.getIcons().add(new Image("file:src/resources/imgs/Logo - Laranja.png"));
        stgTelaPrincipal.setScene(new Scene(root));
        stgTelaPrincipal.show();

        fecharTela();
    }

}
