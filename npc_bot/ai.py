import asyncio
import openai
from concurrent.futures import ThreadPoolExecutor
from functools import partial


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
        result = await loop.run_in_executor(
            pool,
            func,
        )
    return result.data[0].url
