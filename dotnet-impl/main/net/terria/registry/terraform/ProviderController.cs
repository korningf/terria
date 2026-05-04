using Microsoft.AspNetCore.Mvc;

namespace net.terria.registry.terraform
{
    [ApiController]
    [Route("v1/providers")]
    public class ProviderController : ControllerBase
    {
        private readonly RegistryService _registryService;
        private const string DefaultRegistry = "registry.terraform.io";

        public ProviderController(RegistryService registryService)
        {
            _registryService = registryService;
        }

        /// <summary>
        /// Get provider versions endpoint.
        /// Terraform Registry Protocol: GET /v1/providers/{namespace}/{name}/versions
        /// </summary>
        [HttpGet("{namespace}/{name}/versions")]
        public IActionResult GetProviderVersions(string @namespace, string name)
        {
            var index = _registryService.GetProvider(DefaultRegistry, @namespace, name);
            if (index == null) return NotFound();

            var versions = index.versions
                .OrderByDescending(v => v.Key)
                .Select(v => new
                {
                    version = v.Key.ToString(),
                    platforms = v.Value.Select(p => new { os = p.os, arch = p.arch })
                });

            return Ok(new { versions });
        }

        /// <summary>
        /// Download metadata endpoint (redirect target can be mapped externally).
        /// Terraform Registry Protocol: GET /v1/providers/{namespace}/{name}/{version}/download/{os}/{arch}
        /// </summary>
        [HttpGet("{namespace}/{name}/{version}/download/{os}/{arch}")]
        public IActionResult DownloadProvider(string @namespace, string name, string version, string os, string arch)
        {
            var url = $"/storage/providers/{@namespace}/{name}/{version}/{os}/{arch}/terraform-provider-{name}_{version}_{os}_{arch}.zip";
            return Ok(new { download_url = url });
        }
    }
}