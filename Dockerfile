FROM python:3-slim

WORKDIR /app

RUN apt update -y && apt install git python3-dev libpq-dev build-essential libev-dev -y

COPY rinha rinha
COPY docker-requirements.txt .

RUN pip install -r docker-requirements.txt

RUN apt remove git python3-dev libpq-dev build-essential libev-dev -y
RUN apt clean

CMD ["python", "-m", "rinha"]
