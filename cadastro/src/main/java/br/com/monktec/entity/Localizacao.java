package br.com.monktec.entity;

import javax.persistence.*;

@Entity
@Table(name = "localizacao")
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Double longitude;

    public Double latitude;
}
