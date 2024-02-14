from inspect import signature
from bottle import PluginError


class PsycoPG:
    name = 'psycopg'
    api = 2

    def __init__(self, pool):
        self.pool = pool

    def setup(self, app):
        for other in app.plugins:
            if not isinstance(other, PsycoPG):
                continue
            if other.keyword == self.keyword:
                raise PluginError(
                    "Found another PsycoPG plugin with conflicting settings (non-unique keyword).")

    def apply(self, callback, context):
        url = context.config.get('DATABASE_URL')

        if not url:
            raise Exception("Please provide DATABASE_URL")

        args = signature(context.callback)

        if not args.parameters.get("conn"):
            return callback

        def wrapper(*args, **kwargs):
            with self.pool.connection() as conn:
                kwargs["conn"] = conn
                return callback(*args, **kwargs)

        return wrapper
