using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

namespace net.terria.registry.terraform
{
    /// <summary>
    /// Service that scans local filesystem and builds in-memory indexes for provider and module metadata.
    /// </summary>
    public class RegistryService
    {
        private readonly ILogger<RegistryService> _logger;
        private readonly Dictionary<string, ProviderIndex> _providers = new();
        private readonly Dictionary<string, ModuleIndex> _modules = new();
        private readonly string _storagePath = string.Empty;

        public RegistryService(ILogger<RegistryService> logger, IConfiguration config)
        {
            _logger = logger;
            _storagePath = config.GetValue<string>("Registry:StoragePath") ?? "../registry-storage";
            Init();
        }

        /// <summary>
        /// Initialize in-memory service indexes from local registry storage on disk.
        /// </summary>
        private void Init()
        {
            var root = Path.Combine(_storagePath);
            if (!Directory.Exists(root))
            {
                _logger.LogWarning("Registry storage path does not exist: {Path}", root);
                return;
            }

            ScanProviders(Path.Combine(root, "providers"));
            ScanModules(Path.Combine(root, "modules"));

            _logger.LogInformation("Registry indexes rebuilt. Providers: {ProviderCount}, Modules: {ModuleCount}", _providers.Count, _modules.Count);
        }

        /// <summary>
        /// Scan the top-level providers path (e.g. <c>registry-storage/providers</c>) and index each registry subtree.
        /// </summary>
        /// <param name="providersPath">Filesystem path for provider registry roots.</param>
        private void ScanProviders(string providersPath)
        {
            if (!Directory.Exists(providersPath)) return;

            foreach (var registryDir in Directory.EnumerateDirectories(providersPath))
            {
                var registry = Path.GetFileName(registryDir);
                ScanRegistryProviders(registryDir, registry);
            }
        }

        /// <summary>
        /// Scan a provider registry root path (e.g. <c>registry.terraform.io</c>) and process namespaces.
        /// </summary>
        /// <param name="registryPath">Registry directory on disk</param>
        /// <param name="registry">Registry ID</param>
        private void ScanRegistryProviders(string registryPath, string registry)
        {
            foreach (var namespaceDir in Directory.EnumerateDirectories(registryPath))
            {
                var namespaceName = Path.GetFileName(namespaceDir);
                if (string.IsNullOrEmpty(namespaceName) || namespaceName.Equals("namespaces.json", StringComparison.OrdinalIgnoreCase))
                {
                    continue;
                }

                ScanNamespaceProviders(namespaceDir, registry, namespaceName);
            }
        }

        /// <summary>
        /// Scan a provider namespace path and index provider packages.
        /// </summary>
        /// <param name="namespacePath">Filesystem path for this namespace</param>
        /// <param name="registry">Registry ID</param>
        /// <param name="namespaceName">Namespace name</param>
        private void ScanNamespaceProviders(string namespacePath, string registry, string namespaceName)
        {
            foreach (var providerDir in Directory.EnumerateDirectories(namespacePath))
            {
                var providerName = Path.GetFileName(providerDir);
                var index = ScanProvider(providerDir, registry, namespaceName, providerName);
                _providers[$"{registry}/{namespaceName}/{providerName}"] = index;
            }
        }

        /// <summary>
        /// Scan one provider directory and collect available version entries with platform architecture metadata.
        /// </summary>
        /// <param name="providerPath">Path to provider directory</param>
        /// <param name="registry">Registry id</param>
        /// <param name="namespaceName">Namespace id</param>
        /// <param name="name">Provider name</param>
        /// <returns>Provider index object</returns>
        private ProviderIndex ScanProvider(string providerPath, string registry, string namespaceName, string name)
        {
            // Accept any version directory under provider (including symlink / alias names) as-is.
            var versions = new Dictionary<string, List<Platform>>();

            foreach (var versionDir in Directory.EnumerateDirectories(providerPath))
            {
                var versionStr = Path.GetFileName(versionDir);
                var platforms = ScanPlatforms(versionDir);
                versions[versionStr] = platforms;
                _logger.LogDebug("Discovered provider {Registry}/{Namespace}/{Name}/{Version} with {PlatformCount} platforms", registry, namespaceName, name, versionStr, platforms.Count);
            }

            return new ProviderIndex(registry, namespaceName, name, versions);
        }

        /// <summary>
        /// Scan OS/arch directories under a specific version and produce Platform entries.
        /// </summary>
        /// <param name="versionPath">Filesystem version directory path</param>
        /// <returns>List of Platform metadata</returns>
        private List<Platform> ScanPlatforms(string versionPath)
        {
            var platforms = new List<Platform>();

            if (!Directory.Exists(versionPath)) return platforms;

            foreach (var osDir in Directory.EnumerateDirectories(versionPath))
            {
                var os = Path.GetFileName(osDir);
                foreach (var archDir in Directory.EnumerateDirectories(osDir))
                {
                    var arch = Path.GetFileName(archDir);
                    platforms.Add(new Platform(os, arch));
                }
            }

            return platforms;
        }

        /// <summary>
        /// Scan the top-level modules path (e.g. <c>registry-storage/modules</c>) and index each registry subtree.
        /// </summary>
        /// <param name="modulesPath">Filesystem path for module registry roots</param>
        private void ScanModules(string modulesPath)
        {
            if (!Directory.Exists(modulesPath)) return;

            foreach (var registryDir in Directory.EnumerateDirectories(modulesPath))
            {
                var registry = Path.GetFileName(registryDir);
                ScanRegistryModules(registryDir, registry);
            }
        }

        private void ScanRegistryModules(string registryPath, string registry)
        {
            foreach (var namespaceDir in Directory.EnumerateDirectories(registryPath))
            {
                var namespaceName = Path.GetFileName(namespaceDir);
                if (string.IsNullOrEmpty(namespaceName) || namespaceName.Equals("namespaces.json", StringComparison.OrdinalIgnoreCase))
                {
                    continue;
                }

                ScanNamespaceModules(namespaceDir, registry, namespaceName);
            }
        }

        private void ScanNamespaceModules(string namespacePath, string registry, string namespaceName)
        {
            foreach (var moduleDir in Directory.EnumerateDirectories(namespacePath))
            {
                var moduleName = Path.GetFileName(moduleDir);
                var moduleIndex = ScanModule(moduleDir, registry, namespaceName, moduleName);
                _modules[$"{registry}/{namespaceName}/{moduleName}"] = moduleIndex;
            }
        }

        private ModuleIndex ScanModule(string modulePath, string registry, string namespaceName, string name)
        {
            // Keep all module version directories as provided by filesystem.
            var versions = new Dictionary<string, ModuleVersion>();

            foreach (var versionDir in Directory.EnumerateDirectories(modulePath))
            {
                var versionStr = Path.GetFileName(versionDir);
                versions[versionStr] = new ModuleVersion(versionStr, ScanPlatforms(versionDir));
                _logger.LogDebug("Discovered module {Registry}/{Namespace}/{Name}/{Version} with {PlatformCount} platforms", registry, namespaceName, name, versionStr, versions[versionStr].Platforms.Count);
            }

            return new ModuleIndex(registry, namespaceName, name, versions);
        }

        /// <summary>
        /// Returns provider index for registry/namespace/name. Null if not found.
        /// </summary>
        public ProviderIndex? GetProvider(string registry, string namespaceName, string name)
        {
            _providers.TryGetValue($"{registry}/{namespaceName}/{name}", out var index);
            return index;
        }

        /// <summary>
        /// Returns module index for registry/namespace/name. Null if not found.
        /// </summary>
        public ModuleIndex? GetModule(string registry, string namespaceName, string name)
        {
            _modules.TryGetValue($"{registry}/{namespaceName}/{name}", out var index);
            return index;
        }

        /// <summary>
        /// Get all providers (for testing).
        /// </summary>
        public IReadOnlyDictionary<string, ProviderIndex> GetProviders() => _providers;

        /// <summary>
        /// Get all modules (for testing).
        /// </summary>
        public IReadOnlyDictionary<string, ModuleIndex> GetModules() => _modules;
    }

    /// <summary>
    /// Represents a platform (OS/architecture) where a provider or module is available.
    /// </summary>
    public record Platform(string Os, string Arch);

    /// <summary>
    /// Represents a specific version of a Terraform module with its available platforms.
    /// </summary>
    public record ModuleVersion(string Version, List<Platform> Platforms);

    /// <summary>
    /// Index of all available versions and platforms for a provider.
    /// </summary>
    public record ProviderIndex(string Registry, string Namespace, string Name, Dictionary<string, List<Platform>> Versions);

    /// <summary>
    /// Index of all available versions and platforms for a module.
    /// </summary>
    public record ModuleIndex(string Registry, string Namespace, string Name, Dictionary<string, ModuleVersion> Versions);
}
