CREATE TABLE IF NOT EXISTS postagens (
    createdAt TIMESTAMP(6) WITH TIME ZONE,
    id BIGSERIAL NOT NULL,
    updateAt TIMESTAMP(6) WITH TIME ZONE,
    cnpj VARCHAR(255),
    mensagem VARCHAR(255),
    PRIMARY KEY (id)
);

insert into postagens (id, mensagem, cnpj, createdAt, updateAt) values(997, 'Sangue AB+', '87020517000120', '2023-10-19 16:55:59.806000 +00:00', '2023-10-19 16:55:59 +00:00');
insert into postagens (id, mensagem, cnpj, createdAt, updateAt) values(998, 'Sangue O+', '93712735000200', '2023-10-19 16:55:59.806000 +00:00', '2023-10-19 16:55:59 +00:00');
insert into postagens (id, mensagem, cnpj, createdAt, updateAt) values(999, 'Sangue A-', '87020517000120', '2023-10-19 16:55:59.806000 +00:00', '2023-10-19 16:55:59 +00:00');