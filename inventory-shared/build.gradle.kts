plugins { `java-library` }

dependencies {
  api("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
  implementation("jakarta.validation:jakarta.validation-api:3.1.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
  compileOnly("org.projectlombok:lombok:1.18.30")
  annotationProcessor("org.projectlombok:lombok:1.18.30")
  testCompileOnly("org.projectlombok:lombok:1.18.30")
  implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.3.0")
}
