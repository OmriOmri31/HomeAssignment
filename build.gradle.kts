plugins { java }

repositories {
    mavenCentral()
    google()
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    implementation("io.appium:java-client:9.2.1")
    implementation("org.seleniumhq.selenium:selenium-support:4.21.0")

    implementation("org.slf4j:slf4j-api:2.0.13")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("PASSED","FAILED","SKIPPED")
    }
}
