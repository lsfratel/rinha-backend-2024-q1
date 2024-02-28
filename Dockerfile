FROM python:alpine

RUN apk add --update --no-cache \
    python3-dev \
    libpq-dev \
    libev-dev \
    curl \
    build-base

WORKDIR /app

COPY docker-requirements.txt .

RUN pip install --no-cache-dir -r docker-requirements.txt

COPY lib lib

RUN pip install lib/restcraft-0.0.1-py3-none-any.whl

RUN rm -rf lib

COPY rinha rinha
COPY bin bin

ARG PORT
ARG WORKERS
ARG THREADS

CMD bin/start.py --host 0.0.0.0 --port ${PORT} --workers ${WORKERS} --threads ${THREADS} rinha.main:app
