plugins {
    id("helmo-common-conventions")
    `java-library`
}

dependencies {
     implementation(project(":domains"))
     
     testImplementation("org.mockito:mockito-core")
}
