
# 첫 번째 인자 확인
ACTION=$1

usage() {
  echo "Usage : $0 {help|}"
  exit 1
}


start_mysql() {
  echo "start mysql : Source 1 Slave 1"
  cd ./mysql_setup
  bash ./mysql_setup.sh up
  cd - # 원래 디렉토리로 돌아감
}

end_mysql() {
  echo "end mysql : Source 1 Slave 1"
  cd ./mysql_setup
  bash ./mysql_setup.sh down
  cd -
}

# 입력값이 없는 경우 사용법 안내
if [ -z "$1" ]; then
  usage
fi

# 입력값에 따른 작업 수행
case "$1" in
  help)
    usage
    ;;
  start_db)
    start_mysql
    ;;
  *)
    echo "Invalid Argument: $1"
    usage
    ;;
esac