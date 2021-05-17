
docker-build:
	@docker build -t word-counter .

docker-run:
	@docker run  -it --rm --name wc-container --mount type=bind,source=$(data),target=/test-data,readonly word-counter
