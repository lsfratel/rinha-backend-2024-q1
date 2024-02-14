--- key:create_transaction
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
    %s,
    %s,
    %s,
    %s
  );