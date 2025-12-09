package com.example.utils;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileUtilsTest {

    private final String DATA_FOLDER = "SistemaOrganizacaoData";

    @BeforeAll
    void cleanupBefore() throws IOException {
        // Limpar pasta de dados antes de iniciar os testes
        File folder = new File(DATA_FOLDER);
        if (folder.exists()) {
            deleteFolder(folder);
        }
    }

    @AfterAll
    void cleanupAfter() throws IOException {
        // Limpar pasta de dados depois dos testes
        File folder = new File(DATA_FOLDER);
        if (folder.exists()) {
            deleteFolder(folder);
        }
    }

    private void deleteFolder(File folder) throws IOException {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                deleteFolder(file);
            }
        }
        Files.deleteIfExists(folder.toPath());
    }

    @Test
    void testInitialize_CriaPastasEFicheiros() {
        FileUtils.initialize();

        File dataFolder = FileUtils.getDataFolder();
        assertNotNull(dataFolder);
        assertTrue(dataFolder.exists() && dataFolder.isDirectory());

        assertTrue(FileUtils.getUsersFile().exists());
        assertTrue(FileUtils.getTurnosFile().exists());
        assertTrue(FileUtils.getNacionalidadesFile().exists());
        assertTrue(FileUtils.getPagamentosFile().exists());
        assertTrue(FileUtils.getMovimentosFile().exists());
    }

    @Test
    void testSalvarECarregarNacionalidades() {
        FileUtils.initialize();

        List<String> nacionalidades = Arrays.asList("Portugal", "Espanha", "França");
        FileUtils.salvarNacionalidades(nacionalidades);

        List<String> carregadas = FileUtils.carregarNacionalidades();
        assertNotNull(carregadas);
        assertEquals(3, carregadas.size());
        assertTrue(carregadas.contains("Portugal"));
        assertTrue(carregadas.contains("Espanha"));
        assertTrue(carregadas.contains("França"));
    }

    @Test
    void testGettersNaoNulos() {
        FileUtils.initialize();

        assertNotNull(FileUtils.getUsersFile());
        assertNotNull(FileUtils.getTurnosFile());
        assertNotNull(FileUtils.getNacionalidadesFile());
        assertNotNull(FileUtils.getPagamentosFile());
        assertNotNull(FileUtils.getMovimentosFile());
        assertNotNull(FileUtils.getDataFolder());
    }
}
