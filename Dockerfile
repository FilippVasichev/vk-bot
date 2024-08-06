FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app

COPY build/libs/vk-vkBot-1.0.jar /app/vk-bot.jar

EXPOSE 8080

CMD ["sh", "-c", "java ${ARGS} -jar vk-bot.jar"]
