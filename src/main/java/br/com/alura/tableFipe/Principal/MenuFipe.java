package br.com.alura.tableFipe.Principal;

import br.com.alura.tableFipe.model.Dados;
import br.com.alura.tableFipe.model.Modelos;
import br.com.alura.tableFipe.model.Veiculo;
import br.com.alura.tableFipe.service.ConsumoApi;
import br.com.alura.tableFipe.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MenuFipe {

    private static final Scanner leitura = new Scanner(System.in);
    private static final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private static final int OPCAO_SAIR = 0;
    private static final int OPCAO_CARRO = 1;
    private static final int OPCAO_MOTO = 2;
    private static final int OPCAO_CAMINHAO = 3;

    private final ConsumoApi consumoApi;
    private final ConverteDados conversor = new ConverteDados();

    public MenuFipe(ConsumoApi consumoApi) {
        this.consumoApi = consumoApi;
    }

    public void exibeMenu() {
        Map<Integer, String> opcoes = new HashMap<>();
        opcoes.put(OPCAO_CARRO, URL_BASE + "carros/marcas");
        opcoes.put(OPCAO_MOTO, URL_BASE + "motos/marcas");
        opcoes.put(OPCAO_CAMINHAO, URL_BASE + "caminhoes/marcas");

        int opcao = -1;

        while (opcao != OPCAO_SAIR) {
            System.out.println("""
                *** INICIO OPÇÕES ***
                0) Sair
                1) Carro
                2) Moto
                3) Caminhão
                *** FIM OPÇÕES ***
                Digite uma das opções para consultar:
                """);

            try {
                opcao = Integer.parseInt(leitura.nextLine());
                if (opcao == OPCAO_SAIR) {
                    System.out.println("Até logo!");
                } else if (opcoes.containsKey(opcao)) {
                    processarConsulta(opcoes.get(opcao));
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }
    }

    private void processarConsulta(String enderecoBase) {
        try {
            System.out.println("Consultando marcas em: " + enderecoBase);
            String resposta = consumoApi.obterDados(enderecoBase);

            var marcas = conversor.obterLista(resposta, Dados.class);

            if (marcas.isEmpty()) {
                System.out.println("Nenhuma marca encontrada.");
                return;
            }

            marcas.stream()
                    .sorted(Comparator.comparing(Dados::codigo))
                    .forEach(System.out::println);

            System.out.println("Informe o código da marca para consulta:");
            String codigoMarca = leitura.nextLine();

            Optional<Dados> marcaSelecionada = marcas.stream()
                    .filter(marca -> marca.codigo().equals(codigoMarca))
                    .findFirst();

            if (marcaSelecionada.isEmpty()) {
                System.out.println("Código de marca inválido. Por favor, tente novamente.");
                return;
            }

            String enderecoModelos = enderecoBase + "/" + codigoMarca + "/modelos";
            System.out.println("Consultando modelos em: " + enderecoModelos);

            resposta = consumoApi.obterDados(enderecoModelos);
            var modeloLista = conversor.obterDados(resposta, Modelos.class);

            if (modeloLista.modelos().isEmpty()) {
                System.out.println("Nenhum modelo encontrado para a marca selecionada.");
                return;
            }

            modeloLista.modelos().stream()
                    .sorted(Comparator.comparing(Dados::codigo))
                    .forEach(System.out::println);

            System.out.println("\nDigite um trecho do nome a ser buscado");

            var nomeVeiculo = leitura.nextLine();

            List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                    .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                    .collect(Collectors.toList());

            System.out.println("\n Modelos FIltrados");

            modelosFiltrados.forEach(System.out::println);

            System.out.println("Digite por favor o código do modelo para buscar os valores de avaliação");

            var codigoModelo = leitura.nextLine();

            String enderecoAnos = enderecoModelos + "/" + codigoModelo + "/anos";

            System.out.println(enderecoAnos);

            resposta = consumoApi.obterDados(enderecoAnos);

            List<Dados> anos = conversor.obterLista(resposta, Dados.class);

            List<Veiculo> veiculos = new ArrayList<>();

            for (int i = 0; i < anos.size(); i++) {
                var getEnderecoAnos = enderecoAnos + "/" + anos.get(i).codigo();
                resposta = consumoApi.obterDados(getEnderecoAnos);
                Veiculo veiculo = conversor.obterDados(resposta, Veiculo.class);
                veiculos.add(veiculo);
            }

            System.out.println("\nTodos os veiculos filtrados com avaliações por ano: ");
            veiculos.forEach(System.out::println);

        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. O código da marca deve ser numérico.");
        } catch (Exception e) {
            System.out.println("Erro ao processar a consulta: " + e.getMessage());
        }
    }
}
