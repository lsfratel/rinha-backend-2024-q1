from jsonschema import Draft202012Validator


schema = {
    "$schema": Draft202012Validator.META_SCHEMA["$id"],
    "type": "object",
    "required": [
        "tipo",
        "valor",
        "descricao"
    ],
    "properties": {
        "tipo": {
            "type": "string",
            "enum": ["c", "d"]
        },
        "valor": {
            "type": "integer",
            "minimum": 0
        },
        "descricao": {
            "type": "string",
            "minLength": 1,
            "maxLength": 10
        }
    }
}


validator = Draft202012Validator(schema)
