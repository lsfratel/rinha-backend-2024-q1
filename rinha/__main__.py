from rinha import settings
from rinha.main import app


if __name__ == "__main__":
    app.run(server="bjoern", host="0.0.0.0", port=settings.PORT, debug=False)
