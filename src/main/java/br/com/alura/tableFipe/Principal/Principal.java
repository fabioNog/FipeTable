package br.com.alura.tableFipe.Principal;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Principal {

    private static final Scanner leitura = new Scanner(System.in);
    private static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private static final int OPCAO_SAIR = 0;
    private static final int OPCAO_CARRO = 1;
    private static final int OPCAO_MOTO = 2;
    private static final int OPCAO_CAMINHAO = 3;

    public void exibeMenu() {
        Map<Integer, String> opcoes = new HashMap<>();
        opcoes.put(OPCAO_CARRO, URL_BASE + "carros/marcas");
        opcoes.put(OPCAO_MOTO, URL_BASE + "motos/marcas");
        opcoes.put(OPCAO_CAMINHAO, URL_BASE + "caminhoes/marcas");

        int opcao = -1;

        while (opcao != OPCAO_SAIR) {
            System.out.println("""
                *** OPÇÕES ***
                0) Sair
                1) Carro
                2) Moto
                3) Caminhão               
                Digite uma das opções para consultar:
                """);

            try {
                opcao = Integer.parseInt(leitura.nextLine());
                if (opcao == OPCAO_SAIR) {
                    System.out.println("Até logo!");
                } else if (opcoes.containsKey(opcao)) {
                    System.out.println(opcoes.get(opcao));
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
    }
}
