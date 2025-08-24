plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  java
}

dependencies {
  implementation(project(":inventory-shared"))
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-amqp")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  runtimeOnly("org.postgresql:postgresql:42.7.3")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  compileOnly("org.projectlombok:lombok:1.18.30")
  annotationProcessor("org.projectlombok:lombok:1.18.30")
  testCompileOnly("org.projectlombok:lombok:1.18.30")
  testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
  implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.3.0")
}
