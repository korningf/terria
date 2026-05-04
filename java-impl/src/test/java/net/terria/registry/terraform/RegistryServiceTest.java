package net.terria.registry.terraform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class RegistryServiceTest {

    private static final Path TEST_STORAGE_PATH = Path.of(System.getProperty("java.io.tmpdir"), "test-registry-" + UUID.randomUUID());

    @Autowired
    private RegistryService registryService;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("registry.storage.path", () -> TEST_STORAGE_PATH.toString());
    }

    @Test
    public void testInitWithEmptyStorage() throws IOException {
        // Ensure path does not exist yet
        if (Files.exists(TEST_STORAGE_PATH)) {
            Files.walk(TEST_STORAGE_PATH)
                 .sorted((a, b) -> b.compareTo(a))
                 .forEach(path -> path.toFile().delete());
        }

        // Re-init service after cleanup
        registryService.init();

        assertThat(registryService.getProviders()).isEmpty();
        assertThat(registryService.getModules()).isEmpty();
    }

    @Test
    public void testInitWithProvider() throws IOException {
        // Create provider structure under registry.terraform.io/providers/
        Path registryPath = TEST_STORAGE_PATH.resolve("registry.terraform.io");
        Path providersPath = registryPath.resolve("providers");
        Path namespacePath = providersPath.resolve("hashicorp");
        Path providerPath = namespacePath.resolve("aws");
        Path versionPath = providerPath.resolve("1.0.0");
        Path platformPath = versionPath.resolve("linux").resolve("amd64");

        Files.createDirectories(platformPath);
        Files.createFile(providersPath.resolve("namespaces.json"));

        // Re-init service after test data creation
        registryService.init();

        assertThat(registryService.getProviders()).hasSize(1);
        assertThat(registryService.getProvider("registry.terraform.io", "hashicorp", "aws")).isPresent();
    }

    // Add more tests as needed
}