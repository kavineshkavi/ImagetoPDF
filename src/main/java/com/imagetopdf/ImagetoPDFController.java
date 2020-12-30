package com.imagetopdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImagetoPDFController {
	@Autowired
	ImagetoPDFService fileService;

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping("/uploadFiles")
	public String uploadFile(@RequestParam("files") MultipartFile[] files, @RequestParam("filename") String fileName,
			RedirectAttributes redirectAttributes) throws Exception {

		if (ImagetoPDFHelper.hasImageFormat(files)) {
			try {
				fileService.save(files, fileName);
				redirectAttributes.addFlashAttribute("message", "You successfully uploaded all files!");
				return "redirect:/";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", "Could not upload the file!");
				return "redirect:/";
			}
		}
		redirectAttributes.addFlashAttribute("message", "Please upload an image file!");
		return "redirect:/";
	}
}
