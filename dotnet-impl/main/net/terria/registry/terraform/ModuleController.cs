using Microsoft.AspNetCore.Mvc;

namespace net.terria.registry.terraform
{
    [ApiController]
    [Route("v1/modules")]
    public class ModuleController : ControllerBase
    {
        private readonly RegistryService _registryService;
        private const string DefaultRegistry = "registry.terraform.io";

        public ModuleController(RegistryService registryService)
        {
            _registryService = registryService;
        }

        /// <summary>
        /// Terraform Registry Protocol: GET /v1/modules/{namespace}/{name}/versions
        /// Returns module versions from filesystem-backed index.
        /// </summary>
        [HttpGet("{namespace}/{name}/versions")]
        public IActionResult GetModuleVersions(string @namespace, string name)
        {
            var module = _registryService.GetModule(DefaultRegistry, @namespace, name);
            if (module == null) return NotFound();

            var versions = module.versions
                .OrderByDescending(v => v.Key)
                .Select(v => new
                {
                    version = v.Key,
                    systems = v.Value.systems
                });

            return Ok(new { versions });
        }

        /// <summary>
        /// Terraform Registry Protocol: GET /v1/modules/{namespace}/{name}/{system}/{version}/download
        /// Returns a JSON payload with the source archive download URL.
        /// </summary>
        [HttpGet("{namespace}/{name}/{system}/{version}/download")]
        public IActionResult DownloadModule(string @namespace, string name, string system, string version)
        {
            var module = _registryService.GetModule(DefaultRegistry, @namespace, name);
            if (module == null) return NotFound();

            if (!module.versions.ContainsKey(version)) return NotFound();

            var url = $"/storage/modules/{@namespace}/{name}/{version}/{system}/terraform-module-{name}_{system}_{version}.zip";
            return Ok(new { download_url = url });
        }
    }
}