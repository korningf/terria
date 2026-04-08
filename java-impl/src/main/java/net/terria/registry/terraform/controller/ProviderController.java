package net.terria.registry.terraform.controller;

import net.terria.registry.terraform.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private RegistryService registryService;

    /**
     * Get provider versions endpoint.
     * API matches Terraform registry provider versions metadata.
     */
    @GetMapping("/{registry}/{namespace}/{name}/versions")
    public ResponseEntity<?> getProviderVersions(@PathVariable String registry,
                                                 @PathVariable String namespace,
                                                 @PathVariable String name) {
        return registryService.getProvider(registry, namespace, name)
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
     */
    @GetMapping("/{registry}/{namespace}/{name}/{version}/download/{os}/{arch}")
    public ResponseEntity<?> downloadProvider(@PathVariable String registry,
                                              @PathVariable String namespace,
                                              @PathVariable String name,
                                              @PathVariable String version,
                                              @PathVariable String os,
                                              @PathVariable String arch) {
        String url = String.format("/storage/providers/%s/%s/%s/%s/%s/%s/terraform-provider-%s_%s_%s_%s.zip",
            registry, namespace, name, version, os, arch, name, version, os, arch);
        return ResponseEntity.ok(Map.of("download_url", url));
    }

}
