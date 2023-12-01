from discord import Embed, app_commands
import discord
from discord.ext import commands

from npc_bot.ai import generate_image
from npc_bot.constants import BOT_COLOR


class AppCommands(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @app_commands.command(
        name="image",
        description="Generate an AI image using DALL-E 3",
    )
    @app_commands.describe(prompt="Prompt to generate image with")
    async def image(self, interaction: discord.Interaction, prompt: str):
        # Defer our response while waiting on our image to generate
        await interaction.response.defer(thinking=True)

        # Call OpenAI to generate the image
        image_url = await generate_image(prompt)

        embed = Embed(color=BOT_COLOR, title=f"`{prompt}`")
        embed.set_image(url=image_url)

        await interaction.edit_original_response(embed=embed)
