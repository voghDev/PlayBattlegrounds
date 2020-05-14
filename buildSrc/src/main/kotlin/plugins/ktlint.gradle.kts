val ktlint by configurations.creating

dependencies {
    ktlint("com.github.shyiko:ktlint:0.31.0")
}

tasks.register<JavaExec>("ktlint") {
    main = "com.github.shyiko.ktlint.Main"
    classpath = ktlint
    args(
        "src/**/*.kt",
        "--reporter=plain",
        "--reporter=checkstyle,output=${buildDir}/reports/ktlint.xml"
    )
}

tasks.named("check") {
    dependsOn(ktlint)
}

tasks.register<JavaExec>("ktlintFormat") {
    main = "com.github.shyiko.ktlint.Main"
    classpath = ktlint
    args("-F", "src/**/*.kt")
}
