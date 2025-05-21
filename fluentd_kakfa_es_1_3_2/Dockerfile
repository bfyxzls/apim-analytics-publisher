# https://docs.fluentd.org/container-deployment/docker-compose#step-1-create-fluentd-image-with-your-config--plugin
FROM fluent/fluentd:v1.19-debian
USER root
# To connect to docker.elastic.co/elasticsearch/elasticsearch:8.x, it requires elasticsearch v8 gem
# Ref. https://github.com/elastic/elasticsearch-ruby/blob/main/README.md#compatibility
RUN ["gem", "install", "elasticsearch", "--no-document", "--version", "6.8.3"]
RUN ["gem","install", "fluent-plugin-kafka","--no-document","--version","0.18.1"]
RUN ["gem", "install", "fluent-plugin-elasticsearch", "--no-document", "--version", "4.0.3"]
USER fluent