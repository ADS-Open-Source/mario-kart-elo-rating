package pl.com.dolittle.mkelo.controller;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.services.PersistenceService;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class StorageController {

    private PersistenceService persistenceService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") JSONObject json) {
        // persistenceService.uploadData("MKEloData.json", json);
        return new ResponseEntity<>("not supported", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = persistenceService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}

