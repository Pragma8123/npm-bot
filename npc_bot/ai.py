import os
from openai import AsyncClient, BadRequestError
from dotenv import load_dotenv


class RejectedRequestException(Exception):
    pass


class BillingLimitException(Exception):
    pass


load_dotenv()
api_key = os.getenv("OPENAI_API_KEY", "")
client = AsyncClient(api_key=api_key)


async def generate_image(prompt: str) -> str | None:
    try:
        result = await client.images.generate(prompt=prompt)
        return result.data[0].url
    except BadRequestError as error:
        if error.code == "billing_hard_limit_reached":
            raise BillingLimitException
        else:
            raise RejectedRequestException
