--- key:get_statement
SELECT
  JSON_BUILD_OBJECT('total', c.balance, 'limite', c.credit, 'data_extrato', now()::text) saldo,
  (
    SELECT
      COALESCE(JSON_AGG(t), '[]')
    FROM
      (
        SELECT
          t.type as tipo,
          t.amount as valor,
          t.description as descricao,
          t.created_at::text as realizado_em
        FROM
          transactions t
        WHERE
          t.customer_id = c.id
        ORDER BY
          t.created_at DESC
        LIMIT
          10
      ) t
  ) ultimas_transacoes
FROM
  customers c
WHERE
  c.id = %s;