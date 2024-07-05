package br.com.alura.tabelafipe.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import br.com.alura.tabelafipe.model.Dados;
import br.com.alura.tabelafipe.model.Modelos;
import br.com.alura.tabelafipe.model.Veiculo;
import br.com.alura.tabelafipe.service.ConsumoFIPE;
import br.com.alura.tabelafipe.service.ConverteDados;

public class Principal {
  private final String URL_INICIAL = "https://parallelum.com.br/fipe/api/v1/";
  private ConsumoFIPE consumo = new ConsumoFIPE();
  private ConverteDados conversor = new ConverteDados();

  public void exibeMenu() {
    String tipo;
    String codigoMarca;
    Scanner sc = new Scanner(System.in);
    System.out.println(
        "Bem vindo a consulta a tabela fipe, escolha qual tipo de veiculo deseja consultarconsulta:\ncarros\nmotos\ncaminhoes");
    tipo = sc.nextLine();
    if (tipo.contains("caminhão")) {
      tipo = "caminhao";
    }
    String url = URL_INICIAL + tipo.toLowerCase() + "/marcas";

    String jsonMarcas = consumo.obterDados(url);
    exibirDados(jsonMarcas);

    // https://parallelum.com.br/fipe/api/v1/carros/marcas/{marca}/modelos
    System.out.println(
        "Agora informe o código da marca");
    codigoMarca = sc.nextLine();
    url = url + "/" + codigoMarca + "/modelos";
    String jsonModelos = consumo.obterDados(url);
    var modelosLista = conversor.converterDados(jsonModelos, Modelos.class);
    System.out.println("Modelos dessa marca: ");
    modelosLista.modelos().stream()
        .sorted(Comparator.comparing(Dados::codigo))
        .forEach(m -> System.out.println("Código: " + m.codigo() + " Nome: " + m.nome()));

    System.out.println("Digite o nome, ou pelo menos um trecho do nome, do carro que deseja ver a media de preços");
    var nome = sc.nextLine();
    var nomes = conversor.converteNome(nome, modelosLista);
    System.out.println("Modelos filtrados: " + "\n" + nomes);

    System.out.println("Informe o codigo do modelo que dejesa ver");
    var codigoModelo = sc.nextLine();
    exibeAnos(url, codigoModelo);
  }

  private void exibirDados(String json) {
    List<Dados> dados = conversor.converterDadosLista(json, Dados.class);
    dados.forEach(v -> System.out.println("Código: " + v.codigo() + " nome: " + v.nome()));
  }

  private void exibeAnos(String url, String codigo) {
    url = url + "/" + codigo + "/anos";
    var json = consumo.obterDados(url);
    List<Dados> anos = conversor.converterDadosLista(json, Dados.class);
    List<Veiculo> veiculos = new ArrayList<>();

    for (int i = 0; i < anos.size(); i++) {
      var urlAnos = url + "/" + anos.get(i).codigo();
      json = consumo.obterDados(urlAnos);
      Veiculo veiculo = conversor.converterDados(json, Veiculo.class);
      veiculos.add(veiculo);
    }

    System.out.println("Todos os veiculos filtrados com avaliações por ano: ");
    veiculos.forEach(System.out::println);

    // for (String item : codigos) {
    // url = url = url + "/" + item + "/anos/";
    // var json = fipe.obterDados(url);
    // exibirDados(json);
    // }
  }
}
