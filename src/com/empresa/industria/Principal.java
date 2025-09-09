package com.empresa.industria;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    public static void main(String[] args) {
        // 3.1 - Inserir funcionários (mesma ordem da tabela)
        List<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        // 3.2 - Remover "João"
        funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase("João"));

        // Formatações (data dd/MM/yyyy e moeda pt-BR com . como milhar e , como decimal)
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

        // 3.3 - Imprimir todos os funcionários (dados formatados)
        System.out.println("---- Lista de Funcionários ----");
        funcionarios.forEach(f -> {
            System.out.println(
                f.getNome() + " | " +
                f.getDataNascimento().format(dtf) + " | R$ " +
                df.format(f.getSalario()) + " | " +
                f.getFuncao()
            );
        });

        // 3.4 - Aumento de 10%
        funcionarios.forEach(f -> {
            BigDecimal novo = f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);
            f.setSalario(novo);
        });

        // 3.5 - Agrupar por função (Map<String, List<Funcionario>>)
        Map<String, List<Funcionario>> porFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        // 3.6 - Imprimir agrupado por função
        System.out.println("\n---- Funcionários por Função ----");
        porFuncao.forEach((funcao, lista) -> {
            System.out.println(funcao + ":");
            lista.forEach(f -> System.out.println("  " + f.getNome() + " - " + f.getDataNascimento().format(dtf) + " - R$ " + df.format(f.getSalario())));
        });

        // 3.8 - Aniversariantes nos meses 10 e 12
        System.out.println("\n---- Aniversariantes (mês 10 e 12) ----");
        funcionarios.stream()
                .filter(f -> f.getDataNascimento().getMonthValue() == 10 || f.getDataNascimento().getMonthValue() == 12)
                .forEach(f -> System.out.println(f.getNome() + " - " + f.getDataNascimento().format(dtf)));

        // 3.9 - Funcionário com maior idade (mais velho)
        Funcionario maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento)) // data menor => mais velho
                .orElse(null);
        if (maisVelho != null) {
            int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
            System.out.println("\nMais velho: " + maisVelho.getNome() + " - " + idade + " anos");
        }

        // 3.10 - Lista em ordem alfabética
        System.out.println("\n---- Funcionários em ordem alfabética ----");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(f -> System.out.println(f.getNome()));

        // 3.11 - Total dos salários
        BigDecimal total = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("\nTotal dos salários: R$ " + df.format(total));

        // 3.12 - Quantos salários mínimos ganha cada funcionário (mínimo = 1212.00)
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        System.out.println("\n---- Quantos salários mínimos ----");
        funcionarios.forEach(f -> {
            BigDecimal qtd = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " ganha " + qtd + " salários mínimos.");
        });
    }
}
