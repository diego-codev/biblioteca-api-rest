-- V2: Unificação de usuario em pessoa adicionando coluna role e removendo tabela usuarios
-- Adiciona coluna role em pessoa (default USER) se não existir
ALTER TABLE pessoa ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'USER';

-- Opcional: se desejar migrar roles previamente existentes da tabela usuarios para pessoa por email
-- UPDATE pessoa p SET role = u.role FROM usuarios u WHERE lower(p.email) = lower(u.email);

-- Remove tabela usuarios (caso exista)
DROP TABLE IF EXISTS usuarios CASCADE;
