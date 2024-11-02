import jwt
from datetime import datetime, timezone, timedelta

def load_jwt_secret(file_path=".jwt-secret"):
    with open(file_path, "r") as file:
        return file.read().strip()

def gen_jwt_token(user_id_claim, token_duration=7*24*60*60):
    expiration_time = datetime.now(timezone.utc) + timedelta(seconds=token_duration)
    token = jwt.encode(
        {
            "typ": "JWT",
            "iss": "identity.service",
            "userId": user_id_claim,
            "iat": datetime.now(timezone.utc),
            "exp": expiration_time
        },
        jwt_secret,
        algorithm="HS512"
    )
    return token

jwt_secret = load_jwt_secret()
print("Press Ctrl+C to exit.")
while True:
    user_id_claim = input("Enter user ID: ")
    token = gen_jwt_token(user_id_claim)
    print(token)