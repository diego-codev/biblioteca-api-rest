package br.com.emakers.biblioteca_api.data.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmprestimoId implements Serializable {
    private Long livro;
    private Long pessoa;
}
