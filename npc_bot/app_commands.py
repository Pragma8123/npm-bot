from discord import app_commands
import discord
from discord.ext import commands

from npc_bot.ai import generate_image

class AppCommands(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @app_commands.command(
        name="image",
        description="Generate an AI image using DALL-E 3",
    )
    @app_commands.describe(prompt="Prompt to generate image with")
    async def image(self, interaction: discord.Interaction, prompt: str):
        await interaction.response.defer(ephemeral=True, thinking=True)
        image_response = await generate_image(prompt)
        await interaction.edit_original_response(content=image_response)
