CREATE TABLE IF NOT EXISTS ranking (
    id     SERIAL PRIMARY KEY,
    nome   VARCHAR(100) NOT NULL,
    pontos INTEGER      NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_ranking_pontos ON ranking (pontos DESC);