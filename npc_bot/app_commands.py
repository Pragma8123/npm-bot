import logging
from discord import Embed, app_commands
from discord.ext import commands
import discord
from npc_bot.ai import generate_image
from npc_bot.constants import BOT_COLOR


class AppCommands(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.logger = logging.getLogger("discord")

    @app_commands.command(
        name="image",
        description="Generate an AI image using DALL-E 3",
    )
    @app_commands.describe(prompt="Prompt to generate image with")
    async def image(self, interaction: discord.Interaction, prompt: str):
        # Defer our response while waiting on our image to generate
        await interaction.response.defer(thinking=True)

        # Call OpenAI to generate the image
        image_url: str | None = None
        description: str | None = None
        try:
            image_url = await generate_image(prompt)
        except:
            self.logger.warn(f"Prompt was rejected. Prompt: {prompt}")
            image_url = await generate_image(
                "A comical test card; technical difficulties"
            )
            description = "Your prompt was rejected 🤔"

        title = f"`{prompt}`" if len(prompt) < 256 else f"`{prompt[0:250]}...`"
        embed = Embed(color=BOT_COLOR, title=title, description=description)
        embed.set_image(url=image_url)

        # Edit our reply with a custom embed
        await interaction.edit_original_response(embed=embed)
