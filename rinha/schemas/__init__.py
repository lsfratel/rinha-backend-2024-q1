from rinha.schemas.transaction import validator as transaction_validator


def schema_validator(data, validator):
    errors = validator.iter_errors(data)
    error_list = []
    for e in errors:
        error_list.append(e.message)
    return error_list
