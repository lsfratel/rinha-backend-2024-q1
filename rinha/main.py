from bottle import HTTPError, request

from rinha import make_app, validate
from rinha.schemas import transaction_validator


app, queries = make_app()


@app.get("/clientes/<id:int>/extrato")
def customer_statement(id, conn):
    customer_statement = conn.execute(
        queries.get("get_statement"), (id,)).fetchone()

    if not customer_statement:
        raise HTTPError(404, "Customer not found")

    return customer_statement


@app.post("/clientes/<id:int>/transacoes")
@validate(transaction_validator)
def customer_transactions(id, conn):
    req = request.json
    with conn.transaction():
        conn.execute("select pg_advisory_xact_lock(%s)", (id,))

        customer = conn.execute(queries.get("get_customer"), (id,)).fetchone()

        if not customer:
            raise HTTPError(404, "Customer not found")

        if req["tipo"] == "d" and (customer["balance"] - req["valor"] < -customer["credit"]):
            raise HTTPError(422, "Saldo insuficiente")

        if req["tipo"] == "d":
            customer["balance"] -= req["valor"]
        else:
            customer["balance"] += req["valor"]

        conn.execute(queries.get("update_customer"),
                     (customer["balance"], customer["id"]))

        conn.execute(queries.get("create_transaction"),
                     (customer["id"], req["tipo"], req["valor"], req["descricao"]))

        return dict(limite=customer["credit"], saldo=customer["balance"])
