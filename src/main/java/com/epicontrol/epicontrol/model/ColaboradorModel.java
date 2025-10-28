package com.epicontrol.epicontrol.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorModel {
   
    private Long id;
    private String nome;
    private String re;
    private String funcao;
    private String setor;
    private  String data_admissao;

}
