import os
from pathlib import Path

DATABASE_URL = os.getenv(
    "DATABASE_URL", "postgres://postgres:postgres@localhost/postgres")

CURRENT_PATH = Path(__file__).parent.absolute()

QUERY_KEY_REGEX = r"^---\s*key:(\w+)"

PORT = int(os.getenv("PORT", 8080))
