package com.epicontrol.epicontrol.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisModel {

  private Long id;
  private String nome;
  private String ca;
  private String validade;
  private Integer quantidade;
  private String createdAt;
  
}
