package com.imagetopdf;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;

@Service
public class ImagetoPDFService {
	
	@Autowired
	ImagetoPDFRepository repository;

	public void save(MultipartFile[] files, String fileName) throws DocumentException {
		try {
			List<ImagetoPDF> pdf = ImagetoPDFHelper.imageToPDF(files, fileName);
			repository.saveAll(pdf);
		} catch (IOException e) {
			throw new RuntimeException("fail to store image data: " + e.getMessage());
		}
	}

}
