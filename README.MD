$ docker run application-project-test -p 8080:8080
--name: 컨테이너 이름 지정
-p: 컨테이너는 기본적으로 외부와 격리되어 있기 때문에 호스트와 컨테이너 port 포워딩 처리
$ docker ps
$ docker ps -a
$ docker exec -it [컨테이너 이름 또는 id] /bin/bash // 컨테이너를 sh, bash 등의 터미널 환경으로 접근
$ docker stop [컨테이너 이름 또는 id]
$ docker inspect [컨테이너 이름 또는 id]

도커 컴포즈 실행
docker-compose -f docker-compose-local.yml up -d
