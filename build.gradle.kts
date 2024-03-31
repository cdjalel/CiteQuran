plugins {
    id("java")
    id("com.palantir.git-version") version "3.0.0"
  }

group = "dz.djalel.LO"

val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.libreoffice:officebean:7.4.1")
    implementation("org.libreoffice:libreoffice:7.4.1")
    testImplementation("org.testng:testng:7.6.1")
}

tasks.withType<Jar> {
    manifest {
        attributes["RegistrationClassName"] = "dz.djalel.LO.comp.RegistrationHandler"
        attributes["Implementation-Title"] = "CiteQuran"
        attributes["Implementation-Version"] = project.version
    }
    from(sourceSets["main"].allSource) {
        exclude("**/*.java")
        exclude("**/*.properties")
    }
}

tasks.register<Copy>("copyRegistrationFilesForDeploy") {
    from(layout.projectDirectory.dir("registry"))
    into(layout.buildDirectory.dir("toArchive/registry"))

}

tasks.register<Copy>("copyDialogFilesForDeploy") {
    from(layout.projectDirectory.dir("dialog"))
    into(layout.buildDirectory.dir("toArchive/dialog"))
}

tasks.register<Copy>("copyDescriptionDirForDeploy") {
    from(layout.projectDirectory.dir("description"))
    include("*.txt")
    into(layout.buildDirectory.dir("toArchive/description"))
}

tasks.register<Copy>("copyDescriptionXMLForDeploy") {
    from(layout.projectDirectory.dir("description"))
    include("*.xml")
    expand("version" to project.version)
    into(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Copy>("copyIDLDirForDeploy") {
    from(layout.projectDirectory.dir("idl"))
    into(layout.buildDirectory.dir("toArchive/idl"))
}

tasks.register<Copy>("copyImagesDirForDeploy") {
    from(layout.projectDirectory.dir("images"))
    into(layout.buildDirectory.dir("toArchive/images"))
}

tasks.register<Copy>("copyLicenseFileForDeploy") {
    from(layout.projectDirectory.dir("COPYING"))
    into(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Copy>("copyManifestDirForDeploy") {
    from(layout.projectDirectory.dir("META-INF"))
    into(layout.buildDirectory.dir("toArchive/META-INF"))
}

tasks.register<Copy>("copyJarFileForDeploy") {
    dependsOn("build")
    from(layout.buildDirectory.dir("libs/${project.name}-${version}.jar"))
    into(layout.buildDirectory.dir("toArchive"))

    rename("${project.name}-${version}.jar", "${project.name}.jar")
}

tasks.register<Copy>("copyQuranDirForDeploy") {
    from(layout.projectDirectory.dir("data"))

    into(layout.buildDirectory.dir("toArchive/data"))
}

tasks.register<Exec>("compileIDLfile") {
    commandLine(
        "/usr/lib/libreoffice/sdk/bin/unoidl-write",
        "/usr/lib64/libreoffice/program/types.rdb",
        "/usr/lib64/libreoffice/program/types/offapi.rdb",
        "idl/dz.djalel.LO/CiteQuran/CiteQuran.idl",
        "toArchive/types.rdb"
    )
}

tasks.register<Zip>("prepareDistributionPackage") {
    dependsOn("copyQuranDirForDeploy")
    dependsOn("copyDescriptionDirForDeploy")
    dependsOn("copyDescriptionXMLForDeploy")
    dependsOn("copyDialogFilesForDeploy")
    dependsOn("copyImagesDirForDeploy")
    dependsOn("copyLicenseFileForDeploy")
    dependsOn("copyManifestDirForDeploy")
    dependsOn("copyRegistrationFilesForDeploy")
    dependsOn("copyJarFileForDeploy")
    archiveBaseName.set("${project.name}")
    archiveVersion.set("${project.version}")
    archiveExtension.set("oxt")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
    from(layout.buildDirectory.dir("toArchive"))
}

tasks.register<Exec>("InstallDistributionPackage") {
    dependsOn("prepareDistributionPackage")
    commandLine(
        "/usr/bin/unopkg",
        "add",
        "--force",
        "--suppress-license",
        "build/dist/${project.name}-${version}.oxt"
    )
}

tasks.test {
    useTestNG {
        preserveOrder = true
    }
}
