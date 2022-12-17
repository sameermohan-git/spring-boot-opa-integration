# spring-boot-opa-integration
This project covers integration needed between sample spring boot application and OPA (Open Policy Agent)

#Project Dependencies
- OpenJDK 17
- Spring Boot 3.0

#OPA Build Steps
1. Pull latest OPA image in docker desktop
2. Run container
	docker run -p 8181:8181 openpolicyagent/opa run --server --log-level debug
	
3. Insert sample policy using curl or through postman:
	curl --location --request PUT 'localhost:8181/v1/policies/opaweb/authz' \
  	--header 'Content-Type: text/plain' \
  	--data-binary @opaweb-policy.rego 

#Docker Image Build - Web App
1. Change directory where you have Dockerfile
2. Build Image
	docker build -t spring-boot-opa-integration .
3. Run the container
	docker run -p 8080:8080 spring-boot-opa-integration
4. Access app endpoint through Postman with Basic auth, providing username and password
	http://localhost:8080/hello
