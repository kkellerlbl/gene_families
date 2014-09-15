KB_TOP ?= /kb/dev_container
KB_RUNTIME ?= /kb/runtime
DEPLOY_RUNTIME ?= $(KB_RUNTIME)
TARGET ?= /kb/deployment
CURR_DIR = $(shell pwd)
SERVICE_NAME = KBaseGeneFamilies
SERVICE = $(shell basename $(CURR_DIR))
SERVICE_DIR = $(TARGET)/services/$(SERVICE)
LIB_JARS_DIR = $(KB_TOP)/modules/jars/lib/jars
SERVICE_PORT = 7123
THREADPOOL_SIZE = 20
MEMORY = 1000
MAX_MEMORY = 10000
GLASSFISH_HOME ?= $(DEPLOY_RUNTIME)/glassfish3
ASADMIN = $(GLASSFISH_HOME)/glassfish/bin/asadmin
ANT = ant

default: compile

deploy-all: deploy

deploy: deploy-client deploy-service deploy-scripts deploy-docs

test: test-client test-service test-scripts

test-client:
	@echo "No tests for client"

test-service:
	@echo "No tests for service yet"

test-scripts:
	@echo "No tests for scripts"

compile: src
	$(ANT) war

deploy-client:
	@echo "No deployment for client"

deploy-service:
	@echo "Service folder: $(SERVICE_DIR)"
	mkdir -p $(SERVICE_DIR)
	cp -f ./dist/KBaseGeneFamilies.war $(SERVICE_DIR)
	cp -f ./service/glassfish_administer_service.py $(SERVICE_DIR)
	cp -f ./deploy.cfg $(SERVICE_DIR)
	echo '#!/bin/sh' > $(SERVICE_DIR)/start_service
	echo "export KB_DEPLOYMENT_CONFIG=$(SERVICE_DIR)/deploy.cfg" >> $(SERVICE_DIR)/start_service
	echo "$(SERVICE_DIR)/glassfish_administer_service.py --admin $(ASADMIN)\
	 --domain $(SERVICE_NAME) --domain-dir $(SERVICE_DIR)/glassfish_domain\
	 --war $(SERVICE_DIR)/KBaseGeneFamilies.war --port $(SERVICE_PORT)\
	 --threads $(THREADPOOL_SIZE) --Xms $(MEMORY) --Xmx $(MAX_MEMORY)\
	 --noparallelgc --properties KB_DEPLOYMENT_CONFIG=\$$KB_DEPLOYMENT_CONFIG"\
	 >> $(SERVICE_DIR)/start_service
	chmod +x $(SERVICE_DIR)/start_service
	echo '#!/bin/sh' > $(SERVICE_DIR)/stop_service
	echo "$(SERVICE_DIR)/glassfish_administer_service.py --admin $(ASADMIN)\
	 --domain $(SERVICE_NAME) --domain-dir $(SERVICE_DIR)/glassfish_domain\
	 --port $(SERVICE_PORT)" >> $(SERVICE_DIR)/stop_service
	chmod +x $(SERVICE_DIR)/stop_service
	echo '#!/bin/sh' > $(SERVICE_DIR)/stop_domain
	echo "$(ASADMIN) stop-domain --domaindir $(SERVICE_DIR)/glassfish_domain $(SERVICE_NAME)"\
	 >> $(SERVICE_DIR)/stop_domain
	chmod +x $(SERVICE_DIR)/stop_domain

deploy-scripts:
	@echo "No deployment for scripts"

deploy-docs:
	$(ANT) docs
	rsync -a ./docs $(SERVICE_DIR)

clean:
	rm -rf ./classes
	rm -rf ./docs
