package net.terria.registry.terraform;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/modules")
public class ModuleController {

    @Autowired
    private RegistryService registryService;
    
    private static final String DEFAULT_REGISTRY = "registry.terraform.io";

    /**
     * Terraform Registry Protocol: GET /v1/modules/{namespace}/{name}/versions
     * Returns module versions from filesystem-backed index.
     */
    @GetMapping("/{namespace}/{name}/versions")
    public ResponseEntity<?> getModuleVersions(@PathVariable String namespace,
                                               @PathVariable String name) {
        return registryService.getModule(DEFAULT_REGISTRY, namespace, name)
            .map(index -> {
                List<Map<String, Object>> versions = index.versions.entrySet().stream()
                    .map(entry -> Map.of(
                        "version", entry.getKey(),
                        "systems", entry.getValue().systems
                    ))
                    .collect(Collectors.toList());

                return ResponseEntity.ok(Map.of("versions", versions));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Terraform Registry Protocol: GET /v1/modules/{namespace}/{name}/{system}/{version}/download
     * Returns a JSON payload with the source archive download URL.
     */
    @GetMapping("/{namespace}/{name}/{system}/{version}/download")
    public ResponseEntity<?> downloadModule(@PathVariable String namespace,
                                            @PathVariable String name,
                                            @PathVariable String system,
                                            @PathVariable String version) {
        return registryService.getModule(DEFAULT_REGISTRY, namespace, name)
            .map(index -> {
                if (!index.versions.containsKey(version)) {
                    return ResponseEntity.notFound().build();
                }

                String url = String.format("/storage/modules/%s/%s/%s/%s/terraform-module-%s_%s_%s.zip",
                    namespace, name, version, system, name, system, version);
                return ResponseEntity.ok(Map.of("download_url", url));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
