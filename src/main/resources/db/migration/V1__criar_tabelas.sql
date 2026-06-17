CREATE TABLE empresa(
    id BIGSERIAL PRIMARY KEY,
    nome_empresa VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    endereco VARCHAR(255) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP
);

CREATE TABLE colaboradores(
    id BIGSERIAL PRIMARY KEY,
    nome_colaborador VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(15) UNIQUE NOT NULL,
    id_empresa BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,

    CONSTRAINT fk_colaboradores_empresa FOREIGN KEY (id_empresa) REFERENCES empresa (id)
);

CREATE TABLE coordenadas(
    id BIGSERIAL PRIMARY KEY,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    id_empresa BIGINT NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,

    CONSTRAINT fk_coordenada_empresa FOREIGN KEY (id_empresa) REFERENCES empresa (id)
);

CREATE TABLE fotos(
    id BIGSERIAL PRIMARY KEY,
    foto BYTEA NOT NULL,
    id_colaborador BIGINT NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,

    CONSTRAINT fk_fotos_colaboradores FOREIGN KEY (id_colaborador) REFERENCES colaboradores (id)
);

CREATE TABLE ponto(
    id BIGSERIAL PRIMARY KEY,
    id_colaborador BIGINT NOT NULL,
    id_responsavel BIGINT NOT NULL,
    ponto TIMESTAMP NOT NULL,

    CONSTRAINT fk_ponto_colaboradores FOREIGN KEY (id_colaborador) REFERENCES colaboradores (id),
    CONSTRAINT fk_ponto_colaboradores_responsavel FOREIGN KEY (id_responsavel) REFERENCES colaboradores (id)
);
