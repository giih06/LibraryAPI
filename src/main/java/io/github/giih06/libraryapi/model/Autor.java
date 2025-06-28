package io.github.giih06.libraryapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um autor no sistema.
 *
 * Cada autor pode ter múltiplos livros associados (relação 1:N).
 * Possui campos de auditoria para data de criação e atualização automática.
 */
@Entity
@Table(name = "autor", schema = "public")
@Getter
@Setter
@ToString(exclude = "livros")
@EntityListeners(AuditingEntityListener.class)// annotation para atributos que realizam o controle da aplicação e auiditoria do usuário ( capta os dados de modificações e data cadastro)
public class Autor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade", length = 50, nullable = false)
    private String nacionalidade;

    // um autor para muitos livros ; O cascade significa que se for removido o autor, será removido seus livros automaticamente
    // o fetcher eager significa que sempre que o autor for carregado, serão carregados os livros também. No lazy os livros não serão carregados juntos com autor
    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY
    //        ,cascade = CascadeType.ALL
    )
    private List<Livro> livros;

    @CreatedDate//insere automaticamente a data atual no campo
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate// insere automaticamente a última atualização do objetof
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;
}
