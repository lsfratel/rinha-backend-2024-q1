--- key:get_customer
SELECT
  c.id,
  c.name,
  c.credit,
  c.balance
FROM
  customers c
WHERE
  c.id = %s;
