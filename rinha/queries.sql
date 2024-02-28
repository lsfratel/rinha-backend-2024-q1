--- name:create_transaction
INSERT INTO
  transactions
  (
    customer_id,
    type,
    amount,
    description
  )
VALUES
  (
    %(id)s,
    %(type)s,
    %(amount)s,
    %(description)s
  );


--- name:get_customer
SELECT
  c.id,
  c.name,
  c.credit,
  c.balance
FROM
  customers c
WHERE
  c.id = %(id)s;


--- name:get_transactions
SELECT
  t.type,
  t.amount,
  t.description,
  t.created_at::text
FROM
  transactions t
WHERE
  t.customer_id = %(id)s
ORDER BY
  t.created_at DESC
LIMIT 10;


--- name:update_balance_credit
UPDATE
  customers
SET
  balance = balance + %(value)s
WHERE
  id = %(id)s
RETURNING
  balance as saldo,
  credit as limite;


--- name:update_balance_debit
UPDATE
  customers
SET
  balance = balance - %(value)s
WHERE
  id = %(id)s AND balance - %(value)s > -credit
RETURNING
  balance as saldo,
  credit as limite;
