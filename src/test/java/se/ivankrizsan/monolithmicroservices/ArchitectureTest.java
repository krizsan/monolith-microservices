package se.ivankrizsan.monolithmicroservices;


import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SliceAssignment;
import com.tngtech.archunit.library.dependencies.SliceIdentifier;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

public class ArchitectureTest {

    @Test
    public void architectureOneTest() {
        final JavaClasses theClassesToCheck = new ClassFileImporter()
            .importPackages("se.ivankrizsan");

        final SliceAssignment theSliceAssignment = new SliceAssignment() {
            @Override
            public SliceIdentifier getIdentifierOf(JavaClass javaClass) {
                if (javaClass.getPackageName().contains(".modules.")) {
                    final String theModuleSubPackage = javaClass
                        .getPackageName()
                        .replace("se.ivankrizsan.monolithmicroservices.modules.", "");
                    final String theModule = theModuleSubPackage.split("\\.")[0];

                    /*
                     * The subpackages api and configuration of a module may be accessed from anywhere,
                     * and so they are not to belong to any slice.
                     */
                    if (theModuleSubPackage.contains("api") || theModuleSubPackage.contains("configuration")) {
                        return SliceIdentifier.ignore();
                    }

                    return SliceIdentifier.of("Module-" + theModule);
                }

                return SliceIdentifier.ignore();
            }

            @Override
            public String getDescription() {
                return "My package structure";
            }
        };

        SlicesRuleDefinition.slices()
            .assignedFrom(theSliceAssignment)
            .should().notDependOnEachOther()
            .check(theClassesToCheck);
    }
}
