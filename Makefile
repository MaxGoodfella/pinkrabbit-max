restart1:
	docker-compose down
	mvn clean package
	docker-compose up -d rabbitmq
	docker-compose up -d prometheus
	docker-compose up -d grafana
	docker-compose up -d producer

restart2:
	docker-compose up -d consumer
