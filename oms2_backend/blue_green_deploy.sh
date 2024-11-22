#!/bin/bash

# Nginx 설정 파일 경로
NGINX_CONTAINER="oms2-nginx"
NGINX_CONFIG_PATH="$(pwd)/nginx.conf"

# 컨테이너 이름 정의
BLUE_CONTAINERS=("oms2-blue1" "oms2-blue2")
GREEN_CONTAINERS=("oms2-green1" "oms2-green2")
NEW_CONTAINERS=()
OLD_CONTAINERS=()
NEW_PORTS=()

# 활성화된 서버 확인
if docker ps --format '{{.Names}}' | grep -q "${BLUE_CONTAINERS[0]}"; then
    OLD_CONTAINERS=("${BLUE_CONTAINERS[@]}")
    NEW_CONTAINERS=("${GREEN_CONTAINERS[@]}")
    NEW_PORTS=(8083 8084)
elif docker ps --format '{{.Names}}' | grep -q "${GREEN_CONTAINERS[0]}"; then
    OLD_CONTAINERS=("${GREEN_CONTAINERS[@]}")
    NEW_CONTAINERS=("${BLUE_CONTAINERS[@]}")
    NEW_PORTS=(8081 8082)
else
		NEW_CONTAINERS=("${BLUE_CONTAINERS[@]}")
    NEW_PORTS=(8081 8082)
fi

# 새 서버 띄우기
for i in "${!NEW_CONTAINERS[@]}"; do
    NEW_CONTAINER="${NEW_CONTAINERS[$i]}"
    NEW_PORT="${NEW_PORTS[$i]}"

    echo "Starting new server: $NEW_CONTAINER on port $NEW_PORT"
    docker run -d --name $NEW_CONTAINER --network oms2-network -p $NEW_PORT:8080 -e SPRING_PROFILES_ACTIVE=docker oms2

    # 새 서버 상태 확인
    echo "Checking the status of the new server: $NEW_CONTAINER..."
    sleep 2
    if ! docker ps --format '{{.Names}}' | grep -q "$NEW_CONTAINER"; then
        echo "Error: New server $NEW_CONTAINER failed to start."
        exit 1
    fi
    echo "$NEW_CONTAINER is up and running on port $NEW_PORT."
done

# Nginx 실행 확인 및 시작
if ! docker ps --format '{{.Names}}' | grep -q "$NGINX_CONTAINER"; then
    echo "Nginx container not found. Starting Nginx..."
    docker run --name $NGINX_CONTAINER -d --network oms2-network -p 80:80 -v $NGINX_CONFIG_PATH:/etc/nginx/nginx.conf nginx:latest
    sleep 2  # 초기화 대기
    if ! docker ps --format '{{.Names}}' | grep -q "$NGINX_CONTAINER"; then
        echo "Error: Failed to start Nginx container."
        exit 1
    fi
    echo "Nginx container started successfully."
else
    echo "Nginx container is already running."
fi


# Nginx 설정 업데이트
echo "Updating Nginx configuration to enable new servers and disable old servers."
for OLD_CONTAINER in "${OLD_CONTAINERS[@]}"; do
    ex -sc "g/^\s*server $OLD_CONTAINER/s//\# server $OLD_CONTAINER/" -cx $NGINX_CONFIG_PATH
done

for i in "${!NEW_CONTAINERS[@]}"; do
    NEW_CONTAINER="${NEW_CONTAINERS[$i]}"
    ex -sc "g/^\s*\# server $NEW_CONTAINER/s//server $NEW_CONTAINER/" -cx $NGINX_CONFIG_PATH
done

# Nginx 설정 재로드
# docker restart oms2-nginx
docker exec oms2-nginx nginx -s reload
echo "Nginx configuration reloaded. Traffic is now routed to new servers."

# 기존 서버 내리기
for OLD_CONTAINER in "${OLD_CONTAINERS[@]}"; do
    echo "Stopping old server: $OLD_CONTAINER"
    docker stop $OLD_CONTAINER
    docker rm $OLD_CONTAINER
    echo "$OLD_CONTAINER has been stopped and removed."
done

echo "Blue-Green deployment completed successfully."