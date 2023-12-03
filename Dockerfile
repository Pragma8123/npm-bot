FROM python:3.11.6-slim AS build

# python
ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1

# poetry
ENV POETRY_NO_INTERACTION=1 \
    POETRY_VIRTUALENVS_CREATE=false

RUN apt-get update \
    && apt-get install --no-install-recommends -y \
    # deps for installing poetry
    curl \
    # deps for building python deps
    build-essential

# install poetry
RUN pip install poetry

# copy project requirement files here to ensure they will be cached.
WORKDIR /app
COPY poetry.lock pyproject.toml ./

# install runtime deps
RUN poetry install --no-dev

COPY . .

# build our wheel
RUN poetry build -f wheel

# `production` image used for runtime
FROM python:3.11.6-slim AS production
WORKDIR /app
COPY --from=build /app/dist/*.whl .
RUN pip install *.whl
CMD ["npc-bot"]
