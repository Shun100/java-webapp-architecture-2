FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

# Gradler Wrapperを使用するために一式コピー
COPY . .

# Gradle Wrapperに実行権限を与える
RUN chmod +x ./gradlew

# ビルド・デプロイ
CMD ["./gradlew", "bootRun"]