package br.com.alura.tabelafipe.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.Modelos;

public class ConverteDados {
  private ObjectMapper mapper = new ObjectMapper();

  public <T> T converterDados(String json, Class<T> classe) {
    try {
      return mapper.readValue(json, classe);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> converterDadosLista(String json, Class<T> classe) {
    CollectionType lista = mapper.getTypeFactory()
        .constructCollectionType(List.class, classe);
    try {
      return mapper.readValue(json, lista);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Dados> converteNome(String nome, Modelos modelo) {
    List<Dados> modelosFiltrados = modelo.modelos().stream()
        .filter(m -> m.nome().toLowerCase().contains(nome.toLowerCase()))
        .collect(Collectors.toList());

    return modelosFiltrados;

  }

}
