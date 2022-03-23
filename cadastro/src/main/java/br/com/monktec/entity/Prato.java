package br.com.monktec.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prato")
public class Prato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String nome;

    public String descricao;

    @ManyToOne
    public Restaurante restaurante;

    public BigDecimal preco;
}
