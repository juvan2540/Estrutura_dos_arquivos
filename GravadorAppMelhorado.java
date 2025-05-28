import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GravadorAppMelhorado extends Application {
    private File arquivoSelecionado;
    private File destinoSelecionado;

    private Label labelArquivo = new Label("Nenhuma música selecionada");
    private Label labelDestino = new Label("Nenhum destino selecionado");
    private TextField artistaField = new TextField();
    private TextField faixaField = new TextField();
    private Button btnGravar = new Button("Gravar no Chip");

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gravador de Músicas para Rádio");

        Label titulo = new Label("🎵 Gravador de Músicas para Rádio de Carro MP3");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnSelecionarMusica = new Button("Selecionar Música (.mp3)");
        btnSelecionarMusica.setOnAction(e -> selecionarArquivo(stage));

        Button btnSelecionarDestino = new Button("Selecionar Destino (USB ou SD)");
        btnSelecionarDestino.setOnAction(e -> selecionarDestino(stage));

        artistaField.setPromptText("Nome do artista");
        faixaField.setPromptText("Nome da faixa");

        btnGravar.setDisable(true);
        btnGravar.setOnAction(e -> gravarArquivo());

        // Atualiza o botão "Gravar" sempre que os campos mudarem
        artistaField.textProperty().addListener((obs, oldText, newText) -> atualizarBotaoGravar());
        faixaField.textProperty().addListener((obs, oldText, newText) -> atualizarBotaoGravar());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(titulo, 0, 0, 2, 1);

        grid.add(btnSelecionarMusica, 0, 1);
        grid.add(labelArquivo, 1, 1);

        grid.add(new Label("Artista:"), 0, 2);
        grid.add(artistaField, 1, 2);

        grid.add(new Label("Faixa:"), 0, 3);
        grid.add(faixaField, 1, 3);

        grid.add(btnSelecionarDestino, 0, 4);
        grid.add(labelDestino, 1, 4);

        grid.add(btnGravar, 0, 5);

        Scene scene = new Scene(grid, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void selecionarArquivo(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            arquivoSelecionado = file;
            labelArquivo.setText(file.getAbsolutePath());
            atualizarBotaoGravar();
        }
    }

    private void selecionarDestino(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(stage);
        if (dir != null) {
            destinoSelecionado = dir;
            labelDestino.setText(dir.getAbsolutePath());
            atualizarBotaoGravar();
        }
    }

    private void atualizarBotaoGravar() {
        boolean pronto = arquivoSelecionado != null
                && destinoSelecionado != null
                && !artistaField.getText().trim().isEmpty()
                && !faixaField.getText().trim().isEmpty();
        btnGravar.setDisable(!pronto);
    }

    private void gravarArquivo() {
        String nomeArtista = artistaField.getText().trim();
        String nomeFaixa = faixaField.getText().trim();
        String novoNome = nomeArtista + " - " + nomeFaixa + ".mp3";
        File destino = new File(destinoSelecionado, novoNome);

        try {
            if (destino.exists()) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "Arquivo já existe. Deseja substituir?", ButtonType.YES, ButtonType.NO);
                alerta.setHeaderText(null);
                alerta.showAndWait();
                if (alerta.getResult() == ButtonType.NO) {
                    return;
                }
            }
            Files.copy(arquivoSelecionado.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION, "✅ Música gravada com sucesso!");
            sucesso.setHeaderText(null);
            sucesso.show();

            // Limpa campos
            arquivoSelecionado = null;
            destinoSelecionado = null;
            labelArquivo.setText("Nenhuma música selecionada");
            labelDestino.setText("Nenhum destino selecionado");
            artistaField.clear();
            faixaField.clear();
            atualizarBotaoGravar();

        } catch (IOException e) {
            Alert erro = new Alert(Alert.AlertType.ERROR, "❌ Erro ao gravar: " + e.getMessage());
            erro.setHeaderText(null);
            erro.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

