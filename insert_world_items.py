import json
import oracledb
from datetime import datetime

# === CONFIGURACIÓN DE CONEXIÓN ===
DB_USERNAME = "SYSTEM"
DB_PASSWORD = "MiPasswordSegura123"
DB_DSN = "localhost:1521/XE"

# === FUNCIONES AUXILIARES ===
def parse_item(item):
    return {
        "WORLD_ITEM_ID": item.get("worldItemId"),
        "ITEM_TEMPLATE_ID": item.get("itemTemplateId"),
        "ZONE_ID": item.get("zoneId"),
        "POS_X": item.get("posX"),
        "POS_Y": item.get("posY"),
        "POS_Z": item.get("posZ"),
        "QUANTITY": item.get("quantity"),
        "SPAWNED_AT": parse_timestamp(item.get("spawnedAt")),
        "DESPAWN_AT": parse_timestamp(item.get("despawnAt")),
        "IS_AVAILABLE": "Y" if item.get("isAvailable") else "N",
        "DROPPED_BY": item.get("droppedBy"),
    }

def parse_timestamp(ts):
    if ts is None:
        return None
    try:
        return datetime.fromisoformat(ts.replace("Z", "+00:00"))
    except Exception:
        return None

def ensure_zone(cursor, zone_id):
    cursor.execute("SELECT 1 FROM ZONES WHERE ZONE_ID = :zid", zid=zone_id)
    if cursor.fetchone() is None:
        # Insertar valor básico
        cursor.execute("""
            INSERT INTO ZONES (ZONE_ID, ZONE_NAME, MIN_X, MAX_X, MIN_Z, MAX_Z, MIN_LEVEL, MAX_LEVEL, IS_SAFE_ZONE, CREATED_AT)
            VALUES (:ZONE_ID, :ZONE_NAME, 0, 100, 0, 100, 1, 100, 'Y', SYSTIMESTAMP)
        """, {"ZONE_ID": zone_id, "ZONE_NAME": zone_id})
        print(f"🟢 ZONE creado: {zone_id}")

def ensure_item_template(cursor, item_template_id):
    cursor.execute("SELECT 1 FROM ITEM_TEMPLATES WHERE ITEM_TEMPLATE_ID = :iid", iid=item_template_id)
    if cursor.fetchone() is None:
        cursor.execute("""
            INSERT INTO ITEM_TEMPLATES (ITEM_TEMPLATE_ID, ITEM_NAME, ITEM_TYPE, RARITY, LEVEL_REQUIRED, STACK_SIZE, CREATED_AT)
            VALUES (:ITEM_TEMPLATE_ID, :ITEM_NAME, 'Generic', 'Common', 1, 99, SYSTIMESTAMP)
        """, {"ITEM_TEMPLATE_ID": item_template_id, "ITEM_NAME": item_template_id})
        print(f"🟢 ITEM_TEMPLATE creado: {item_template_id}")

# === FUNCIÓN PRINCIPAL ===
def insert_world_items_from_json(json_path):
    with open(json_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    if isinstance(data, dict):
        data = [data]

    connection = oracledb.connect(
        user=DB_USERNAME,
        password=DB_PASSWORD,
        dsn=DB_DSN
    )
    cursor = connection.cursor()

    insert_sql = """
        INSERT INTO WORLD_ITEMS (
            WORLD_ITEM_ID, ITEM_TEMPLATE_ID, ZONE_ID,
            POS_X, POS_Y, POS_Z, QUANTITY,
            SPAWNED_AT, DESPAWN_AT, IS_AVAILABLE, DROPPED_BY
        ) VALUES (
            :WORLD_ITEM_ID, :ITEM_TEMPLATE_ID, :ZONE_ID,
            :POS_X, :POS_Y, :POS_Z, :QUANTITY,
            :SPAWNED_AT, :DESPAWN_AT, :IS_AVAILABLE, :DROPPED_BY
        )
    """

    for item in data:
        parsed = parse_item(item)

        # ✅ Aseguramos que los padres existan
        ensure_zone(cursor, parsed["ZONE_ID"])
        ensure_item_template(cursor, parsed["ITEM_TEMPLATE_ID"])

        try:
            cursor.execute(insert_sql, parsed)
            print(f"✅ WORLD_ITEM insertado: {parsed['WORLD_ITEM_ID']}")
        except oracledb.IntegrityError as e:
            print(f"⚠️ Error (duplicado o FK): {parsed['WORLD_ITEM_ID']} → {e}")
        except Exception as e:
            print(f"❌ Error insertando {parsed['WORLD_ITEM_ID']}: {e}")

    connection.commit()
    cursor.close()
    connection.close()
    print("✅ Todos los datos insertados correctamente.")

# === EJECUCIÓN ===
if __name__ == "__main__":
    insert_world_items_from_json("world_items.json")
