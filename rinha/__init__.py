import re
from psycopg.rows import dict_row
from psycopg_pool import ConnectionPool

from rinha import settings


def get_queries():
    rx = re.compile(r'--- name:(.*?)\n(.*?)(?=\n--- name:|\Z)', re.DOTALL)
    with open("rinha/queries.sql", "r") as f:
        tmp = f.read()
        matches = rx.findall(tmp)
        return {match[0].strip(): match[1].strip() for match in matches}


def get_connection_pool():
    pool = ConnectionPool(open=True, conninfo=settings.DATABASE_URL,
                          min_size=4, max_size=10, kwargs=dict(row_factory=dict_row))
    pool.wait()
    return pool


def get_all():
    return get_queries(), get_connection_pool()
