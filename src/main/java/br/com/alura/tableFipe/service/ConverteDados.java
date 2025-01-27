package br.com.alura.tableFipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ConverteDados implements IConverteDados {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Erro de mapeamento JSON: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> List<T> obterLista(String json, Class<T> classe) {
        CollectionType lista = mapper.getTypeFactory()
                .constructCollectionType(List.class, classe);
        try {
            return mapper.readValue(json, lista);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Erro de mapeamento JSON: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar JSON: " + e.getMessage(), e);
        }
    }
}
