package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService service;
    private Utilizador u1, u2, u3;

    MockedStatic<FileUtils> fileMock;

    @BeforeEach
    void setup() throws Exception {

        // Mock da classe FileUtils
        fileMock = mockStatic(FileUtils.class);

        File fakeFile = mock(File.class);

        when(FileUtils.getUsersFile()).thenReturn(fakeFile);
        fileMock.when(FileUtils::initialize).thenAnswer(inv -> null);


        // Fake ficheiro "existe" e tem conteúdo vazio
        when(fakeFile.exists()).thenReturn(true);
        when(fakeFile.length()).thenReturn(0L);

        service = new UserService();

        // Criar alguns utilizadores com teu construtor atualizado
        u1 = new Utilizador(
                "joao", "pass1", "Funcionario",
                "João", "Silva", "desc", "PT", "2000-01-01",
                5.0, "08:00", "16:00"
        );

        u2 = new Utilizador(
                "ana", "pass2", "Admin",
                "Ana", "Costa", "desc2", "PT", "1999-06-10",
                6.0, "09:00", "17:00"
        );

        u3 = new Utilizador(
                "bruno", "pass3", "Funcionario",
                "Bruno", "Santos", "desc3", "PT", "1998-03-20",
                5.5, "07:00", "15:00"
        );
    }

    @AfterEach
    void cleanup() {
        fileMock.close();
    }

    // ---------------------------
    //          TESTE ADD USER
    // ---------------------------

    @Test
    void testAddUser() throws Exception {

        FileWriter writer = mock(FileWriter.class);
        mockFileWriter(writer);

        service.addUser(u1);

        assertEquals(1, service.getAllUsers().size());
        assertEquals("joao", service.getAllUsers().get(0).getUsername());

        verify(writer, atLeastOnce()).write(anyString());
    }

    // ---------------------------
    //          TESTE REMOVE
    // ---------------------------

    @Test
    void testRemoveUser() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);
        service.addUser(u2);

        service.removeUser("joao");

        assertEquals(1, service.getAllUsers().size());
        assertNull(service.getByUsername("joao"));
    }

    // ---------------------------
    //         TESTE UPDATE
    // ---------------------------

    @Test
    void testUpdateUser() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);

        Utilizador novos = new Utilizador(
                "joao", "novaPass", "Admin",
                "João Pedro", "Almeida", "nova desc", "BR", "2001-05-10",
                7.5, "08:30", "16:30"
        );

        service.updateUser("joao", novos);

        Utilizador result = service.getByUsername("joao");

        assertEquals("João Pedro", result.getNome());
        assertEquals("Admin", result.getTipo());
        assertEquals(7.5, result.getSalario());
        assertEquals("08:30", result.getTurnoEntrada());
    }

    // ---------------------------
    //          TESTE GET
    // ---------------------------

    @Test
    void testGetByUsername() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);
        service.addUser(u2);

        assertEquals("Ana", service.getByUsername("ana").getNome());
        assertNull(service.getByUsername("inexistente"));
    }

    // ---------------------------
    //      TESTE LISTAGEM ORDENADA
    // ---------------------------

    @Test
    void testListarOrdenado() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);
        service.addUser(u2);
        service.addUser(u3);

        List<Utilizador> lista = service.listarOrdenado();

        assertEquals("ana", lista.get(0).getUsername());
        assertEquals("bruno", lista.get(1).getUsername());
        assertEquals("joao", lista.get(2).getUsername());
    }

    // ---------------------------
    //        TESTE POR TIPO
    // ---------------------------

    @Test
    void testListarPorTipo() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);
        service.addUser(u2);
        service.addUser(u3);

        List<Utilizador> funcionarios = service.listarPorTipo("Funcionario");

        assertEquals(2, funcionarios.size());
        assertTrue(funcionarios.stream().anyMatch(u -> u.getUsername().equals("joao")));
        assertTrue(funcionarios.stream().anyMatch(u -> u.getUsername().equals("bruno")));
    }

    // ---------------------------
    //      TESTE BUSCAR POR NOME
    // ---------------------------

    @Test
    void testBuscarPorNome() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);
        service.addUser(u2);

        List<Utilizador> r = service.buscarPorNome("joão");

        assertEquals(1, r.size());
        assertEquals("joao", r.get(0).getUsername());
    }

    // ---------------------------
    //         PAGINAÇÃO
    // ---------------------------

    @Test
    void testListarPaginado() throws Exception {

        mockFileWriter(mock(FileWriter.class));

        service.addUser(u1);
        service.addUser(u2);
        service.addUser(u3);

        List<Utilizador> page0 = service.listarPaginado(0, 2);
        List<Utilizador> page1 = service.listarPaginado(1, 2);

        assertEquals(2, page0.size());
        assertEquals(1, page1.size());
    }

    // =======================================
    //        AUXILIAR PARA MOCK FILEWRITER
    // =======================================

    private void mockFileWriter(FileWriter writer) throws Exception {
        MockedStatic<FileWriter> writerMock = mockStatic(FileWriter.class);
        writerMock.when(() -> new FileWriter(any(File.class))).thenReturn(writer);
    }
}
