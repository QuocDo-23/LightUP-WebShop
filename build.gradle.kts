plugins {
    java
    war
    id("org.hidetake.ssh") version "2.11.2"
}

group = "code.web.lightup"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

dependencies {
    // Servlet API
    providedCompile("jakarta.servlet:jakarta.servlet-api:6.1.0")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.2")

    // Database
    implementation("org.jdbi:jdbi3-core:3.45.1")
    implementation("com.mysql:mysql-connector-j:8.4.0")

    // Spring Security
    implementation("org.springframework.security:spring-security-core:6.5.4")

    // JSP / JSTL
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")

    // Google API / OAuth2
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-oauth2:v2-rev20200213-2.0.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Jakarta Mail
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("com.sun.activation:jakarta.activation:2.0.1")
}

tasks.test {
    useJUnitPlatform()
}
//
//// ==================== DEPLOY CONFIG ====================
//
//val remotes = mapOf(
//    "host" to mapOf(
//        "host" to "192.168.1.40",   // ← Thay IP VPS của bạn
//        "user" to "root",
//        "password" to "..."          // ← Thay mật khẩu root
//    )
//)
//
//// Dùng SSH plugin theo cú pháp Groovy trong file .kts không tiện
//// Khuyến nghị: dùng Cách 1 (Groovy DSL) để dùng được ssh plugin dễ hơn