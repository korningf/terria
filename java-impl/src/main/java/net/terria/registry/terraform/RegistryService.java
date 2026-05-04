package net.terria.registry.terraform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RegistryService {

    private static final Logger logger = LoggerFactory.getLogger(RegistryService.class);

    /**
     * Root path where on-disk registry storage is mounted.
     * Can be configured through application properties.
     */
    @Value("${registry.storage.path:../registry}")
    private String storagePath;

    /**
     * In-memory index keyed by "registry/namespaceName/name" for providers.
     */
    private final Map<String, ProviderIndex> providers = new ConcurrentHashMap<>();

    /**
     * In-memory index keyed by "registry/namespaceName/name" for modules.
     */
    private final Map<String, ModuleIndex> modules = new ConcurrentHashMap<>();

    /**
     * Initialize the in-memory index by scanning filesystem at startup.
     * This mirrors dynamic .json generation from repo content.
     */
    @PostConstruct
    public void init() throws IOException {
        Path root = Paths.get(storagePath);
        if (!Files.exists(root)) {
            logger.warn("Registry storage path does not exist: {}", root);
            return;
        }

        // Scan the terraform registry structure
        Path terraformRegistry = root.resolve("registry.terraform.io");
        if (Files.exists(terraformRegistry)) {
            scanTerraformRegistry(terraformRegistry);
        }

        logger.info("Registry indexes rebuilt. Providers: {}, Modules: {}", providers.size(), modules.size());
    }

    /**
     * Scan a specific terraform registry directory structure.
     */
    private void scanTerraformRegistry(Path registryPath) throws IOException {
        String registry = "registry.terraform.io";

        // Scan providers under registry.terraform.io/providers/
        Path providersPath = registryPath.resolve("providers");
        if (Files.exists(providersPath)) {
            scanRegistryProviders(providersPath, registry);
        }

        // Scan modules under registry.terraform.io/modules/
        Path modulesPath = registryPath.resolve("modules");
        if (Files.exists(modulesPath)) {
            scanRegistryModules(modulesPath, registry);
        }
    }

    /**
     * Scan provider storage directories and build in-memory provider index.
     */
    private void scanProviders(Path providersPath) throws IOException {
        if (!Files.exists(providersPath)) return;

        // Top-level under providers: registry namespaces like registry.terraform.io
        Files.walk(providersPath, 1)
            .filter(Files::isDirectory)
            .filter(p -> !p.equals(providersPath))
            .forEach(registryPath -> {
                String registry = registryPath.getFileName().toString();
                try {
                    scanRegistryProviders(registryPath, registry);
                } catch (IOException e) {
                    logger.error("Error scanning providers in registry {}", registry, e);
                }
            });
    }

    private void scanRegistryProviders(Path registryPath, String registry) throws IOException {
        Path namespacesPath = registryPath.resolve("namespaces.json");
        if (!Files.exists(namespacesPath)) return; // Skip if no namespaces.json

        Files.list(registryPath)
            .filter(Files::isDirectory)
            .filter(p -> !p.getFileName().toString().equals("namespaces.json"))
            .forEach(namespacePath -> {
                String namespaceName = namespacePath.getFileName().toString();
                try {
                    scanNamespaceProviders(namespacePath, registry, namespaceName);
                } catch (IOException e) {
                    logger.error("Error scanning namespace {} in registry {}", namespaceName, registry, e);
                }
            });
    }

    private void scanNamespaceProviders(Path namespacePath, String registry, String namespaceName) throws IOException {
        Files.list(namespacePath)
            .filter(Files::isDirectory)
            .forEach(providerPath -> {
                String providerName = providerPath.getFileName().toString();
                try {
                    ProviderIndex index = scanProvider(providerPath, registry, namespaceName, providerName);
                    String key = registry + "/" + namespaceName + "/" + providerName;
                    providers.put(key, index);
                } catch (Exception e) {
                    logger.error("Error scanning provider {}/{}/{}", registry, namespaceName, providerName, e);
                }
            });
    }

    private ProviderIndex scanProvider(Path providerPath, String registry, String namespaceName, String name) throws IOException {
        // Keep raw folder names to reflect exactly what exists on disk.
        // Accept symlink/alias version directories as-is by name.
        Map<String, List<Platform>> versions = new LinkedHashMap<>();

        Files.list(providerPath)
            .filter(Files::isDirectory)
            .forEach(versionPath -> {
                String versionStr = versionPath.getFileName().toString();
                try {
                    List<Platform> platforms = scanPlatforms(versionPath);
                    versions.put(versionStr, platforms);
                    logger.debug("Discovered provider {}/{}/{}/{} with {} platform entries", registry, namespaceName, name, versionStr, platforms.size());
                } catch (IOException ex) {
                    logger.warn("Failed to scan provider version {}: {}", versionStr, ex.getMessage());
                }
            });

        return new ProviderIndex(registry, namespaceName, name, versions);
    }

    private List<Platform> scanPlatforms(Path versionPath) throws IOException {
        if (!Files.exists(versionPath)) return Collections.emptyList();

        // Magic platform directory detection in each provider version.
        return Files.list(versionPath)
            .filter(Files::isDirectory)
            .flatMap(osPath -> {
                String os = osPath.getFileName().toString();
                try {
                    return Files.list(osPath)
                        .filter(Files::isDirectory)
                        .map(archPath -> new Platform(os, archPath.getFileName().toString()));
                } catch (IOException ex) {
                    logger.warn("Failed to scan arch directories for os {}: {}", os, ex.getMessage());
                    return java.util.stream.Stream.empty();
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Scan module storage directories and build in-memory module index.
     */
    private void scanModules(Path modulesPath) throws IOException {
        if (!Files.exists(modulesPath)) return;

        Files.walk(modulesPath, 1)
            .filter(Files::isDirectory)
            .filter(p -> !p.equals(modulesPath))
            .forEach(registryPath -> {
                String registry = registryPath.getFileName().toString();
                try {
                    scanRegistryModules(registryPath, registry);
                } catch (IOException e) {
                    logger.error("Error scanning modules in registry {}", registry, e);
                }
            });
    }

    private void scanRegistryModules(Path registryPath, String registry) throws IOException {
        Path namespacesPath = registryPath.resolve("namespaces.json");
        if (!Files.exists(namespacesPath)) return;

        Files.list(registryPath)
            .filter(Files::isDirectory)
            .filter(p -> !p.getFileName().toString().equals("namespaces.json"))
            .forEach(namespacePath -> {
                String namespaceName = namespacePath.getFileName().toString();
                try {
                    scanNamespaceModules(namespacePath, registry, namespaceName);
                } catch (IOException e) {
                    logger.error("Error scanning module namespace {} in registry {}", namespaceName, registry, e);
                }
            });
    }

    private void scanNamespaceModules(Path namespacePath, String registry, String namespaceName) throws IOException {
        Files.list(namespacePath)
            .filter(Files::isDirectory)
            .forEach(modulePath -> {
                String moduleName = modulePath.getFileName().toString();
                try {
                    ModuleIndex moduleIndex = scanModule(modulePath, registry, namespaceName, moduleName);
                    String key = registry + "/" + namespaceName + "/" + moduleName;
                    modules.put(key, moduleIndex);
                } catch (Exception e) {
                    logger.error("Error scanning module {}/{}/{}", registry, namespaceName, moduleName, e);
                }
            });
    }

    private ModuleIndex scanModule(Path modulePath, String registry, String namespaceName, String name) throws IOException {
        // Keep raw module versions as directory names to mirror filesystem; no strict semver required.
        Map<String, ModuleVersion> versions = new LinkedHashMap<>();

        Files.list(modulePath)
            .filter(Files::isDirectory)
            .forEach(versionPath -> {
                String versionStr = versionPath.getFileName().toString();
                try {
                    ModuleVersion moduleVersion = scanModuleVersion(versionPath, versionStr);
                    versions.put(versionStr, moduleVersion);
                    logger.debug("Discovered module {}/{}/{}/{} with {} system entries", registry, namespaceName, name, versionStr, moduleVersion.systems.size());
                } catch (IOException ex) {
                    logger.warn("Failed to scan module version {}: {}", versionStr, ex.getMessage());
                }
            });

        return new ModuleIndex(registry, namespaceName, name, versions);
    }

    private ModuleVersion scanModuleVersion(Path versionPath, String version) throws IOException {
        List<String> systems = scanModuleSystems(versionPath);
        return new ModuleVersion(version, systems);
    }

    private List<String> scanModuleSystems(Path versionPath) throws IOException {
        if (!Files.isDirectory(versionPath)) {
            return List.of();
        }

        try (var stream = Files.list(versionPath)) {
            return stream
                .filter(Files::isDirectory)
                .map(path -> path.getFileName().toString())
                .toList();
        }
    }

    /**
     * Lookup provider index by full provider path key.
     */
    public Optional<ProviderIndex> getProvider(String registry, String namespaceName, String name) {
        return Optional.ofNullable(providers.get(registry + "/" + namespaceName + "/" + name));
    }

    /**
     * Lookup module index by full module path key.
     */
    public Optional<ModuleIndex> getModule(String registry, String namespaceName, String name) {
        return Optional.ofNullable(modules.get(registry + "/" + namespaceName + "/" + name));
    }

    /**
     * Get all providers (for testing).
     */
    public Map<String, ProviderIndex> getProviders() {
        return providers;
    }

    /**
     * Get all modules (for testing).
     */
    public Map<String, ModuleIndex> getModules() {
        return modules;
    }

    /**
     * Provider index data model.
     */
    public static class ProviderIndex {
        public final String registry;
        public final String namespaceName;
        public final String name;
        public final Map<String, List<Platform>> versions;

        public ProviderIndex(String registry, String namespaceName, String name, Map<String, List<Platform>> versions) {
            this.registry = registry;
            this.namespaceName = namespaceName;
            this.name = name;
            this.versions = versions;
        }
    }

    public static class ModuleIndex {
        public final String registry;
        public final String namespaceName;
        public final String name;
        public final Map<String, ModuleVersion> versions;

        public ModuleIndex(String registry, String namespaceName, String name, Map<String, ModuleVersion> versions) {
            this.registry = registry;
            this.namespaceName = namespaceName;
            this.name = name;
            this.versions = versions;
        }
    }

    public static class ModuleVersion {
        public final String version;
        public final List<String> systems;

        public ModuleVersion(String version, List<String> systems) {
            this.version = version;
            this.systems = systems;
        }
    }

    public static class Platform {
        public final String os;
        public final String arch;

        public Platform(String os, String arch) {
            this.os = os;
            this.arch = arch;
        }
    }
}