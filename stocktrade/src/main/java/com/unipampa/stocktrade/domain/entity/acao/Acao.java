package com.unipampa.stocktrade.domain.entity.acao;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "acao")
@Table(name = "tb_acao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Acao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String sigla;

    private Double valor;

}