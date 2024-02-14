from bottle import HTTPError

from rinha.schemas import schema_validator, transaction_request_schema


def validate(req):
    errors = schema_validator(req, transaction_request_schema)

    if len(errors) > 0:
        raise HTTPError(422, errors)

    return req
