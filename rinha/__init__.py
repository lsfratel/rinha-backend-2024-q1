import re
import pathlib
import functools
from psycopg.rows import dict_row
from psycopg_pool import ConnectionPool
from bottle import Bottle, HTTPError, response, request

from rinha import settings
from rinha.schemas import schema_validator
from rinha.plugins import PsycoPG


def get_queries():
    queries = dict()
    files = pathlib.Path(settings.CURRENT_PATH, "queries").glob("*.sql")
    for file in files:
        with file.open(mode="r", encoding="utf-8") as sql:
            content = sql.read()
            if result := re.search(settings.QUERY_KEY_REGEX, content):
                queries.update({result.group(1): content})
            else:
                raise Exception("Malformed query")
    return queries


def make_app():
    app = Bottle()
    app.config.load_module("rinha.settings")
    create_connection_pool(app)
    install_plugins(app)

    app.error(404, callback=error_404)
    app.error(422, callback=error_all)
    app.error(500, callback=error_all)

    return (app, get_queries())


def create_connection_pool(app: Bottle):
    pool = ConnectionPool(open=True, conninfo=app.config.get(
        "DATABASE_URL"), kwargs=dict(row_factory=dict_row))
    pool.wait()
    app.config.update({"db_pool": pool})


def install_plugins(app):
    app.install(PsycoPG(pool=app.config.get("db_pool")))


def error_404(_):
    return None


def error_all(error: HTTPError):
    if error.status_code == 500:
        print(error.exception)

    response.content_type = 'application/json'

    return '{"code": %s, "error": %s}' % (error.status_code, error.body)


def validate(validator):
    def inner(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            req = request.json
            errors = schema_validator(req, validator)
            if errors:
                raise HTTPError(422, errors)
            return func(*args, **kwargs)
        return wrapper
    return inner
