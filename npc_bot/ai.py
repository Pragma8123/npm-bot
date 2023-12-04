import asyncio
import openai
from openai import BadRequestError
from concurrent.futures import ThreadPoolExecutor
from functools import partial


class RejectedRequestException(Exception):
    pass


class BillingLimitException(Exception):
    pass


async def generate_image(prompt):
    loop = asyncio.get_event_loop()
    func = partial(
        openai.images.generate,
        model="dall-e-3",
        prompt=prompt,
        size="1024x1024",
        quality="hd",
        n=1,
    )
    with ThreadPoolExecutor() as pool:
        try:
            result = await loop.run_in_executor(
                pool,
                func,
            )
            return result.data[0].url
        except BadRequestError as error:
            if error.code == "billing_hard_limit_reached":
                raise BillingLimitException
            else:
                raise RejectedRequestException
