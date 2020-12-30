package com.imagetopdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class ImagetoPDFHelper {

	@Autowired
	static ImagetoPDFService fileService;

	public static boolean hasImageFormat(MultipartFile[] files) {
		for (MultipartFile file : files) {
			if (!file.getContentType().startsWith("image")) {
				return false;
			}
		}
		return true;
	}

	public static List<ImagetoPDF> imageToPDF(MultipartFile[] files, String fileName)
			throws DocumentException, MalformedURLException, IOException {

		File root = new File("C:\\Users\\kavin\\eclipse-workspace\\ImagetoPDF\\source");
		if (!root.exists()) {
			root.mkdir();
		}

		List<ImagetoPDF> pdfList = new ArrayList<ImagetoPDF>();

		List<String> imageName = new ArrayList<String>();
		for (MultipartFile file : files) {
			Path copyLocation = Paths.get(root + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			imageName.add(file.getOriginalFilename());
		}

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(new File(root, fileName + ".pdf")));
		document.open();

		for (String f : imageName) {
			document.newPage();
			Image image = Image.getInstance(new File(root, f).getAbsolutePath());
			image.setAlignment(Element.ALIGN_CENTER);
			float imageWidth = image.getWidth();
			if (imageWidth > document.getPageSize().getWidth()) {
				int indentation = 0;
				float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()
						- indentation) / image.getWidth()) * 50;
				image.scalePercent(scaler);
			}
			document.add(image);

			ImagetoPDF pdfObject = new ImagetoPDF();
			pdfObject.setImageName(f);
			pdfObject.setPdfName(fileName);

			pdfList.add(pdfObject);
		}
		document.close();

		return pdfList;

	}

}
