import os
from dotenv import load_dotenv
from npc_bot.discord import DiscordBot


def main():
    load_dotenv()
    bot = DiscordBot()
    token = os.getenv("DISCORD_TOKEN")
    if token is None:
        raise ValueError("DISCORD_TOKEN needs to be set!")
    bot.run(token=token)


if __name__ == "__main__":
    main()
