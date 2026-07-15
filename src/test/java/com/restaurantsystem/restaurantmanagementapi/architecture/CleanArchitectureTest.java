package com.restaurantsystem.restaurantmanagementapi.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.restaurantsystem.restaurantmanagementapi",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class CleanArchitectureTest {

    private static final String ROOT = "com.restaurantsystem.restaurantmanagementapi";

    @ArchTest
    static final ArchRule domainMustRemainIndependent = noClasses()
            .that().resideInAPackage(ROOT + ".domain..")
            .should().dependOnClassesThat()
            .resideOutsideOfPackages("java..", ROOT + ".domain..");

    @ArchTest
    static final ArchRule applicationMustOnlyDependInward = noClasses()
            .that().resideInAPackage(ROOT + ".application..")
            .should().dependOnClassesThat()
            .resideOutsideOfPackages("java..", ROOT + ".application..", ROOT + ".domain..");

    @ArchTest
    static final ArchRule presentationMustNotDependOnInfrastructure = noClasses()
            .that().resideInAPackage(ROOT + ".presentation..")
            .should().dependOnClassesThat()
            .resideInAPackage(ROOT + ".infrastructure..");

    @ArchTest
    static final ArchRule innerLayersMustNotDependOnPresentation = noClasses()
            .that().resideInAnyPackage(ROOT + ".domain..", ROOT + ".application..")
            .should().dependOnClassesThat()
            .resideInAPackage(ROOT + ".presentation..");
}
