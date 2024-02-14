--- key:update_customer
UPDATE
  customers
SET
  balance = %s
WHERE
  id = %s;
