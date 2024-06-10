import logging
import pkg_resources
import base64
from io import BytesIO
from discord import app_commands
from discord.ext import commands
import discord
from npc_bot.ai import generate_image


class AppCommands(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.logger = logging.getLogger("discord")

    @app_commands.command(
        name="image",
        description="Generate an AI image using Stable Diffusion",
    )
    @app_commands.describe(prompt="Prompt to generate image with", negative_prompt="Anti-prompt", count="Batch of images")
    async def image(self, interaction: discord.Interaction, prompt: str, negative_prompt: str = "", count: int = 1):
        # Defer our response while waiting on our image to generate
        await interaction.response.defer(thinking=True)

        # Call Stable Diffusion to generate the image
        images: list[str] | None = None
        try:
            images = await generate_image(prompt, negative_prompt, count)
        except Exception as e:
            self.logger.error(e)
            await interaction.edit_original_response(content="There was an error generating your image 🤔.")
            return

        files = []
        if images != None:
            for i, image in enumerate(images):
                image_data = base64.b64decode(image)
                image_file = BytesIO(image_data)
                file = discord.File(image_file, filename=f"image_{i}.png")
                files.append(file)

        content = (
            f"Prompt: `{prompt}`\n"
            f"Negative Prompt: `{negative_prompt}`\n"
        )

        await interaction.followup.send(content=content, files=files, wait=True)

    @app_commands.command(name="version")
    async def version(self, interaction: discord.Interaction):
        version = pkg_resources.get_distribution("npc_bot").version
        await interaction.response.send_message(content=f"Version: {version}")
