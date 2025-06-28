package io.github.giih06.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um livro no sistema da Library API.
 *
 * Cada livro possui um autor relacionado e campos de auditoria para rastreamento
 * de criação e atualização. Essa entidade é persistida na tabela "livro".
 */
@Entity
@Table(name = "livro")
@Data // è uma coposição de várias annotations como Getter, Setter, toString, EqualdAndHashCode e RequierdArgsConstructor
@ToString(exclude = "autor")
@EntityListeners(AuditingEntityListener.class)
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 18, scale = 2)
    private BigDecimal preco;

    // chave estrangeira
    @ManyToOne(
            // cascade = CascadeType.ALL,//Um autor pode ter muitos livros
             fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autor")
    private Autor autor;

    @CreatedDate//insere automaticamente a data atual no campo
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate// insere automaticamente a última atualização do objetof
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;
}
