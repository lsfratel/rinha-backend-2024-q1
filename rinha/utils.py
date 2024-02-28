from typing import Any


def v_valor(v: Any):
    if not isinstance(v, int) or v < 0:
        return False
    return True


def v_tipo(v: Any):
    if not isinstance(v, str) or v not in ("c", "d"):
        return False
    return True


def v_descricao(v: Any):
    if not isinstance(v, str) or len(v) not in range(1, 11):
        return False
    return True


def validate_transaction_input(data: dict[str, Any] | None):
    if data is None:
        return {
            "tipo": "Tipo é obrigatório",
            "valor": "Valor é obrigatório",
            "descricao": "Descrção é obrigatório"
        }
    errors = {}
    if not v_tipo(data.get("tipo")):
        errors["tipo"] = "Tipo é obrigatório, e apenas 'c' para crédito ou 'd' para débito."

    if not v_valor(data.get("valor")):
        errors["valor"] = "Valor é obrigatório, e deve ser um inteiro positivo."

    if not v_descricao(data.get("descricao")):
        errors["descricao"] = "Descricao é obrigatório, e deve ser menor que 10 caracteres."

    return errors
