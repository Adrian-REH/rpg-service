from kafka import KafkaProducer
import json, random, time
from datetime import datetime

# Inicializa el productor de Kafka
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Zonas de juego posibles
zones = ["forest_north", "forest_south", "village", "dungeon_entrance", "castle_courtyard"]

# Acciones posibles
actions = [
    {"type": "attack", "targetId": "mob-42"},
    {"type": "cast_spell", "targetId": "mob-17"},
    {"type": "move", "targetId": None},
    {"type": "heal", "targetId": "player-005"},
    {"type": "defend", "targetId": None}
]

# Estados posibles
statuses = ["normal", "poisoned", "stunned", "buffed"]

while True:
    player_id = f"player-036"
    #player_id = f"player-{random.randint(1, 50):03}"
    now_ms = int(datetime.now().timestamp() * 1000)

    msg = {
        "type": "player_update",
        "entityId": player_id,
        "entityType": "player",
        "timestamp": now_ms,
        "position": {
            "x": round(random.uniform(-100, 100), 2),
            "y": round(random.uniform(0, 10), 2),
            "z": round(random.uniform(-100, 100), 2)
        },
        "rotation": {
            "x": round(random.uniform(0, 360), 1),
            "y": round(random.uniform(0, 360), 1),
            "z": round(random.uniform(0, 360), 1)
        },
        "velocity": {
            "x": round(random.uniform(-1, 1), 2),
            "y": 0,
            "z": round(random.uniform(-1, 1), 2)
        },
        "attributes": {
            "hp": random.randint(0, 100),
            "mp": random.randint(0, 50),
            "zone": random.choice(zones),
            "status": random.sample(statuses, k=random.randint(0, 2))
        },
        "action": {
            **random.choice(actions),
            "damage": random.randint(0, 20)
        }
    }

    # Envía el mensaje al tópico
    producer.send("player.state.data", msg)
    print("Sent:", msg)

    # Espera 2 segundos entre envíos
    time.sleep(2)
