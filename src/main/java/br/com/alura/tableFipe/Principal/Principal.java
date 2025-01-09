package br.com.alura.tableFipe.Principal;

import org.springframework.stereotype.Component;

@Component
public class Principal {

    private final MenuFipe menuFipe;

    // Injeção de dependência do MenuFipe
    public Principal(MenuFipe menuFipe) {
        this.menuFipe = menuFipe;
    }

    public void exibeMenu() {
        menuFipe.exibeMenu();
    }
}
