package io.github.giih06.libraryapi.service;

import io.github.giih06.libraryapi.model.Usuario;
import io.github.giih06.libraryapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public void salvar(Usuario usuario) {
        var senha = usuario.getSenha();
        usuario.setSenha(encoder.encode(senha));
        repository.save(usuario);
    }

    public Usuario buscarPorLogin(String login) {
        return repository.findByLogin(login);
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}
