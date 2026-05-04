using System;
using System.IO;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using NUnit.Framework;
using net.terria.registry.terraform;

namespace net.terria.registry.terraform
{
    [TestFixture]
    public class RegistryServiceTest
    {
        private string _tempDir;

        [SetUp]
        public void Setup()
        {
            _tempDir = Path.Combine(Path.GetTempPath(), Guid.NewGuid().ToString());
            Directory.CreateDirectory(_tempDir);
        }

        [TearDown]
        public void TearDown()
        {
            if (Directory.Exists(_tempDir))
            {
                Directory.Delete(_tempDir, true);
            }
        }

        [Test]
        public void TestInitWithEmptyStorage()
        {
            var config = new ConfigurationBuilder()
                .AddInMemoryCollection(new[] { new KeyValuePair<string, string>("registry.storage.path", _tempDir) })
                .Build();

            var logger = LoggerFactory.Create(builder => builder.AddConsole()).CreateLogger<RegistryService>();

            var service = new RegistryService(logger, config);

            Assert.That(service.GetProviders().Count, Is.EqualTo(0));
            Assert.That(service.GetModules().Count, Is.EqualTo(0));
        }

        [Test]
        public void TestInitWithProvider()
        {
            // Create provider structure under registry.terraform.io/providers
            var providersPath = Path.Combine(_tempDir, "registry.terraform.io", "providers");
            var namespacePath = Path.Combine(providersPath, "hashicorp");
            var providerPath = Path.Combine(namespacePath, "aws");
            var versionPath = Path.Combine(providerPath, "1.0.0");
            var platformPath = Path.Combine(versionPath, "linux", "amd64");

            Directory.CreateDirectory(platformPath);

            var config = new ConfigurationBuilder()
                .AddInMemoryCollection(new[] { new KeyValuePair<string, string>("registry.storage.path", _tempDir) })
                .Build();

            var logger = LoggerFactory.Create(builder => builder.AddConsole()).CreateLogger<RegistryService>();

            var service = new RegistryService(logger, config);

            Assert.That(service.GetProviders().Count, Is.EqualTo(1));
            Assert.That(service.GetProvider("registry.terraform.io", "hashicorp", "aws"), Is.Not.Null);
        }

        // Add more tests as needed
    }
}