package net.terria.registry.terraform.controller;

import net.terria.registry.terraform.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    private RegistryService registryService;

    /**
     * GET /modules/{registry}/{namespace}/{name}/versions
     * Returns module versions from filesystem-backed index.
     */
    @GetMapping("/{registry}/{namespace}/{name}/versions")
    public ResponseEntity<?> getModuleVersions(@PathVariable String registry,
                                               @PathVariable String namespace,
                                               @PathVariable String name) {
        return registryService.getModule(registry, namespace, name)
            .map(index -> {
                List<Map<String, Object>> versions = index.versions.entrySet().stream()
                    .map(entry -> Map.of(
                        "version", entry.getKey(),
                        "platforms", entry.getValue().platforms.stream()
                            .map(p -> Map.of("os", p.os, "arch", p.arch))
                            .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());

                return ResponseEntity.ok(Map.of("versions", versions));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
