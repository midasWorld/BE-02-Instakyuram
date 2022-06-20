package com.kdt.instakyuram.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ImageUploader {

	private ImageUploader() {}

	private static final Logger logger = LoggerFactory.getLogger(ImageUploader.class);

	// TODO : 추후 커스텀 예외로 처리할 것
	public static void upload(MultipartFile image, String serverFileName, String path) {
		try {
			image.transferTo(new File(path + serverFileName));
		} catch (IOException e) {
			logger.warn("이미지 파일을 저장하면서 오류가 발생하였습니다.");
			throw new RuntimeException("이미지 파일을 저장하면서 오류가 발생하였습니다.", e);
		}

	}

}