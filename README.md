# UNILINK
## Descrição
O UniLink foi desenvolvido com o objetivo de centralizar e divulgar os projetos de extensão da UFES - Goiabeiras, tornando mais fácil para os estudantes descobrirem e conhecerem as iniciativas disponíveis.
A plataforma oferece uma visualização simples e intuitiva de todos os projetos, com possibilidade de buscar por nome, filtrar por centro de ensino, verificar quais estão com inscrições abertas e explorar projetos por tags — palavras-chave que identificam o nicho ou característica principal de cada projeto (como tecnologia, saúde, educação, entre outros).
O foco é proporcionar uma forma rápida e acessível para que os alunos encontrem informações essenciais sobre os projetos e possam participar ativamente das oportunidades oferecidas.

Este repositório contempla o backend da aplicação. O repositório do frontend está disponível [aqui](https://github.com/raphaelitos/unilink-frontend).

## Diagrama UML

![UML](imgs/uml.png)


## Ferramentas escolhidas
- **Framework Backend**: Spring Boot
- **Building**: Maven
- **Testes**: JUnit e Mockito
- **Versionamento**: Git e Github
- **Virtualização e portabilidade**: Docker
- **Geração de documentação**: JavaDoc

## Execução

Para clonar e acessar o repositório, execute os seguintes comandos no terminal:
```bash
git clone https://github.com/joaoloss/unilink-backend.git
cd unilink-backend
```

Para iniciar a API, execute o seguinte comando:
```bash
./mvnw spring-boot:run
```

## Gerar documentação

Para gerar a documentação do projeto, execute o seguinte comando no terminal:
```bash
./mvnw javadoc:javadoc
```

## Desenvolvedores
* [Conrado Antoniazi dos Santos](https://github.com/ConradoAntoniazi)
* [João Pedro Pereira Loss](https://github.com/joaoloss)
* [Raphael Correia Dornelas](https://github.com/raphaelitos)
