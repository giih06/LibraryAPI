package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelas regras de negócio relacionadas à entidade Usuario.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    // Repositório para operações de persistência com a entidade Usuario
    private final UsuarioRepository repository;

    // Encoder responsável por criptografar senhas com segurança
    private final PasswordEncoder encoder;

    /**
     * Salva um novo usuário no banco de dados.
     * A senha informada é criptografada antes da persistência.
     *
     * @param usuario usuário a ser salvo
     */
    public void salvar(Usuario usuario) {
        var senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));
        repository.save(usuario);
    }

    /**
     * Busca um usuário pelo seu login.
     *
     * @param login login do usuário
     * @return usuário correspondente, ou null se não encontrado
     */
    public Usuario buscarPorLogin(String login) {
        return repository.findByLogin(login);
    }

    /**
     * Busca um usuário pelo seu e-mail.
     *
     * @param email e-mail do usuário
     * @return usuário correspondente, ou null se não encontrado
     */
    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}
