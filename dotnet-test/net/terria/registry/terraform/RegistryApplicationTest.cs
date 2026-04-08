using System.Net;
using Microsoft.AspNetCore.Mvc.Testing;
using NUnit.Framework;
using net.terria.registry.terraform;

namespace net.terria.registry.terraform
{
    [TestFixture]
    public class RegistryApplicationTest
    {
        private WebApplicationFactory<RegistryApplication> _factory;

        [SetUp]
        public void Setup()
        {
            _factory = new WebApplicationFactory<RegistryApplication>();
        }

        [TearDown]
        public void TearDown()
        {
            _factory.Dispose();
        }

        [Test]
        public async Task TestProvidersVersionsEndpointReturnsNotFoundWhenNoData()
        {
            var client = _factory.CreateClient();

            var response = await client.GetAsync("/providers/registry.terraform.io/hashicorp/aws/versions");

            Assert.That(response.StatusCode, Is.EqualTo(HttpStatusCode.NotFound));
        }

        [Test]
        public async Task TestModulesVersionsEndpointReturnsNotFoundWhenNoData()
        {
            var client = _factory.CreateClient();

            var response = await client.GetAsync("/modules/registry.terraform.io/hashicorp/vpc/aws/versions");

            Assert.That(response.StatusCode, Is.EqualTo(HttpStatusCode.NotFound));
        }

        // Add more integration tests as needed
    }
}