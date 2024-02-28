from datetime import datetime, UTC
from restcraft import RestCraft
from restcraft.context import Context

from rinha import get_all
from rinha.utils import validate_transaction_input


app = RestCraft()
queries, pool = get_all()


@app.route("/clientes/:id/extrato")
def customer_statement(ctx: Context):
    params = ctx.params

    with pool.connection() as conn:
        customer = conn.execute(queries["get_customer"], params).fetchone()
        if not customer:
            ctx.set_status = 404
            ctx.set_body = "Customer not found"
            return ctx
        transactions = conn.execute(queries["get_transactions"], params).fetchall()
        ctx.set_body = {
            "saldo": {
                "total": customer["balance"],
                "limite": customer["credit"],
                "data_extrato": datetime.now(UTC)
            },
            "ultimas_transacoes": [
                {
                    "tipo": t["type"],
                    "valor": t["amount"],
                    "descricao": t["description"],
                    "realizado_em": t["created_at"]
                } for t in transactions
            ]
        }
    return ctx


@app.route("/clientes/:id/transacoes", method="POST")
def customer_transactions(ctx: Context):
    body = ctx.json
    params = ctx.params
    errors = validate_transaction_input(body)
    if not body or errors:
        ctx.set_status = 422
        ctx.set_body = errors
        return ctx

    with pool.connection() as conn:
        customer = conn.execute(queries["get_customer"], params).fetchone()
        if not customer:
            ctx.set_status = 404
            ctx.set_body = "Customer not found"
            return ctx
        conn.transaction()
        t_type = body["tipo"]
        q = queries["update_balance_debit"] if t_type == "d" else queries["update_balance_credit"]
        ret = conn.execute(q, {"id": params["id"], "value": body["valor"]}).fetchone()
        if not ret:
            conn.rollback()
            ctx.set_status = 422
            ctx.set_body = "Saldo insuficiente"
            return ctx
        conn.execute(queries["create_transaction"], {"id": params["id"],
                                                    "type": t_type,
                                                    "amount": body["valor"],
                                                    "description": body["descricao"]})
        ctx.set_body = ret
    return ctx
