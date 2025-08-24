plugins {
  id("io.spring.dependency-management") version "1.1.6" apply false
  id("org.springframework.boot") version "3.3.2" apply false
  java
}

allprojects {
  group = "com.acme"
  version = "0.1.0"
  repositories { mavenCentral() }
}

subprojects {
  apply(plugin = "java")
  java { toolchain { languageVersion.set(JavaLanguageVersion.of(findProperty("javaVersion").toString())) } }
  tasks.test { useJUnitPlatform() }
}
