package com.imagetopdf;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImagetoPdfApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ImagetoPdfApplication.class, args);
		try (WatchService service = FileSystems.getDefault().newWatchService()) {
			Map<WatchKey, Path> keyMap = new HashMap<>();
			File root = new File("C:\\Users\\kavin\\eclipse-workspace\\ImagetoPDF\\source");
			if (!root.exists()) {
				root.mkdir();
			}
			Path path = Paths.get("source");
			keyMap.put(path.register(service, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY), path);
			WatchKey watchKey;
			do {
				watchKey = service.take();
				Path eventDir = keyMap.get(watchKey);
				for (WatchEvent<?> event : watchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path eventPath = (Path) event.context();
					System.out.println(eventDir + ":" + kind + ":" + eventPath);
				}
			} while (watchKey.reset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
