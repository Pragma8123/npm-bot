import os
from dotenv import load_dotenv
import aiohttp

load_dotenv()

SD_API_URL = os.environ["SD_API_URL"]


async def generate_image(
    prompt: str, negative_prompt: str, count: int
) -> list[str] | None:
    async with aiohttp.ClientSession() as session:
        json = {
            "prompt": prompt,
            "negative_prompt": negative_prompt,
            "batch_size": count,
        }
        async with session.post(
            f"{SD_API_URL}/sdapi/v1/txt2img", json=json
        ) as response:
            data = await response.json()
            return data["images"]
