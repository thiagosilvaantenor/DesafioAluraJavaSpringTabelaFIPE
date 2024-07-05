package br.com.alura.tabelafipe.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoFIPE {

  public String obterDados(String url) {

    HttpClient cliente = HttpClient.newHttpClient();
    HttpRequest requesicao = HttpRequest.newBuilder()
        .uri(URI.create(url)).build();

    HttpResponse<String> response = null;
    try {
      response = cliente
          .send(requesicao, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    String json = response.body();
    
    return json;
  }
}
