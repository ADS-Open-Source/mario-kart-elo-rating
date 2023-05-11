import json
from itertools import chain

TABLE_PLAYERS = '"PUBLIC"."PLAYERS"'
TABLE_GAMES = '"PUBLIC"."GAMES"'
TABLE_INTERMEDIARY = '"PUBLIC"."GAMES_PLAYERS"'

with open("mkeloData.json", 'r', encoding='utf-8') as f:
    data = json.load(f)
    with open("mkeloData.sql", 'w', encoding='utf-8') as nf:
        for player in data['players']:
            nf.write(
                f"INSERT INTO {TABLE_PLAYERS} VALUES(UUID '{player['uuid']}', UUID '{player['secret']}', '{player['name']}', '{player['email']}', {player['activated']}, {player['elo']}, {player['gamesPlayed']}, ")
            if player['lastEmailRequest']:
                nf.write(f"TIMESTAMP '{player['lastEmailRequest']}');\n")
            else:
                nf.write(f"DEFAULT );\n")
        for i, game in enumerate(data['games'], start=1):
            nf.write(
                f"INSERT INTO {TABLE_GAMES} VALUES({i}, UUID '{game['reportedBy']['uuid']}', TIMESTAMP '{game['reportedTime']}');\n")
            for ranking_player in chain.from_iterable(game['ranking']):
                nf.write(
                    f"INSERT INTO {TABLE_INTERMEDIARY} VALUES ({i}, UUID '{ranking_player['uuid']}', {ranking_player['place']}, {ranking_player['preElo']}, {ranking_player['elo']});\n")
