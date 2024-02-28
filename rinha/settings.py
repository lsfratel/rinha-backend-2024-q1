import os

PORT = int(os.environ.get("PORT", "8080"))
HOST = os.environ.get("HOST", "0.0.0.0")
DATABASE_URL = os.environ.get("DATABASE_URL", "postgresql://postgres:postgres@localhost/rinha-2024-q1")
