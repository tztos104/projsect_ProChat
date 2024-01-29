db-up:
    docker-compose up -d --force-recreate

de-down:
    docker-compose down -v

도커 실행
docker-compose -f docker-compose-local.yml up