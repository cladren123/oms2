#!/bin/bash

# Nginx 설정 파일 경로
NGINX_CONFIG_PATH="$(pwd)/nginx.conf"

# 컨테이너 이름 정의
BLUE_CONTAINER="oms2-blue"
GREEN_CONTAINER="oms2-green"
NEW_CONTAINER=""
OLD_CONTAINER=""
NEW_PORT=""

# 활성화된 서버 확인
if docker ps --format '{{.Names}}' | grep -q "$BLUE_CONTAINER"; then
    OLD_CONTAINER="$BLUE_CONTAINER"
    NEW_CONTAINER="$GREEN_CONTAINER"
    NEW_PORT=8082
elif docker ps --format '{{.Names}}' | grep -q "$GREEN_CONTAINER"; then
    OLD_CONTAINER="$GREEN_CONTAINER"
    NEW_CONTAINER="$BLUE_CONTAINER"
    NEW_PORT=8081
else
		NEW_CONTAINER="$BLUE_CONTAINER"
    NEW_PORT=8081
fi

# 새 서버 띄우기
echo "Starting new server: $NEW_CONTAINER on port $NEW_PORT"
docker run -d --name $NEW_CONTAINER --network oms2-network -p $NEW_PORT:8080 -e SPRING_PROFILES_ACTIVE=docker oms2

# 새 서버 상태 확인
echo "Checking the status of the new server..."
sleep 3  # 필요한 경우 서버 초기화 시간을 기다림
if ! docker ps --format '{{.Names}}' | grep -q "$NEW_CONTAINER"; then
    echo "Error: New server $NEW_CONTAINER failed to start."
    exit 1
fi
echo "$NEW_CONTAINER is up and running on port $NEW_PORT."

# Nginx 설정 업데이트
echo "Updating Nginx configuration to enable $NEW_CONTAINER and disable $OLD_CONTAINER."
sed -i "s/^\s*server $OLD_CONTAINER/# server $OLD_CONTAINER/" $NGINX_CONFIG_PATH
sed -i "s/^\s*# server $NEW_CONTAINER/server $NEW_CONTAINER/" $NGINX_CONFIG_PATH

# Nginx 설정 재로드
docker restart oms2-nginx
echo "Nginx configuration reloaded. Traffic is now routed to $NEW_CONTAINER."

# 기존 서버 있을 경우에만 내리기
if [ -n "$OLD_CONTAINER" ]; then
	echo "Stopping old server: $OLD_CONTAINER"
	docker stop $OLD_CONTAINER
	docker rm $OLD_CONTAINER
	echo "$OLD_CONTAINER has been stopped and removed."
fi

echo "Blue-Green deployment completed successfully."