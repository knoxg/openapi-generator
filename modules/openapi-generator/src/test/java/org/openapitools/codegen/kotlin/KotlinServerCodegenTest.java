package org.openapitools.codegen.kotlin;

import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.languages.KotlinServerCodegen;
import org.openapitools.codegen.languages.KotlinSpringServerCodegen;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.openapitools.codegen.CodegenConstants.LIBRARY;
import static org.openapitools.codegen.TestUtils.assertFileContains;
import static org.openapitools.codegen.TestUtils.assertFileNotContains;
import static org.openapitools.codegen.languages.AbstractKotlinCodegen.USE_JAKARTA_EE;
import static org.openapitools.codegen.languages.KotlinServerCodegen.Constants.INTERFACE_ONLY;
import static org.openapitools.codegen.languages.KotlinServerCodegen.Constants.JAXRS_SPEC;
import static org.openapitools.codegen.languages.features.BeanValidationFeatures.USE_BEANVALIDATION;

public class KotlinServerCodegenTest {


    @Test
    public void enumDescription() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinServerCodegen codegen = new KotlinServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(LIBRARY, JAXRS_SPEC);

        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_0/enum-description.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools/server";
        Path petApi = Paths.get(outputPath + "/models/Type.kt");
        assertFileNotContains(
                petApi,
                "import jakarta.ws.rs.*",
                "import jakarta.ws.rs.core.Response",
                "@jakarta.annotation.Generated(value = arrayOf(\"org.openapitools.codegen.languages.KotlinServerCodegen\")"
        );
        // assert, that all enum values have a description comment
        assertFileContains(
                petApi,
                "Pegasi b is a gas giant exoplanet that orbits a G-type star",
                "Mercury is the first planet from the Sun and the smallest in the Solar System",
                "The planet we all live on"
        );
    }

    @Test
    public void javaxImports() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinServerCodegen codegen = new KotlinServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(LIBRARY, JAXRS_SPEC);

        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_0/petstore.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools/server";
        Path petApi = Paths.get(outputPath + "/apis/PetApi.kt");
        assertFileNotContains(
                petApi,
                "import jakarta.ws.rs.*",
                "import jakarta.ws.rs.core.Response",
                "@jakarta.annotation.Generated(value = arrayOf(\"org.openapitools.codegen.languages.KotlinServerCodegen\")"
        );
        assertFileContains(
                petApi,
                "import javax.ws.rs.*",
                "import javax.ws.rs.core.Response",
                "@javax.annotation.Generated(value = arrayOf(\"org.openapitools.codegen.languages.KotlinServerCodegen\")"
        );
    }

    @Test
    public void jakartaEeImports() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinServerCodegen codegen = new KotlinServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(USE_JAKARTA_EE, true);
        codegen.additionalProperties().put(LIBRARY, JAXRS_SPEC);

        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_0/petstore.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools/server";
        Path petApi = Paths.get(outputPath + "/apis/PetApi.kt");
        assertFileContains(
                petApi,
                "import jakarta.ws.rs.*",
                "import jakarta.ws.rs.core.Response",
                "@jakarta.annotation.Generated(value = arrayOf(\"org.openapitools.codegen.languages.KotlinServerCodegen\")"
        );
        assertFileNotContains(
                petApi,
                "import javax.ws.rs.*",
                "import javax.ws.rs.core.Response",
                "@javax.annotation.Generated(value = arrayOf(\"org.openapitools.codegen.languages.KotlinServerCodegen\")"
        );
    }

    @Test
    public void beanValidationJavaxImports() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinServerCodegen codegen = new KotlinServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(LIBRARY, JAXRS_SPEC);
        codegen.additionalProperties().put(USE_BEANVALIDATION, true);

        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_0/petstore.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools/server";
        Path petApi = Paths.get(outputPath + "/apis/PetApi.kt");
        assertFileNotContains(
                petApi,
                "import jakarta.validation.Valid",
                "import jakarta.validation.Valid"
        );
        assertFileContains(
                petApi,
                "import javax.validation.constraints.*",
                "import javax.validation.Valid"
        );

        Path petModel = Paths.get(outputPath + "/models/Pet.kt");
        assertFileNotContains(
                petApi,
                "import jakarta.validation.Valid",
                "import jakarta.validation.Valid"
        );
        assertFileContains(
                petApi,
                "import javax.validation.constraints.*",
                "import javax.validation.Valid"
        );
    }

    @Test
    public void beanValidationJakartaEeImports() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinServerCodegen codegen = new KotlinServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.additionalProperties().put(USE_JAKARTA_EE, true);
        codegen.additionalProperties().put(LIBRARY, JAXRS_SPEC);
        codegen.additionalProperties().put(USE_BEANVALIDATION, true);

        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_0/petstore.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools/server";
        Path petApi = Paths.get(outputPath + "/apis/PetApi.kt");
        assertFileContains(
                petApi,
                "import jakarta.validation.Valid",
                "import jakarta.validation.Valid"
        );
        assertFileNotContains(
                petApi,
                "import javax.validation.constraints.*",
                "import javax.validation.Valid"
        );

        Path petModel = Paths.get(outputPath + "/models/Pet.kt");
        assertFileContains(
                petModel,
                "import jakarta.validation.Valid",
                "import jakarta.validation.Valid"
        );
        assertFileNotContains(
                petModel,
                "import javax.validation.constraints.*",
                "import javax.validation.Valid"
        );
    }

    @Test
    public void issue18177Arrays() throws IOException {
        File output = Files.createTempDirectory("test").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinServerCodegen codegen = new KotlinServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());
        codegen.setUseBeanValidation(true);
        codegen.additionalProperties().put(INTERFACE_ONLY, true);
        codegen.additionalProperties().put(USE_JAKARTA_EE, true);
        codegen.additionalProperties().put(LIBRARY, JAXRS_SPEC);
        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_0/kotlin/issue18177-array.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools/server";
        Path stuffApi = Paths.get(outputPath + "/apis/StuffApi.kt");
        assertFileContains(
                stuffApi,
                "fun findStuff(): kotlin.collections.List<Stuff>"
        );
        assertFileNotContains(
                stuffApi,
                "fun findStuff(): Stuff"
        );
        assertFileContains(
                stuffApi,
                "fun findUniqueStuff(): kotlin.collections.Set<Stuff>"
        );
    }

    // to test attributes in the $ref (OpenAPI 3.1 spec)
    @Test
    public void attributesInRef() throws IOException {
        File output = Files.createTempDirectory("test_attributes").toFile().getCanonicalFile();
        output.deleteOnExit();

        KotlinSpringServerCodegen codegen = new KotlinSpringServerCodegen();
        codegen.setOutputDir(output.getAbsolutePath());

        new DefaultGenerator().opts(new ClientOptInput()
                        .openAPI(TestUtils.parseSpec("src/test/resources/3_1/issue_17726.yaml"))
                        .config(codegen))
                .generate();

        String outputPath = output.getAbsolutePath() + "/src/main/kotlin/org/openapitools";
        Path order = Paths.get(outputPath + "/model/Order.kt");
        assertFileContains(
                order,
                "@get:Size(max=50)"
        );
    }
}
