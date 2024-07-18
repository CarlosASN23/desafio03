![Untitled](https://github.com/CarlosASN23/desafio03/assets/128325902/3251f2d0-585a-4ebb-a98a-f1ef14522526)
<h1 align="center"> DESAFIO 03 – Desenvolvendo aplicações para um Ecommerce</h1>

# Índice 

* [Título e Imagem de capa](#Título-e-Imagem-de-capa)
* [Índice](#índice)
* [Descrição do Projeto](#descrição-do-projeto)
* [Status do Projeto](#status-do-Projeto)
* [Funcionalidades e Demonstração da Aplicação](#funcionalidades-e-demonstração-da-aplicação)
* [Tecnologias utilizadas](#tecnologias-utilizadas)
* [Pessoas Contribuidoras](#pessoas-contribuidoras)
* [Pessoas Desenvolvedoras do Projeto](#pessoas-desenvolvedoras)
* [Licença](#licença)

# Descrição do Projeto
<p align="center"> Uma API em Java para simular de forma simples as operações realizadas por um ecommerce, tais como a realização de métodos CRUD para produto e venda</p>

## Diagrama de Classes ER
![Class Diagram0](https://github.com/user-attachments/assets/d4238c72-8ecc-4b79-8ce3-1fe7c6d3baa3)

## Diagrama ER Banco de dados
<p align="center">
<img width="385" alt="Banco" src="https://github.com/user-attachments/assets/6ba5db06-db1d-45f7-9255-c5b4a4c63cae">
</p>

# Status do Projeto
<p align="center">
<img loading="lazy" src="http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge"/>
</p>

# Funcionalidades
- 1.Produto:
  - Permitir que os usuários criem, leiam, atualizem e excluam produtos.
  - Criar validações que façam sentido para os dados de entrada na criação de um produto, por exemplo: Preço do produto só pode ser positivo.
  - Um produto não pode ser deletado após ser incluso em uma venda, porém deve ter alguma maneira de inativar ele.
  - Controlar o estoque do produto de forma que ele não possa ser vendido caso o estoque seja menor do que a quantidade necessária para a venda.
    
- 2.Vendas:
  - Permitir que os usuários criem, leiam, atualizem e excluam vendas (Uma venda tem que ter no mínimo 1 produto para ser concluída).
  - Criar método para filtrar vendas por data.
  - Criar métodos de relatório de vendas, mensal e semanal.
    
- 3.Cache:
  - As leituras na API devem salvar as informações no cache da aplicação, para que as próximas buscas sejam mais rápidas. Deve ser feito um bom gerenciamento do cache, por exemplo: ao inserir uma nova venda, deletar o cache de vendas para que a informação seja buscada no banco de dados diretamente e venha atualizada.
 
- 4.Auntenticação via Token JWT
  - Implementação de um token JWT onde o Token precisa ter um subject (referência para o usuário), um timestamp ou instant para data de expiração e outro para data de criação (expiresAt e issuedAt).
  - Implementação de autorização para usuários autenticadosSomente usuários com permissão de ADMIN podem deletar informações do sistema e cadastrar e atualizar novos produtos. Venda tem que ter um registro do usuário que realizou ela.
  - Implementação de reset de senhas por meio de envio de token por e-mail

# Demonstração Documentação Swagger
<p align="center">  
<img width="951" alt="Swagger 1" src="https://github.com/user-attachments/assets/c82b7a23-7a81-4954-b571-1900ebbbf054">
</p>
<p align="center">
<img width="938" alt="Swagger2" src="https://github.com/user-attachments/assets/86f559f4-9c97-4531-8bb2-a9538b9c1ad1">
</p>
<p align="center">
<img width="937" alt="Swagger3" src="https://github.com/user-attachments/assets/978f3736-9e7b-4ca2-9026-4bacc64245e6">
</p>
<p align="center">
<img width="950" alt="Swagger4" src="https://github.com/user-attachments/assets/fb2867cf-dd88-472b-9901-4d50b56a669f">
</p>
<p align="center">
<img width="825" alt="Swagger5" src="https://github.com/user-attachments/assets/d8a4fbd8-5260-4ec6-9212-7970bf012e74">
</p>

# Tecnologias Utilizadas
- Tecnologias: Java 17 e Spring Boot 3.3.1
- Banco de dados PostgreSQL
- Git e GitHub
- Arquitetura Rest API

# Pessoas Contribuidoras

[<img loading="lazy" src="[https://avatars.githubusercontent.com/u/37356058?v=4](https://avatars.githubusercontent.com/u/128325902?v=4)" width=115><br><sub>Carlos Alberto Souza Nascimento</sub>]([https://github.com/camilafernanda](https://github.com/CarlosASN23))

# Pessoas Desenvolvedoras do Projeto
[<img loading="lazy" src="[https://avatars.githubusercontent.com/u/37356058?v=4](https://avatars.githubusercontent.com/u/128325902?v=4)" width=115><br><sub>Carlos Alberto Souza Nascimento</sub>]([https://github.com/camilafernanda](https://github.com/CarlosASN23))

# Licença
MIT licensed.
