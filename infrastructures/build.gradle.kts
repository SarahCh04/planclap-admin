plugins {
    id("helmo-common-conventions")
    `java-library`
}

dependencies {
    implementation(project(":domains"))
    implementation("com.google.code.gson:gson:2.7")
}
