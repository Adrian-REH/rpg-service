from kafka import KafkaProducer, KafkaConsumer
import json, random, time
from datetime import datetime

# Inicializa el productor de Kafka
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Inicializa el consumidor para recibir respuestas
consumer = KafkaConsumer(
    'player.state.response',  # tópico donde esperamos respuestas
    bootstrap_servers=['localhost:9092'],
    value_deserializer=lambda v: json.loads(v.decode('utf-8')),
    auto_offset_reset='earliest',
    enable_auto_commit=True,
    group_id='player_group'
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

player_id = "player-036"

while True:
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
    producer.flush()
    print("Sent:", msg)

    # Espera respuesta
    response_received = False
    wait_start = time.time()
    timeout = 5  # segundos a esperar antes de reintentar

    while not response_received:
        for message in consumer.poll(timeout_ms=1000).values():
            for record in message:
                if record.value.get("entityId") == player_id:
                    print("Received response:", record.value)
                    response_received = True
                    break
            if response_received:
                break

        # Si pasa el tiempo y no recibe nada, espera antes de reintentar
        if not response_received and time.time() - wait_start > timeout:
            print("No response received, retrying after 2 seconds...")
            time.sleep(2)
            break

