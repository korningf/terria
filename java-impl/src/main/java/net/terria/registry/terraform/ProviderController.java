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
@RequestMapping("/v1/providers")
public class ProviderController {

    @Autowired
    private RegistryService registryService;
    
    private static final String DEFAULT_REGISTRY = "registry.terraform.io";

    /**
     * Get provider versions endpoint.
     * Terraform Registry Protocol: GET /v1/providers/{namespace}/{type}/versions
     */
    @GetMapping("/{namespace}/{name}/versions")
    public ResponseEntity<?> getProviderVersions(@PathVariable String namespace,
                                                 @PathVariable String name) {
        return registryService.getProvider(DEFAULT_REGISTRY, namespace, name)
            .map(index -> {
                List<Map<String, Object>> versions = index.versions.entrySet().stream()
                    .map(entry -> Map.of(
                        "version", entry.getKey().toString(),
                        "platforms", entry.getValue().stream()
                            .map(p -> Map.of("os", p.os, "arch", p.arch))
                            .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());

                return ResponseEntity.ok(Map.of("versions", versions));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Download metadata endpoint (redirect target can be mapped externally).
     * Terraform Registry Protocol: GET /v1/providers/{namespace}/{type}/{version}/download/{os}/{arch}
     */
    @GetMapping("/{namespace}/{name}/{version}/download/{os}/{arch}")
    public ResponseEntity<?> downloadProvider(@PathVariable String namespace,
                                              @PathVariable String name,
                                              @PathVariable String version,
                                              @PathVariable String os,
                                              @PathVariable String arch) {
        String url = String.format("/storage/providers/%s/%s/%s/%s/%s/terraform-provider-%s_%s_%s_%s.zip",
            namespace, name, version, os, arch, name, version, os, arch);
        return ResponseEntity.ok(Map.of("download_url", url));
    }

}
